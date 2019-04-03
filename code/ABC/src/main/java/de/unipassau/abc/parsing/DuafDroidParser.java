package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.DataNode;
import de.unipassau.abc.carving.DataNodeFactory;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.exceptions.CarvingException;
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
 * Synthetic calls are identified by [>s]
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

            // We must call this every time we start a new trace to ensure it removes spurious/uncomplete onDestroy methods
            if (each.contains("---- STARTING TRACING for")) {
                List<MethodInvocation> invocationsToDrop = callGraph.verify();
                for( MethodInvocation toDrop : invocationsToDrop ){
                    executionFlowGraph.dequeue( toDrop );
                    dataDependencyGraph.removeMethodInvocation(toDrop);
                }

                continue;
            }

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

            boolean isSyntheticMethod = false;
            switch (openingToken) {

            case Trace.SYNTHETIC_METHOD_START_TOKEN:
            case Trace.METHOD_START_TOKEN:
            case Trace.LIB_METHOD_START_TOKEN:
                /*
                 * This is the basic carving object, a method call/method
                 * invocation
                 */
                methodInvocation = new MethodInvocation(invocationCount.incrementAndGet(), methodSignature);
                /*
                 * Explicitly set the relevant attributes of the object
                 */
                methodInvocation.setLibraryCall(openingToken.equals(Trace.LIB_METHOD_START_TOKEN));
                methodInvocation.setSyntheticMethod(openingToken.equals(Trace.SYNTHETIC_METHOD_START_TOKEN));
                /*
                 * A call which lacks the owner and is not an instance
                 * constructor must be static. Note there's no way from the
                 * methodSignature to distinguish static vs instance calls
                 */
                if (!JimpleUtils.isConstructor(methodSignature)) {
                    methodInvocation.setStatic(methodOwner.isEmpty());
                }

                /*
                 * Update the parsing data
                 */
                executionFlowGraph.enqueueMethodInvocations(methodInvocation);
                callGraph.push(methodInvocation);
                dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocation);

                /*
                 * Update data dependencies and link the various objects
                 */

                /*
                 * Method Ownership for non static, non constructor method
                 * (including static initializers)
                 */
                if (!(methodInvocation.isStatic() || JimpleUtils.isConstructorOrClassConstructor(methodSignature))) {
                    ObjectInstance owner = new ObjectInstance(methodOwner);
                    methodInvocation.setOwner(owner);
                    dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, owner);
                    // executionFlowGraph.addOwnerToMethodInvocation(methodInvocation,
                    // owner);
                }

                /*
                 * Parameters
                 */
                String[] actualParameters = Trace.getActualParameters(methodSignature, parameterString);
                String[] formalParameters = Trace.getFormalParameters(methodSignature);
                List<DataNode> actualParameterInstances = new ArrayList<>();

                for (int position = 0; position < actualParameters.length; position++) {
                    String actualParameterAsString = actualParameters[position];
                    String formalParameter = formalParameters[position];
                    DataNode actualParameter = DataNodeFactory.get(formalParameter, actualParameterAsString);
                    actualParameterInstances.add(actualParameter);
                    dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocation, actualParameter, position);
                }
                methodInvocation.setActualParameterInstances(actualParameterInstances);

                logger.trace(
                        "Method invocation:" + methodInvocation + "" + ((methodInvocation.isStatic()) ? " static" : "")
                                + ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
                break;
            case Trace.METHOD_END_TOKEN:
                String returnValueAsString = parameterString.isEmpty() ? null : parameterString;
                /*
                 * Retrieve the last method invocation registered Check that
                 * this corresponds to the one we just parsed Method Signatures
                 * must match and owner must match unless this is a call to
                 * constructor
                 */
                methodInvocation = callGraph.pop();

                /*
                 * Check this is actually the call we are looking for
                 */
                if (!methodInvocation.getMethodSignature().equals(methodSignature)) {

                    List<MethodInvocation> subsumingMethods = callGraph
                            .getOrderedSubsumingMethodInvocationsFor(methodInvocation);

                    throw new RuntimeException(
                            "Cannot parse " + i + " -- " + each + ". Unexpected method return from " + methodInvocation
                                    + "\nSubsuming methods; " + subsumingMethods.toString().replaceAll(",", "\n"));
                }

                if (!methodInvocation.isStatic() && !JimpleUtils.isConstructorOrClassConstructor(methodSignature)) {
                    String storedMethodOwner = methodInvocation.getOwner().getObjectId();
                    if (!storedMethodOwner.equals(methodOwner)) {

                        List<MethodInvocation> subsumingMethods = callGraph
                                .getOrderedSubsumingMethodInvocationsFor(methodInvocation);

                        throw new RuntimeException("Cannot parse " + i + " -- " + each
                                + ". Method owner does not match with " + storedMethodOwner + "\nSubsuming methods; "
                                + subsumingMethods.toString().replaceAll(",", "\n"));
                    }
                }

                /*
                 * At this point we found a matching method call we update the
                 * data structures.
                 */
                if (JimpleUtils.isConstructor(methodSignature)) {
                    /*
                     * Ownership info for constructors are available only by the
                     * end of the invocation... This is not 100% accurate but we
                     * try to live with that...
                     */
                    ObjectInstance owner = new ObjectInstance(methodOwner);
                    methodInvocation.setOwner(owner);
                    dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, owner);
                    // Still not sure WHY we need to register the data deps if
                    // we have that into the methodInvocation and data deps...
                    // executionFlowGraph.addOwnerToMethodInvocation(methodInvocation,
                    // owner);
                }
                /*
                 * Return type
                 */
                String returnType = methodSignature.trim().split(" ")[1];
                if (!JimpleUtils.isVoid(returnType)) {
                    DataNode returnValue = DataNodeFactory.get(returnType, returnValueAsString);
                    methodInvocation.setReturnValue(returnValue);
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
                Files.createDirectories(outputArtifactTo.getParentFile().toPath(), new FileAttribute[] {});
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
