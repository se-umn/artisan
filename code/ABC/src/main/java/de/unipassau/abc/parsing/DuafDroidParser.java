package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.DataNode;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.PrimitiveValue;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.tracing.Trace;
import de.unipassau.abc.utils.JimpleUtils;

/**
 * Parses the traces collected over android devices using the dyniac/duafdroid
 * code.
 * 
 * Each class is either an user class (for which we can generate carved tests),
 * or a library class. Library classes are either Android system Classes (also
 * Java lang) or 3rd party libraries, which we might invoke/mock but for which
 * we cannot generate tests.
 * 
 * Parser "only" parse the trace files, and generates the data structures
 * required for carving. Optionally, it can store/serialize/deserialize WIP
 * artifacts to file(s)
 * 
 * Library calls are identified by [>>] while user calls are identified by [>]
 * 
 * @author gambi
 *
 */
public class DuafDroidParser {

    private static final Logger logger = LoggerFactory.getLogger(DuafDroidParser.class);

    public interface ParserCLI {
        @Option(longName = "trace-files")
        List<File> getTraceFiles();

        @Option(longName = "store-artifacts-to", defaultValue = "./wip")
        File getOutputDir();
    }

    /**
     * Parse the traceFilePath and generates the required DataStructures
     * 
     * TODO HARD TO TEST !!!
     * 
     * @param traceFilePath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws CarvingException
     */
    public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parseTrace(File traceFile)
            throws FileNotFoundException, IOException, CarvingException {

        AtomicInteger invocationCount = new AtomicInteger(0);

        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> result = null;
        /*
         * Read the file into a set of string, this might be a problem for long
         * traces...
         */
        List<String> lines = new ArrayList<>();
        lines.addAll(Files.readAllLines(traceFile.toPath(), Charset.defaultCharset()));

        /*
         * Trace should start with opening line: ---- STARTING TRACING for
         * /com.farmerbb.notepad_107 ----
         * 
         * TODO We assume that a trace ends when it ends ;) that is System.exit
         * might have not been called
         */
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();
        CallGraph callGraph = new CallGraph();

        int initialPosition = 1;
        for (int i = initialPosition; i < lines.size(); i++) {

            String each = lines.get(i);

            String[] parsedLine = Trace.parseLine(each);

            if (parsedLine.length != 4) {
                throw new RuntimeException("Cannot parse " + i + " -- " + each);
            }

            // Flag the call as library or user
            String openingToken = parsedLine[0];
            // Method owner if any or empty string otherwise
            String methodOwner = parsedLine[1];
            // Method Signature - a la JIMPLE
            String methodSignature = parsedLine[2];
            // Actual Parameters
            String parameterString = parsedLine[3];

            // Currently analyzes method invocation in the trace
            MethodInvocation methodInvocation = null;

            switch (openingToken) {
            case Trace.METHOD_START_TOKEN:
            case Trace.LIB_METHOD_START_TOKEN:
                //
                String[] actualParameters = Trace.getActualParameters(methodSignature, parameterString);
                // This is the basic carving object, a method call/method
                // invocation
                methodInvocation = new MethodInvocation(invocationCount.incrementAndGet(), methodSignature,
                        actualParameters);
                methodInvocation.setLibraryCall(openingToken.equals(Trace.LIB_METHOD_START_TOKEN));
                methodInvocation.setStatic(methodOwner.isEmpty());
                // Store parsing data
                callGraph.push(methodInvocation);
                executionFlowGraph.enqueueMethodInvocations(methodInvocation);
                dataDependencyGraph.addMethodInvocation(methodInvocation);
                // Update method ownership information for non static and non
                // constructor methods
                if (!(methodInvocation.isStatic() || JimpleUtils.isConstructor(methodSignature))) {
                    // Method Owner is Type@ID, ID=0 if instance is null
                    ObjectInstance owner = new ObjectInstance(methodOwner);
                    methodInvocation.setOwner(owner);
                    dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, owner);
                    executionFlowGraph.addOwnerToMethodInvocation(methodInvocation, owner);
                }
                logger.trace(
                        "Method invocation:" + methodInvocation + "" + ((methodInvocation.isStatic()) ? " static" : "")
                                + ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
                break;
            case Trace.METHOD_END_TOKEN:
                String returnValue = parameterString.isEmpty() ? null : parameterString;
                // TODO We cannot distinguish static from non-static calls from
                // the methodSignature only

                /*
                 * Retrieve the last method invocation registered Check that
                 * this corresponds to the one we just parsed Method Signatures
                 * must match and owner must match unless this is a call to
                 * constructor
                 */
                methodInvocation = callGraph.pop();
                if (!methodInvocation.getMethodSignature().equals(methodSignature)) {
                    throw new RuntimeException("Cannot parse " + i + " -- " + each + ". Unexpected method return from "
                            + methodInvocation);
                }
                // Owner should match for non constructor calls. For static this
                // is empty/null on both sides?
                if (!JimpleUtils.isConstructor(methodSignature)) {
                    String storedMethodOwner = methodInvocation.getOwner() != null
                            ? methodInvocation.getOwner().getObjectId() : "";
                    if (!storedMethodOwner.equals(methodOwner)) {
                        throw new RuntimeException("Cannot parse " + i + " -- " + each
                                + ". Method owner does not match with " + storedMethodOwner);
                    }
                }

                /*
                 * At this point we found a matching method call we update the
                 * data structures
                 */
                if (JimpleUtils.isConstructor(methodSignature)) {
                    // Ownership info for constructors arrives by the end of the
                    // invocation...
                    ObjectInstance owner = new ObjectInstance(methodOwner);
                    methodInvocation.setOwner(owner);
                    dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, owner);
                    executionFlowGraph.addOwnerToMethodInvocation(methodInvocation, owner);
                }

                String returnType = methodSignature.trim().split(" ")[1];
                if (!JimpleUtils.isVoid(returnType)) {
                    // TODO Do we need to handle NUll values in some special way
                    // ?
                    dataDependencyGraph.addDataDependencyOnReturn(methodInvocation, returnValue);
                }
                break;
            case Trace.EXCEPTION_METHOD_END_TOKEN:
                // TODO ATM We lack return/method ends from exceptions
                logger.warn("CANNOT HANDLE " + openingToken + each);
                break;
            default:
                logger.error("WARNING SKIP THE FOLLOWING LINE ! " + each);
                break;
            }
        }

