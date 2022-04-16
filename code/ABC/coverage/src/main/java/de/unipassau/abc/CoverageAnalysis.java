package de.unipassau.abc;

import org.jacoco.core.analysis.*;
import org.jacoco.core.tools.ExecFileLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class CoverageAnalysis {

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
        Map<String, Map<Integer, LineInfo>> coverage = new HashMap<String, Map<Integer, LineInfo>>();
        for(File execFile:execFiles){
            try {
                Map<String, Map<Integer, LineInfo>> coverageFromFile = dump(execFile, classesDirectoryFile);
                coverage = merge(coverage, coverageFromFile);
            }
            catch(IOException e){
                e.printStackTrace();
                System.err.printf("An error occurred while analyzing %s", execFile.getAbsolutePath());
                System.exit(1);
            }
        }
        return new Coverage(coverage, execFiles.size());
    }

    private static Map<String, Map<Integer, LineInfo>> merge(final Map<String, Map<Integer, LineInfo>> currentCoverageData,
            final Map<String, Map<Integer, LineInfo>> newCoverageData) {
        for(String className:newCoverageData.keySet()){
            if(currentCoverageData.keySet().contains(className)){
                //merge class data
                Map<Integer, LineInfo> newClassData = newCoverageData.get(className);
                Map<Integer, LineInfo> currentClassData = currentCoverageData.get(className);
                for(Integer line:newClassData.keySet()){
                    if(currentClassData.keySet().contains(line)){
                        //taking the max of covered instructions
                        LineInfo newClassDataLineInfo = newClassData.get(line);
                        LineInfo currentClassDataLineInfo = currentClassData.get(line);
                        LineInfo newLineInfo = new LineInfo(Math.max(newClassDataLineInfo.getCoverageType(), currentClassDataLineInfo.getCoverageType()),
                                Math.max(newClassDataLineInfo.getCoveredInstructionsCount(), currentClassDataLineInfo.getCoveredInstructionsCount()),
                                Math.max(newClassDataLineInfo.getCoveredBranchCount(), currentClassDataLineInfo.getCoveredBranchCount()));
                        currentClassData.put(line, newLineInfo);
                    }
                    else{
                        currentClassData.put(line, newClassData.get(line));
                    }
                }
            }
            else{
                currentCoverageData.put(className, newCoverageData.get(className));
            }
        }
        return currentCoverageData;
    }

    private Map<String, Map<Integer, LineInfo>> dump(final File file, final File classesDir) throws IOException {
        if (!file.exists()) {
            throw new RuntimeException(String.format("Coverage file does not exist: %s", file));
        } else if (!classesDir.exists()) {
            throw new RuntimeException("Classes directory does not exist");
        }

        ExecFileLoader execFileLoader = new ExecFileLoader();
        execFileLoader.load(file);

        CoverageBuilder coverageBuilder = new CoverageBuilder();

        Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        analyzer.analyzeAll(classesDir);

        Map<String, Map<Integer, LineInfo>> data = new TreeMap<>();
        for (final IClassCoverage cc : coverageBuilder.getClasses()) {
            String canonicalClassName = canonicalClassName(cc.getName());
            Map<Integer, LineInfo> classData = new TreeMap<>();
            for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
                if(cc.getLine(i).getInstructionCounter().getCoveredCount()>0 || cc.getLine(i).getInstructionCounter().getCoveredCount()>0) {
                    classData.put(i, new LineInfo(status(cc.getLine(i).getStatus()), cc.getLine(i).getInstructionCounter().getCoveredCount(), cc.getLine(i).getBranchCounter().getCoveredCount()));
                }
            }
            if (data.containsKey(canonicalClassName)) {
                Map<Integer, LineInfo> alreadyComputedData = data.get(canonicalClassName);
                for(Integer line:classData.keySet()){
                    if(alreadyComputedData.keySet().contains(line)){
                        //taking the max of covered instructions
                        LineInfo classDataLineInfo = classData.get(line);
                        LineInfo alreadyComputedDataLineInfo = alreadyComputedData.get(line);
                        LineInfo newLineInfo = new LineInfo(Math.max(classDataLineInfo.getCoverageType(), alreadyComputedDataLineInfo.getCoverageType()),
                                Math.max(classDataLineInfo.getCoveredInstructionsCount(), alreadyComputedDataLineInfo.getCoveredInstructionsCount()),
                                Math.max(classDataLineInfo.getCoveredBranchCount(), alreadyComputedDataLineInfo.getCoveredBranchCount()));
                        alreadyComputedData.put(line, newLineInfo);
                    }
                    else{
                        alreadyComputedData.put(line, classData.get(line));
                    }
                }
            } else {
                data.put(canonicalClassName, classData);
            }
        }

        return data;
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

    public static void printCoverageCount(String coverageType, Map<String, Map<Integer, LineInfo>> coverageInfo){
        int coveredInstructionCount = 0;
        int coveredBranchCount = 0;
        for(String className:coverageInfo.keySet()){
            if(className.endsWith(".R")){
                continue;
            }
            //System.out.println(className);
            Map<Integer, LineInfo> classCoverageInfo = coverageInfo.get(className);
            for(Integer line:classCoverageInfo.keySet()){
                LineInfo lineInfo = classCoverageInfo.get(line);
                if(lineInfo.getCoverageType()>0){
                    coveredInstructionCount = coveredInstructionCount + lineInfo.getCoveredInstructionsCount();
                    coveredBranchCount = coveredBranchCount + lineInfo.getCoveredBranchCount();
                }
            }
        }
        System.out.println(coverageType+" Instruction Count:"+coveredInstructionCount);
        System.out.println(coverageType+" Branch Count:"+coveredBranchCount);
    }

    public static int findSharedCoverage(Map<String, Map<Integer, LineInfo>> coverageA, Map<String, Map<Integer, LineInfo>> coverageB, String type){
        int sharedCoverageCount = 0;
        for(String className:coverageA.keySet()){
            if(className.endsWith(".R")){
                continue;
            }
            if(coverageB.keySet().contains(className)){
                Map<Integer, LineInfo> classCoverageA = coverageA.get(className);
                Map<Integer, LineInfo> classCoverageB = coverageB.get(className);
                for(Integer line:classCoverageA.keySet()){
                    if(classCoverageB.keySet().contains(line)){
                        LineInfo lineInfoA = classCoverageA.get(line);
                        LineInfo lineInfoB = classCoverageB.get(line);
                        if(type.equals("instruction")){
                            if(lineInfoA.getCoverageType()>0 && lineInfoB.getCoverageType()>0 && lineInfoA.getCoveredInstructionsCount()>0 && lineInfoB.getCoveredInstructionsCount()>0){
                                sharedCoverageCount = sharedCoverageCount + lineInfoA.getCoveredInstructionsCount();
                            }
                        }
                        else if (type.equals("branch")){
                            if(lineInfoA.getCoverageType()>0 && lineInfoB.getCoverageType()>0 && lineInfoA.getCoveredBranchCount()>0 && lineInfoB.getCoveredBranchCount()>0){
                                sharedCoverageCount = sharedCoverageCount + lineInfoA.getCoveredBranchCount();
                            }
                        }
                    }
                }
            }
        }
        return sharedCoverageCount;
    }

    public static void main(final String[] args) throws IOException {
        //params
        String packageName = "";
        String carvedClassesDirectory = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/A1/blabbertabber_main_data/carved_coverage/build/intermediates/javac/debug/classes";
        List<String> carvedExecFolders = new ArrayList<>();
        carvedExecFolders.add("/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/A1/blabbertabber_main_data/carved_coverage/build/jacoco");
        String guiClassesDirectory = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/A1/blabbertabber_main_data/gui_coverage/build/intermediates/javac/debug/classes";
        List<String> guiExecFolders = new ArrayList<>();
        guiExecFolders.add("/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/A1/blabbertabber_main_data/gui_coverage/espresso_tests");
        ////////////////////////

        //carved coverage
        Coverage carvedCoverage = new CoverageAnalysis().execute(carvedClassesDirectory, carvedExecFolders);
        Map<String, Map<Integer, LineInfo>>  carvedCoverageInfo = carvedCoverage.get();
        //GUI coverage
        Coverage guiCoverage = new CoverageAnalysis().execute(guiClassesDirectory, guiExecFolders);
        Map<String, Map<Integer, LineInfo>>  guiCoverageInfo = guiCoverage.get();


        printCoverageCount("Carved Coverage", carvedCoverageInfo);
        printCoverageCount("GUI Coverage", guiCoverageInfo);
        int sharedInstructionCoverageCount = findSharedCoverage(carvedCoverageInfo, guiCoverageInfo, "instruction");
        System.out.println("Shared Instruction Coverage Count:"+sharedInstructionCoverageCount);
        int sharedBranchCoverageCount = findSharedCoverage(carvedCoverageInfo, guiCoverageInfo, "branch");
        System.out.println("Shared Branch Coverage Count:"+sharedBranchCoverageCount);



//        int carvedCoveredLines = 0;
//        for(String fileName:overallCarvedCoverageInfo.keySet()) {
//            if (fileName.startsWith(packageName)) {
//                if (fileName.equals(packageName + ".R") || fileName.equals(packageName + ".BuildConfig")) {
//                    continue;
//                }
//                Map<Integer, Integer> carvedCoverageForFile = overallCarvedCoverageInfo.get(fileName);
//                for (Integer line : carvedCoverageForFile.keySet()) {
//                    Integer coverageCount = carvedCoverageForFile.get(line);
//                    if (coverageCount.intValue() > 0) {
//                        carvedCoveredLines++;
//                    }
//                }
//            }
//        }
//
//        int carvedCoveredLinesNotInEspresso = 0;
//        for(String fileName:overallCarvedCoverageInfo.keySet()){
//            if(fileName.startsWith(packageName)){
//                if(fileName.equals(packageName+".R") || fileName.equals(packageName+".BuildConfig")){
//                    continue;
//                }
//                Map<Integer, Integer> carvedCoverageForFile = overallCarvedCoverageInfo.get(fileName);
//                Map<Integer, Integer> espressoCoverageForFile = overallEspressoCoverageInfo.get(fileName);
//                if(espressoCoverageForFile==null){
//                    for(Integer line:carvedCoverageForFile.keySet()){
//                        Integer coverageCount = carvedCoverageForFile.get(line);
//                        if(coverageCount.intValue()>0) {
//                            //System.out.println("Espresso did not cover:"+fileName+"#"+line);
//                            carvedCoveredLinesNotInEspresso++;
//                        }
//                    }
//                }
//                for(Integer line:carvedCoverageForFile.keySet()){
//                    Integer coverageCount = carvedCoverageForFile.get(line);
//                    if(coverageCount.intValue()>0) {
//                        if(espressoCoverageForFile.keySet().contains(line)){
//                            Integer espressoCoverageCount = espressoCoverageForFile.get(line);
//                            if(espressoCoverageCount.intValue()==0){
//                                //System.out.println("Espresso did not cover:"+fileName+"#"+line);
//                                carvedCoveredLinesNotInEspresso++;
//                            }
//                        }
//                        else{
//                            //System.out.println("Espresso did not cover:"+fileName+"#"+line);
//                            carvedCoveredLinesNotInEspresso++;
//                        }
//                    }
//                }
//            }
//        }
//
//        int espressoCoveredLinesNotInCarved = 0;
//        for(String fileName:overallEspressoCoverageInfo.keySet()){
//            if(fileName.startsWith(packageName)){
//                if(fileName.equals(packageName+".R") || fileName.equals(packageName+".BuildConfig")){
//                    continue;
//                }
//                Map<Integer, Integer> espressoCoverageForFile = overallEspressoCoverageInfo.get(fileName);
//                Map<Integer, Integer> carvedCoverageForFile = overallCarvedCoverageInfo.get(fileName);
//                if(carvedCoverageForFile==null){
//                    for(Integer line:espressoCoverageForFile.keySet()){
//                        Integer coverageCount = espressoCoverageForFile.get(line);
//                        if(coverageCount.intValue()>0) {
//                            //System.out.println("Carved did not cover:"+fileName+"#"+line);
//                            espressoCoveredLinesNotInCarved++;
//                        }
//                    }
//                }
//                for(Integer line:espressoCoverageForFile.keySet()){
//                    Integer coverageCount = espressoCoverageForFile.get(line);
//                    if(coverageCount.intValue()>0) {
//                        if(carvedCoverageForFile.keySet().contains(line)){
//                            Integer carvedCoverageCount = carvedCoverageForFile.get(line);
//                            if(carvedCoverageCount.intValue()==0){
//                                //System.out.println("Carved did not cover:"+fileName+"#"+line);
//                                espressoCoveredLinesNotInCarved++;
//                            }
//                        }
//                        else{
//                            //System.out.println("Carved did not cover:"+fileName+"#"+line);
//                            espressoCoveredLinesNotInCarved++;
//                        }
//                    }
//                }
//            }
//        }
//
//        System.out.println(carvedCoveredLines);
//        System.out.println(carvedCoveredLinesNotInEspresso);
//        System.out.println(espressoCoveredLinesNotInCarved);
    }
}
