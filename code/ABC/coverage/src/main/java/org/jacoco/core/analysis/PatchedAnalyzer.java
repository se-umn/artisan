/*******************************************************************************
 * Copyright (c) 2009, 2021 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.core.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.internal.ContentTypeDetector;
import org.jacoco.core.internal.InputStreams;
import org.jacoco.core.internal.Pack200Streams;
import org.jacoco.core.internal.analysis.ClassCoverageImpl;
import org.jacoco.core.internal.analysis.PatchedClassAnalyzer;
import org.jacoco.core.internal.analysis.StringPool;
import org.jacoco.core.internal.analysis.filter.IFilter;
import org.jacoco.core.internal.analysis.filter.IFilterContext;
import org.jacoco.core.internal.analysis.filter.IFilterOutput;
import org.jacoco.core.internal.data.CRC64;
import org.jacoco.core.internal.flow.ClassProbesAdapter;
import org.jacoco.core.internal.instr.InstrSupport;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * An {@link PatchedAnalyzer} instance processes a set of Java class files and
 * calculates coverage data for them. For each class file the result is reported
 * to a given {@link ICoverageVisitor} instance. In addition the
 * {@link PatchedAnalyzer} requires a {@link ExecutionDataStore} instance that
 * holds the execution data for the classes to analyze. The
 * {@link PatchedAnalyzer} offers several methods to analyze classes from a
 * variety of sources.
 */
public class PatchedAnalyzer {

    private final ExecutionDataStore executionData;

    private final ICoverageVisitor coverageVisitor;

    private final StringPool stringPool;

    private final Map<String, List<Integer>> whiteList;

    /**
     * Creates a new analyzer reporting to the given output.
     *
     * @param executionData   execution data
     * @param coverageVisitor the output instance that will coverage data for every
     *                        analyzed class
     */
    public PatchedAnalyzer(final ExecutionDataStore executionData, final ICoverageVisitor coverageVisitor) {
        this.executionData = executionData;
        this.coverageVisitor = coverageVisitor;
        this.stringPool = new StringPool();
        // Remeber the lineNumbers that correspond to our elements of interest: method
        // invocations
        // TODO Extend this to array operation and field accesses only if necessary
        this.whiteList = new HashMap<String, List<Integer>>();
    }

    /**
     * Creates an ASM class visitor for analysis.
     *
     * @param classid   id of the class calculated with {@link CRC64}
     * @param className VM name of the class
     * @return ASM visitor to write class definition to
     */
    private ClassVisitor createAnalyzingVisitor(final long classid, final String className) {
        final ExecutionData data = executionData.get(classid);
        final boolean[] probes;
        final boolean noMatch;
        if (data == null) {
            probes = null;
            noMatch = executionData.contains(className);
        } else {
            probes = data.getProbes();
            noMatch = false;
        }
        // This one is responsible for computing coverage and probably the one who
        // should filter it out
        final ClassCoverageImpl coverage = new ClassCoverageImpl(className, classid, noMatch);

        // Filters anything that is NOT an invoke or dynamicinvoke or a virtualinvoke or
        // something...
        IFilter leaveOnlyInvocations = new IFilter() {

            @Override
            public void filter(final MethodNode methodNode, final IFilterContext context, final IFilterOutput output) {
                for (final AbstractInsnNode i : methodNode.instructions) {
                    filterNonMethodInvocations(i, output);
                }
            }

            private void filterNonMethodInvocations(final AbstractInsnNode start, final IFilterOutput output) {

                if (!(start.getOpcode() == Opcodes.INVOKEDYNAMIC || start.getOpcode() == Opcodes.INVOKESPECIAL
                        || start.getOpcode() == Opcodes.INVOKESTATIC || start.getOpcode() == Opcodes.INVOKEINTERFACE
                        || start.getOpcode() == Opcodes.INVOKEVIRTUAL)) {
                    output.ignore(start, start);
                }
            }

        };

        IFilter filterStringRelatedOperations = new IFilter() {

            @Override
            public void filter(final MethodNode methodNode, final IFilterContext context, final IFilterOutput output) {
                for (final AbstractInsnNode i : methodNode.instructions) {
                    filterStringRelatedMethods(i, output);
                }
            }

            private void filterStringRelatedMethods(final AbstractInsnNode start, final IFilterOutput output) {
                if (start.getOpcode() == Opcodes.INVOKEDYNAMIC || start.getOpcode() == Opcodes.INVOKESPECIAL
                        || start.getOpcode() == Opcodes.INVOKESTATIC || start.getOpcode() == Opcodes.INVOKEINTERFACE
                        || start.getOpcode() == Opcodes.INVOKEVIRTUAL) {

                    if (start instanceof MethodInsnNode) {
                        MethodInsnNode methodNode = (MethodInsnNode) start;
                        if ("java/lang/String".equals(methodNode.owner)) {
                            output.ignore(start, start);
                        }
                    }
                }
            }

        };

        final PatchedClassAnalyzer analyzer = new PatchedClassAnalyzer(coverage, probes, stringPool,
                leaveOnlyInvocations, filterStringRelatedOperations) {
            @Override
            public void visitEnd() {
                super.visitEnd();
                coverageVisitor.visitCoverage(coverage);
            }
        };
        return new ClassProbesAdapter(analyzer, false);
    }