        callGraph.verify();

        result = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(executionFlowGraph,
                dataDependencyGraph, callGraph);

        // TODO There might be the need to perform some additional sanity check
        // ?!
        return result;

    }

    public static void main(String[] args) {

        long startTime = System.nanoTime();

        try {
            ParserCLI cli = CliFactory.parseArguments(ParserCLI.class, args);

            Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraceFiles = new HashMap<>();
            DuafDroidParser parser = new DuafDroidParser();
            // Start the parsing
            for (File traceFile : cli.getTraceFiles()) {
                try {
                    logger.debug("Parsing : " + traceFile);
                    parsedTraceFiles.put(traceFile.getAbsolutePath(), parser.parseTrace(traceFile));
                } catch (Throwable e) {
                    logger.error("Failed to parse" + traceFile, e);
                }
            }

            // Serialize the parsed graphs to file so maybe we can avoid
            // re-parsing the trace over and over...

            File outputArtifactTo = cli.getOutputDir();
            if (!outputArtifactTo.exists()) {
                Files.createDirectories(outputArtifactTo.toPath(), new FileAttribute[] {});
            }

            try (OutputStream writer = new FileOutputStream(outputArtifactTo)) {
                XStream xStream = new XStream();
                xStream.toXML(parsedTraceFiles, writer);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
