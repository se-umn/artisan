package de.unipassau.abc.utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestLinesCounter {
    public static void main(String args[]){
        String carvedTestsFolder = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/A1/blabbertabber_main_data/app/src/allCarvedTest";
        countStatementsIntests(carvedTestsFolder);
    }


    private static void countStatementsIntests(String carvedTestsFolder){
            try{
                Set<String> testFileNames = new HashSet<String>();
                List<String> testFolderWorklist = new ArrayList<String>();
                testFolderWorklist.add(carvedTestsFolder);
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
                int testsCount = 0;
                for(String testFileName:testFileNames){
                    CompilationUnit compilationUnit = StaticJavaParser.parse(new File(testFileName));
                    List<MethodDeclaration> methodDeclarations = compilationUnit.findAll(MethodDeclaration.class);
                    for(MethodDeclaration methodDeclaration:methodDeclarations) {
                        boolean isTest = false;
                        for (AnnotationExpr annotationExpr : methodDeclaration.getAnnotations()) {
                            if (annotationExpr.getName().asString().startsWith("Test")) {
                                isTest = true;
                                break;
                            }
                        }
                        if (isTest) {
                            testsCount++;
                            if (methodDeclaration.getBody().isPresent()) {
                                System.out.println(methodDeclaration.getBody().get().getStatements().size());
                            }
                        }
                    }
                }
                System.out.println("TESTS COUNT:"+testsCount);
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }
}