    public class LogMethodClassVisitor extends ClassVisitor {

        private String className;

        public LogMethodClassVisitor(ClassVisitor cv, String pClassName) {
            super(Opcodes.ASM5, cv);
            className = pClassName;
//            System.out.println("\n\nPROCESSING CLASS:" + className);
            whiteList.put(className, new ArrayList<Integer>());
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//            System.out.println("PROCESSING METHOD " + name + " " + desc);
            return new LogMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions), className);
        }

    }

    public class LogMethodVisitor extends MethodVisitor {

        private String className;
        private int lineNumber;

        public LogMethodVisitor(MethodVisitor methodVisitor, String className) {
            super(/* latest api = */ Opcodes.ASM9, methodVisitor);
            this.className = className;
        }

        public void visitInvokeDynamicInsn(String name, String descriptor,
                org.objectweb.asm.Handle bootstrapMethodHandle, Object[] bootstrapMethodArguments) {
//            System.out.println(lineNumber + " " + name + " " + descriptor);
            whiteList.get(this.className).add(lineNumber);

        };

        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
//            System.out.println("ExecDump.LogMethodVisitor.visitMethodInsn() " +  + "");
            if (!"java/lang/String".equals(owner)) {
//                System.out.println(
//                        lineNumber + " " + owner + "." + name + " " + descriptor + " " + Printer.OPCODES[opcode]);
                whiteList.get(this.className).add(lineNumber);
            }
        };

        // Keep track of the current line processed by the visitor
        @Override
        public void visitLineNumber(int line, Label start) {
            lineNumber = line;
        }

    }

    private void analyzeClass(final byte[] source) {
        final long classId = CRC64.classId(source);
        final ClassReader reader = InstrSupport.classReaderFor(source);
        if ((reader.getAccess() & Opcodes.ACC_MODULE) != 0) {
            return;
        }
        if ((reader.getAccess() & Opcodes.ACC_SYNTHETIC) != 0) {
            return;
        }
        // This is the standard analysis
        final ClassVisitor visitor = createAnalyzingVisitor(classId, reader.getClassName());
        reader.accept(visitor, 0);
        // This is our "private analysis" - we rely on global state !
        final ClassVisitor logClassVisitor = new LogMethodClassVisitor(null, reader.getClassName());
        reader.accept(logClassVisitor, 0);
    }

    /**
     * Analyzes the class definition from a given in-memory buffer.
     *
     * @param buffer   class definitions
     * @param location a location description used for exception messages
     * @throws IOException if the class can't be analyzed
     */
    public void analyzeClass(final byte[] buffer, final String location) throws IOException {
        try {
            analyzeClass(buffer);
        } catch (final RuntimeException cause) {
            throw analyzerError(location, cause);
        }
    }

    /**
     * Analyzes the class definition from a given input stream. The provided
     * {@link InputStream} is not closed by this method.
     *
     * @param input    stream to read class definition from
     * @param location a location description used for exception messages
     * @throws IOException if the stream can't be read or the class can't be
     *                     analyzed
     */
    public void analyzeClass(final InputStream input, final String location) throws IOException {
        final byte[] buffer;
        try {
            buffer = InputStreams.readFully(input);
        } catch (final IOException e) {
            throw analyzerError(location, e);
        }
        analyzeClass(buffer, location);
    }

    private IOException analyzerError(final String location, final Exception cause) {
        final IOException ex = new IOException(String.format("Error while analyzing %s.", location));
        ex.initCause(cause);
        return ex;
    }

    /**
     * Analyzes all classes found in the given input stream. The input stream may
     * either represent a single class file, a ZIP archive, a Pack200 archive or a
     * gzip stream that is searched recursively for class files. All other content
     * types are ignored. The provided {@link InputStream} is not closed by this
     * method.
     *
     * @param input    input data
     * @param location a location description used for exception messages
     * @return number of class files found
     * @throws IOException if the stream can't be read or a class can't be analyzed
     */
    public int analyzeAll(final InputStream input, final String location) throws IOException {
        final ContentTypeDetector detector;
        try {
            detector = new ContentTypeDetector(input);
        } catch (final IOException e) {
            throw analyzerError(location, e);
        }
        switch (detector.getType()) {
        case ContentTypeDetector.CLASSFILE:
            analyzeClass(detector.getInputStream(), location);
            return 1;
        case ContentTypeDetector.ZIPFILE:
            return analyzeZip(detector.getInputStream(), location);
        case ContentTypeDetector.GZFILE:
            return analyzeGzip(detector.getInputStream(), location);
        case ContentTypeDetector.PACK200FILE:
            return analyzePack200(detector.getInputStream(), location);
        default:
            return 0;
        }
    }

    /**
     * Analyzes all class files contained in the given file or folder. Class files
     * as well as ZIP files are considered. Folders are searched recursively.
     *
     * @param file file or folder to look for class files
     * @return number of class files found
     * @throws IOException if the file can't be read or a class can't be analyzed
     */
    public int analyzeAll(final File file) throws IOException {
        int count = 0;
        if (file.isDirectory()) {
            for (final File f : file.listFiles()) {
                count += analyzeAll(f);
            }
        } else {
            final InputStream in = new FileInputStream(file);
            try {
                count += analyzeAll(in, file.getPath());
            } finally {
                in.close();
            }
        }
        return count;
    }

    /**
     * Analyzes all classes from the given class path. Directories containing class
     * files as well as archive files are considered.
     *
     * @param path    path definition
     * @param basedir optional base directory, if <code>null</code> the current
     *                working directory is used as the base for relative path
     *                entries
     * @return number of class files found
     * @throws IOException if a file can't be read or a class can't be analyzed
     */
    public int analyzeAll(final String path, final File basedir) throws IOException {
        int count = 0;
        final StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
        while (st.hasMoreTokens()) {
            count += analyzeAll(new File(basedir, st.nextToken()));
        }
        return count;
    }

    private int analyzeZip(final InputStream input, final String location) throws IOException {
        final ZipInputStream zip = new ZipInputStream(input);
        ZipEntry entry;
        int count = 0;
        while ((entry = nextEntry(zip, location)) != null) {
            count += analyzeAll(zip, location + "@" + entry.getName());
        }
        return count;
    }

    private ZipEntry nextEntry(final ZipInputStream input, final String location) throws IOException {
        try {
            return input.getNextEntry();
        } catch (final IOException e) {
            throw analyzerError(location, e);
        }
    }

    private int analyzeGzip(final InputStream input, final String location) throws IOException {
        GZIPInputStream gzipInputStream;
        try {
            gzipInputStream = new GZIPInputStream(input);
        } catch (final IOException e) {
            throw analyzerError(location, e);
        }
        return analyzeAll(gzipInputStream, location);
    }

    private int analyzePack200(final InputStream input, final String location) throws IOException {
        InputStream unpackedInput;
        try {
            unpackedInput = Pack200Streams.unpack(input);
        } catch (final IOException e) {
            throw analyzerError(location, e);
        }
        return analyzeAll(unpackedInput, location);
    }

    public boolean isLineNumberWhiteListed(String className, int i) {
        return this.whiteList.containsKey(className) && this.whiteList.get(className).contains(i);
    }

}
