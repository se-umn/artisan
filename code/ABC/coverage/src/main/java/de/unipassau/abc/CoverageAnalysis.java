package de.unipassau.abc;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import de.unipassau.abc.IWriter.TextWriter;
import org.jacoco.core.analysis.*;
import org.jacoco.core.tools.ExecFileLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class CoverageAnalysis {

//    private static Set<String> instructionAnalyzed = new HashSet<String>();
//    private static int instructionCount = 0;

    public Coverage execute(String classesDirectoryName, List<String> execFileFolderNames) {
        List<File> execFiles = new ArrayList<File>();
        List<String> workList = new ArrayList<String>();
        workList.addAll(execFileFolderNames);
        while(!workList.isEmpty()){
            String currentFileName = workList.remove(0);
            File currentFile = new File(currentFileName);
            if(currentFile.isDirectory()){
                File filesInCurrentDirectory[] = currentFile.listFiles();
                for(File fileInCurrentDirectory:filesInCurrentDirectory){
                    workList.add(fileInCurrentDirectory.getAbsolutePath());
                }
            }
            else{
                if(currentFile.getName().endsWith(".exec") || currentFile.getName().endsWith(".ec")){
                    execFiles.add(currentFile);
                }
            }
        }
        File classesDirectoryFile = new File(classesDirectoryName);
        Map<String, Map<Integer, Integer>> coverage = execFiles.stream().map(coverageFile -> {
            Map<String, Map<Integer, Integer>> data = null;
            try {
                data = dump(coverageFile, classesDirectoryFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.printf("An error occurred while analyzing %s", coverageFile);
                System.exit(1);
            }
            return data;
        }).reduce(CoverageAnalysis::mergeClasses).get();
        return new Coverage(coverage, execFiles.size());
    }

    private static Map<String, Map<Integer, Integer>> mergeClasses(final Map<String, Map<Integer, Integer>> a,
            final Map<String, Map<Integer, Integer>> b) {
        b.forEach((key, value) -> a.merge(key, value, CoverageAnalysis::mergeLines));
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

        Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        //PatchedAnalyzer analyzer = new PatchedAnalyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        //Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
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
                //if (analyzer.isLineNumberWhiteListed(cc.getName(), i)) {
                if(cc.getLine(i).getInstructionCounter().getCoveredCount()>0) {
                    classData.put(i, status(cc.getLine(i).getStatus()));
//                    if(!instructionAnalyzed.contains(canonicalClassName(cc.getName())+i) && !canonicalClassName(cc.getName()).endsWith(".R")){
//                        instructionCount = instructionCount + cc.getLine(i).getInstructionCounter().getCoveredCount();
//                        instructionAnalyzed.add(canonicalClassName(cc.getName())+i);
//                    }
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
        //carved data
        String packageName = "fifthelement.theelement";
        String carvedClassesDirectory = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/FifthElement/carved_coverage_data/build/intermediates/classes/debug";
        List<String> carvedExecFolders = new ArrayList<>();
        carvedExecFolders.add("/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/FifthElement/carved_coverage_data/build/jacoco");
        Coverage carvedCoverage = new CoverageAnalysis().execute(carvedClassesDirectory, carvedExecFolders);
        Map<String, Map<Integer, Integer>>  overallCarvedCoverageInfo = carvedCoverage.get();
        //espresso data
        String espressoClassesDirectory = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/FifthElement/espresso_coverage_data/build/intermediates/classes/debug";
        List<String> espressoExecFolders = new ArrayList<>();
        espressoExecFolders.add("/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/FifthElement/espresso_coverage_data/coverage_ec");
        Coverage espressoCoverage = new CoverageAnalysis().execute(espressoClassesDirectory, espressoExecFolders);
        Map<String, Map<Integer, Integer>>  overallEspressoCoverageInfo = espressoCoverage.get();

        int carvedCoveredLines = 0;
        for(String fileName:overallCarvedCoverageInfo.keySet()) {
            if (fileName.startsWith(packageName)) {
                if (fileName.equals(packageName + ".R") || fileName.equals(packageName + ".BuildConfig")) {
                    continue;
                }
                Map<Integer, Integer> carvedCoverageForFile = overallCarvedCoverageInfo.get(fileName);
                for (Integer line : carvedCoverageForFile.keySet()) {
                    Integer coverageCount = carvedCoverageForFile.get(line);
                    if (coverageCount.intValue() > 0) {
                        carvedCoveredLines++;
                    }
                }
            }
        }

        int carvedCoveredLinesNotInEspresso = 0;
        for(String fileName:overallCarvedCoverageInfo.keySet()){
            if(fileName.startsWith(packageName)){
                if(fileName.equals(packageName+".R") || fileName.equals(packageName+".BuildConfig")){
                    continue;
                }
                Map<Integer, Integer> carvedCoverageForFile = overallCarvedCoverageInfo.get(fileName);
                Map<Integer, Integer> espressoCoverageForFile = overallEspressoCoverageInfo.get(fileName);
                if(espressoCoverageForFile==null){
                    for(Integer line:carvedCoverageForFile.keySet()){
                        Integer coverageCount = carvedCoverageForFile.get(line);
                        if(coverageCount.intValue()>0) {
                            //System.out.println("Espresso did not cover:"+fileName+"#"+line);
                            carvedCoveredLinesNotInEspresso++;
                        }
                    }
                }
                for(Integer line:carvedCoverageForFile.keySet()){
                    Integer coverageCount = carvedCoverageForFile.get(line);
                    if(coverageCount.intValue()>0) {
                        if(espressoCoverageForFile.keySet().contains(line)){
                            Integer espressoCoverageCount = espressoCoverageForFile.get(line);
                            if(espressoCoverageCount.intValue()==0){
                                //System.out.println("Espresso did not cover:"+fileName+"#"+line);
                                carvedCoveredLinesNotInEspresso++;
                            }
                        }
                        else{
                            //System.out.println("Espresso did not cover:"+fileName+"#"+line);
                            carvedCoveredLinesNotInEspresso++;
                        }
                    }
                }
            }
        }

        int espressoCoveredLinesNotInCarved = 0;
        for(String fileName:overallEspressoCoverageInfo.keySet()){
            if(fileName.startsWith(packageName)){
                if(fileName.equals(packageName+".R") || fileName.equals(packageName+".BuildConfig")){
                    continue;
                }
                Map<Integer, Integer> espressoCoverageForFile = overallEspressoCoverageInfo.get(fileName);
                Map<Integer, Integer> carvedCoverageForFile = overallCarvedCoverageInfo.get(fileName);
                if(carvedCoverageForFile==null){
                    for(Integer line:espressoCoverageForFile.keySet()){
                        Integer coverageCount = espressoCoverageForFile.get(line);
                        if(coverageCount.intValue()>0) {
                            //System.out.println("Carved did not cover:"+fileName+"#"+line);
                            espressoCoveredLinesNotInCarved++;
                        }
                    }
                }
                for(Integer line:espressoCoverageForFile.keySet()){
                    Integer coverageCount = espressoCoverageForFile.get(line);
                    if(coverageCount.intValue()>0) {
                        if(carvedCoverageForFile.keySet().contains(line)){
                            Integer carvedCoverageCount = carvedCoverageForFile.get(line);
                            if(carvedCoverageCount.intValue()==0){
                                //System.out.println("Carved did not cover:"+fileName+"#"+line);
                                espressoCoveredLinesNotInCarved++;
                            }
                        }
                        else{
                            //System.out.println("Carved did not cover:"+fileName+"#"+line);
                            espressoCoveredLinesNotInCarved++;
                        }
                    }
                }
            }
        }

        System.out.println(carvedCoveredLines);
        System.out.println(carvedCoveredLinesNotInEspresso);
        System.out.println(espressoCoveredLinesNotInCarved);
    }
}
