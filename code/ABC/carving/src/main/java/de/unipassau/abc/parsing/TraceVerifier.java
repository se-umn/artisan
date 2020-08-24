package de.unipassau.abc.parsing;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

public class TraceVerifier {
    
    private static File TRACE_FILE;
    
    private static File SEQUENCE_FILE;

    public interface ParserCLI {
        @Option(longName = "android-jar")
        File getAndroidJar();

        @Option(longName = "apk-file")
        File getApkFile();

        @Option(longName = "trace-file")
        File getTraceFile();

        @Option(longName = "sequence-file")
        File getSequenceFile();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ABCException {
        // Get command line arguments
		ParserCLI cli = CliFactory.parseArguments(ParserCLI.class, args);
        ParsingUtils.setupSoot(cli.getAndroidJar(), cli.getApkFile());
        TRACE_FILE = cli.getTraceFile();        
        SEQUENCE_FILE = cli.getSequenceFile();

        // Initialize trace parser
        TraceVerifier traceVerifier = new TraceVerifier();
        traceVerifier.verifyTraces(traceVerifier.parseTargetSequences());
    }

    /* Tests whether an expected sequence of method invocations
     * is present within the trace. This uses a flattened representation
     * of the callgraph to iterate through method sequences, using
     * invocation counts to ascertain temporal information (ie, to determine
     * the order of method invocations)
     */
    public void verifyTraces(List<List<String>> methodSequenceList) throws FileNotFoundException, IOException, ABCException {
        List<MethodInvocation> methodInvocationList = new TraceParserImpl()
            .parseTrace(TRACE_FILE)
            .getUIThreadParsedTrace()
            .getFirst()
            .getOrderedMethodInvocations();

        List<Boolean> verificationList = new ArrayList<Boolean>();

        /* Extract individual sequences from the list, and match functions against the list of method invocations
         * to determine the presence of desired sequences; the search here could be made stricter with the addition of
         * a bound, but is evidently accurate as-is, given sufficiently long sequences
         */
        seqloop:
        for (List<String> sequence : methodSequenceList) {
            int baseInvocationCount = 0;
            for (String func : sequence) {
                boolean found = false;
                for (MethodInvocation methodInvocation : methodInvocationList) {
                    if (methodInvocation.getMethodSignature().contains(func) &&
                            methodInvocation.getInvocationCount() > baseInvocationCount) {
                        baseInvocationCount = methodInvocation.getInvocationCount();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    verificationList.add(false);
                    continue seqloop;
                }
            }
            verificationList.add(true);
        }
        if (verificationList.contains(false)) {
            System.out.println("Trace verification failed!");
        }
        else {
            System.out.println("Trace verification passed!");
        }
    }

    // Extract each of the desired method sequences
    public List<List<String>> parseTargetSequences() {
        List<List<String>> sequenceList = new ArrayList<List<String>>();
        BufferedReader sequenceFile;
        try {

            sequenceFile = new BufferedReader(new FileReader(SEQUENCE_FILE));

            // We'll need to read the first line, to prevent 'func' from being null
            String func = sequenceFile.readLine();

            List<String> sequence = new ArrayList<String>();
            while(func != null) {

                // Each new occurrence of '@SEQUENCE' within the file begins a new sequence
                if (func.equals("@SEQUENCE")) {
                    sequenceList.add(sequence);
                    sequence = new ArrayList<String>();
                }
                else {
                    sequence.add(func);
                }
                func = sequenceFile.readLine();
            }
            sequenceList.add(sequence);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the first entry of the list, because it contains '@SEQUENCE'
        sequenceList.remove(0);

        return sequenceList;
    }
}
