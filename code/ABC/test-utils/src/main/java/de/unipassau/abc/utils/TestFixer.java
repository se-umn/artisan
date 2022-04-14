package de.unipassau.abc.utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestFixer {
    public static void main(String args[]){
        String appName = "FifthElement";
        String carvedTestsFolder = "/Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/FifthElement/app/src/allCarvedTest";
        fixTests(appName, carvedTestsFolder);
    }


    public static void fixTests(String appName, String carvedTestsFolder){
        //result
        if(appName.equals("FifthElement")){
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
                for(String testFileName:testFileNames){
                    System.out.println(testFileName);
                    CompilationUnit compilationUnit = StaticJavaParser.parse(new File(testFileName));
                    List<MethodDeclaration> methodDeclarations = compilationUnit.findAll(MethodDeclaration.class);
                    boolean addFileImports = false;
                    boolean addMainImports = false;
                    for(MethodDeclaration methodDeclaration:methodDeclarations) {
                        boolean isTest = false;
                        for (AnnotationExpr annotationExpr : methodDeclaration.getAnnotations()) {
                            if (annotationExpr.getName().asString().startsWith("Test")) {
                                isTest = true;
                                break;
                            }
                        }
                        if(isTest){
                            if(methodDeclaration.getBody().isPresent()){
                                NodeList<Statement> statements = methodDeclaration.getBody().get().getStatements();
                                for(Statement statement:statements){
                                    Flag replacedDBStringFlag = new Flag();
                                    statement.accept(new VoidVisitorAdapter<Flag>() {
                                        @Override
                                        public void visit(StringLiteralExpr stringLiteralExpr, Flag flag) {
                                            if(stringLiteralExpr.getValue().equals("/data/user/0/fifthelement.theelement/app_db/Element")) {
                                                flag.setValue(true);
                                                stringLiteralExpr.replace(StaticJavaParser.parseExpression("Main.getDBPathName()"));
                                                return;
                                            }
                                        }
                                    }, replacedDBStringFlag);
                                    if(replacedDBStringFlag.getValue()){
                                        addMainImports = true;
                                    }
                                }
                                if(statements.size()>0){
                                    Statement firstStatement = statements.get(0);
                                    boolean initDBAlreadyPresent = false;
                                    if(firstStatement instanceof ExpressionStmt){
                                        ExpressionStmt expressionStmt = (ExpressionStmt) firstStatement;
                                        if(expressionStmt.getExpression() instanceof VariableDeclarationExpr){
                                            VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr) expressionStmt.getExpression();
                                            if(variableDeclarationExpr.toString().contains("TestDatabaseUtil.copyDB")){
                                                initDBAlreadyPresent  = true;
                                            }
                                        }
                                    }
                                    if(!initDBAlreadyPresent){
                                        addFileImports = true;
                                        Statement initDBStatement = StaticJavaParser.parseStatement("File tempDB = TestDatabaseUtil.copyDB();");
                                        methodDeclaration.getBody().get().addStatement(0, initDBStatement);
                                    }
                                }
                            }
                        }
                    }
                    if(addMainImports){
                        compilationUnit.addImport("fifthelement.theelement.application.Main");
                    }
                    if(addFileImports){
                        compilationUnit.addImport("java.io.File");
                        compilationUnit.addImport("fifthelement.theelement.utils.TestDatabaseUtil");
                    }
                    if(addFileImports || addMainImports) {
                        //rewrite file
                        File modifiedTestFile = new File(testFileName);
                        FileWriter fooWriter = new FileWriter(modifiedTestFile, false);
                        fooWriter.write(compilationUnit.toString());
                        fooWriter.close();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
