package de.unipassau.abc;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import de.unipassau.abc.IWriter.TextWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.analysis.PatchedAnalyzer;
import org.jacoco.core.tools.ExecFileLoader;

public final class ExecDump {

    public interface CLI {
        @Option(longName = "execution-files", shortName = "e", description = "Jacoco coverage files list, e.g., coverage.ec")
        List<File> getExecutionFiles();

        @Option(longName = "output-file", shortName = "f", description = "Output file", defaultToNull = true)
        File getOutputFile();

        @Option(longName = "output-dir", shortName = "d", description = "Output directory", defaultToNull = true)
        File getOutputDir();

        @Option(longName = "classes-dir", shortName = "c", description = "Path to the compiled classes, e.g., build/intermediates/javac/debug/classes")
        File getClassesDir();

        @Option(longName = "print-flaky", shortName = "p", description = "Output flaky lines")
        boolean printFlakyness();

        @Option(longName = "no-uncovered", shortName = "n", description = "Ignore uncovered lines")
        boolean ignoreUncovered();
    }

    public Coverage execute(final CLI cli) {
        Map<String, Map<Integer, Integer>> coverage = cli.getExecutionFiles().stream().map(coverageFile -> {
            Map<String, Map<Integer, Integer>> data = null;
            try {
                data = dump(coverageFile, cli.getClassesDir());
            } catch (IOException e) {
                e.printStackTrace();
                System.err.printf("An error occurred while analyzing %s", coverageFile);
                System.exit(1);
            }
            return data;
        }).reduce(ExecDump::mergeClasses).get();
        return new Coverage(coverage, cli.getExecutionFiles().size());
    }

    private static Map<String, Map<Integer, Integer>> mergeClasses(final Map<String, Map<Integer, Integer>> a,
            final Map<String, Map<Integer, Integer>> b) {
        b.forEach((key, value) -> a.merge(key, value, ExecDump::mergeLines));
        return a;
    }

    private static Map<Integer, Integer> mergeLines(final Map<Integer, Integer> a, final Map<Integer, Integer> b) {
        b.forEach((key, value) -> a.merge(key, value, Integer::sum));
        return a;
    }

    private Map<String, Map<Integer, Integer>> dump(final File file, final File classesDir) throws IOException {
        if (!file.exists()) {
            throw new RuntimeException(String.format("Coverage file does not exist: %s", file));
        } else if (!classesDir.exists()) {
            throw new RuntimeException("Classes directory does not exist");
        }

        ExecFileLoader execFileLoader = new ExecFileLoader();
        execFileLoader.load(file);

        // This might be something to change as well. Not really it outputs the coverage
        // data computed by
        // whoever calls visitCoverage.
        CoverageBuilder coverageBuilder = new CoverageBuilder();

//        Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        PatchedAnalyzer analyzer = new PatchedAnalyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);

        analyzer.analyzeAll(classesDir);

        Map<String, Map<Integer, Integer>> data = new TreeMap<>();
        for (final IClassCoverage cc : coverageBuilder.getClasses()) {

            String canonicalClassName = canonicalClassName(cc.getName());
            Map<Integer, Integer> classData = new TreeMap<>();

//            System.out.println("Class: " + canonicalClassName);
            for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {

//                System.out.println(getStatusSymbol(cc.getLine(i).getStatus()) + String.format("%4d", i));

                // TODO THIS SHOULD NOT BE NECESSARY ANYMORE< BUT I NEED TO TEST IT SOMEHOW
                // Report only white listed lines
                if (analyzer.isLineNumberWhiteListed(cc.getName(), i)) {
                    classData.put(i, status(cc.getLine(i).getStatus()));
                }
            }

            if (data.containsKey(canonicalClassName)) {
                Map<Integer, Integer> providedData = data.get(canonicalClassName);
                classData.forEach((key, value) -> providedData.merge(key, value, Integer::max));
            } else {
                data.put(canonicalClassName, classData);
            }
        }

        return data;
    }

    private String getStatusSymbol(int status) {
        switch (status) {
        case ICounter.FULLY_COVERED:
            return "[*]";
        case ICounter.PARTLY_COVERED:
            return "[+]";
        case ICounter.NOT_COVERED:
            return "[ ]";
        case ICounter.EMPTY:
        default:
            return "   ";
        }
    }

    private String canonicalClassName(String className) {
        int cutPosition = className.indexOf('$');
        if (cutPosition == -1) {
            return className.replaceAll("/", ".");
        } else {
            return className.substring(0, cutPosition).replaceAll("/", ".");
        }
    }

    private int status(int status) {
        if (status == ICounter.FULLY_COVERED || status == ICounter.PARTLY_COVERED) {
            return 1;
        }
        return 0;
    }

    public static void main(final String[] args) throws IOException {
        CLI cli = CliFactory.parseArguments(CLI.class, args);
        if (cli.getOutputFile() == null && cli.getOutputDir() == null) {
            throw new RuntimeException("Either -f or -d must be provided");
        } else if (cli.getOutputFile() != null && cli.getOutputDir() != null) {
            throw new RuntimeException("Only one of both -f or -d must be provided");
        }

        Coverage coverage = new ExecDump().execute(cli);

        if (cli.getOutputFile() != null) {
            // Write to file
            TextWriter writer = new TextWriter(cli.getOutputFile());
            coverage.writeToFile(writer, cli.ignoreUncovered());
        } else if (cli.getOutputDir() != null) {
            // Write to dir
            TextWriter writer = new TextWriter(cli.getOutputDir());
            coverage.writeToDir(writer, cli.ignoreUncovered());
        }

        if (cli.printFlakyness()) {
            Map<String, Map<Integer, Integer>> flakyLines = coverage.getFlakyLines();

            if (!flakyLines.isEmpty()) {
                System.out.println("Flaky lines:");
            }

            flakyLines.forEach((className, lines) -> {
                System.out.printf(">> %s%n", className);
                lines.forEach((line, nTimesCovered) -> {
                    System.out.printf("\tLine %d covered only %d times out of %d%n", line, nTimesCovered,
                            cli.getExecutionFiles().size());
                });
            });
        }
    }
}
