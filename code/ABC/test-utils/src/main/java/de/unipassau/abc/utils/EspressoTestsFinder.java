package de.unipassau.abc.utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EspressoTestsFinder {
    public static void main(String args[]){

//        //https://github.com/foxycom/AnyMemo
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/AnyMemo/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/AnyMemo/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/AnyMemo/espresso_tests.txt";
//        //https://github.com/foxycom/bird-monitor
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/bird-monitor/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/bird-monitor/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/bird-monitor/espresso_tests.txt";
//        //https://github.com/foxycom/blabbertabber
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/blabbertabber/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/blabbertabber/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/blabbertabber/espresso_tests.txt";
//        //https://github.com/foxycom/FifthElement
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/FifthElement/TheElement/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/FifthElement/TheElement/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/FifthElement/TheElement/espresso_tests.txt";
//        //https://github.com/foxycom/moneywallet
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/moneywallet/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/moneywallet/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/moneywallet/espresso_tests.txt";
//        //https://github.com/foxycom/MySteamGames
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/MySteamGames/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/MySteamGames/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/MySteamGames/espresso_tests.txt";
//        //https://github.com/foxycom/nzsl-dictionary-android
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/nzsl-dictionary-android/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/nzsl-dictionary-android/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/nzsl-dictionary-android/espresso_tests.txt";
//        //https://github.com/foxycom/PrismaCallBlocker
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/PrismaCallBlocker/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/PrismaCallBlocker/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/PrismaCallBlocker/espresso_tests.txt";
//        //https://github.com/foxycom/probe-android
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/probe-android/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/probe-android/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/probe-android/espresso_tests.txt";
//        //https://github.com/foxycom/todoagenda
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/todoagenda/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/todoagenda/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/todoagenda/espresso_tests.txt";
//        //https://github.com/foxycom/toposuite-android
//        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/toposuite-android/app/src/androidTest";
//        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/toposuite-android/tests.txt";
//        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/toposuite-android/espresso_tests.txt";
        //https://github.com/foxycom/UK-Gebaerden_Muensterland
        String testFolderName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/UK-Gebaerden_Muensterland/app/src/androidTest";
        String testsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/UK-Gebaerden_Muensterland/tests.txt";
        String espressoTestsListFileName = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/benchmarks/UK-Gebaerden_Muensterland/espresso_tests.txt";



        printEspressoTests(testFolderName, testsListFileName, espressoTestsListFileName);
    }


    public static void printEspressoTests(String testFolderName, String testsListFileName, String espressoTestsListFileName){
        //result
        List<String> relevantTestMethodSignatures = new ArrayList<String>();
        try {
            //find test files
            Set<String> testFileNames = new HashSet<String>();
            Set<String> annotationNames = new HashSet<String>();
            List<String> testFolderWorklist = new ArrayList<String>();
            testFolderWorklist.add(testFolderName);
            while(!testFolderWorklist.isEmpty()){
                String fileName = testFolderWorklist.remove(0);
                File file = new File(fileName);
                if(file.isDirectory()){
                    for(File fileInDirectory:file.listFiles()){
                        testFolderWorklist.add(fileInDirectory.getAbsolutePath());
                    }
                }
                else{
                    if(file.getAbsolutePath().endsWith(".java")){
                        testFileNames.add(file.getAbsolutePath());
                    }
                }
            }
            //read tests list
            Set<String> testsListMethodSignatures = new HashSet<String>();
            FileInputStream fis = new FileInputStream(testsListFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String testsListLine;
            while ((testsListLine = br.readLine()) != null)   {
                testsListMethodSignatures.add(testsListLine);
            }
            fis.close();

            //iterate over test files
            for(String testFileName:testFileNames) {
                System.out.println(testFileName);
                CompilationUnit cu = StaticJavaParser.parse(new File(testFileName));
                //find whether file uses espresso
                List<ImportDeclaration> importDeclarations = cu.findAll(ImportDeclaration.class);
                boolean usesEspresso = false;
                for (ImportDeclaration importDeclaration : importDeclarations) {
                    if(importDeclaration.getName().asString().toLowerCase().contains("espresso")){
                        System.out.println(importDeclaration.getName().asString());
                    }
                    if(importDeclaration.getName().asString().startsWith("androidx.test.espresso") ||
                            importDeclaration.getName().asString().startsWith("android.support.test.espresso")){
                        usesEspresso = true;
                        break;
                    }
                }
                if(usesEspresso){
                    //find package name
                    String packageName = "";
                    List<PackageDeclaration> packageDeclarations = cu.findAll(PackageDeclaration.class);
                    if(packageDeclarations.size()!=1){
                        System.out.println("ERROR:cannot get package");
                        System.exit(1);
                    }
                    else{
                        packageName = packageDeclarations.get(0).getName().asString();
                    }
                    //find all methods that have @Test annotation
                    Set<String> testMethodSignatures = new HashSet<String>();
                    List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
                    for(MethodDeclaration methodDeclaration:methodDeclarations){
                        NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
                        String testMethodSignature = "";
                        boolean ignoreTest = false;
                        for(AnnotationExpr annotation:annotations){
                            System.out.println(annotation.getName().asString());
                            annotationNames.add(annotation.getName().asString());
                            if(annotation.getName().asString().equals("Test") || annotation.getName().asString().equals("LargeTest")){
                                String className = getNameOfDeclaringClassNode(methodDeclaration);
                                if(className.equals("")){
                                    System.out.println("ERROR:cannot get class name "+testFileName+"#"+methodDeclaration.getName().asString());
                                    System.exit(1);
                                }
                                testMethodSignature = packageName+"."+className+" "+methodDeclaration.getName().asString();
                            }
                            else if (annotation.getName().asString().equals("Ignore")){
                                ignoreTest = true;
                            }
                        }
                        if(!testMethodSignature.equals("") && !ignoreTest) {
                            if (!testsListMethodSignatures.contains(testMethodSignature)) {
                                System.out.println("ERROR:test not in original list " + testMethodSignature);
                                System.exit(1);
                            }
                            testMethodSignatures.add(testMethodSignature);
                        }
                    }
                    for(String testMethodSignature:testMethodSignatures){
                        relevantTestMethodSignatures.add(testMethodSignature);
                    }
                }
            }
            //print results
            System.out.println("################################################");
            for(String annotationName:annotationNames) {
                System.out.println(annotationName);
            }
            System.out.println("################################################");
            System.out.println(relevantTestMethodSignatures.size());
            System.out.println("################################################");
            FileWriter outputWriter = new FileWriter(espressoTestsListFileName);
            for(String relevantTestMethodSignature:relevantTestMethodSignatures){
                System.out.println(relevantTestMethodSignature);
                outputWriter.write(relevantTestMethodSignature+System.lineSeparator());
            }
            outputWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getNameOfDeclaringClassNode(Node node) {
        String result = "";
        List<Node> workList = new ArrayList<Node>();
        if (node.getParentNode().isPresent()) {
            Node parentNode = node.getParentNode().get();
            if (parentNode instanceof ClassOrInterfaceDeclaration) {
                result = ((ClassOrInterfaceDeclaration) parentNode).getNameAsString();
                return result;
            }
            workList.add(parentNode);
        }
        while (!workList.isEmpty()) {
            Node currNode = workList.remove(0);
            if (currNode.getParentNode().isPresent()) {
                Node parentNode = currNode.getParentNode().get();
                if (parentNode instanceof ClassOrInterfaceDeclaration) {
                    result = ((ClassOrInterfaceDeclaration) parentNode).getNameAsString();
                    return result;
                }
                workList.add(parentNode);
            }
        }
        return result;
    }
}
