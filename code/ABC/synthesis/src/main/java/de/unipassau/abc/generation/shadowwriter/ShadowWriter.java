package de.unipassau.abc.generation.shadowwriter;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.mocks.CarvingShadow;
import de.unipassau.abc.generation.utils.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class ShadowWriter {

    private static final Logger logger = LoggerFactory.getLogger(ShadowWriter.class);

    public static final String SHADOWS_PACKAGE = "abc.shadows";

    public void generateAndWriteShadows(List<TestClass> testClasses, File sourceFolder) {
        // generate shadows needed for test cases
        Map<String, String> shadowToType = new HashMap<String, String>();
        Map<String, Set<String>> shadowToMethods = new HashMap<String, Set<String>>();
        // each test case is a set of carved tests
        // no, this is actually a whole carved test?
        for (TestClass testCase : testClasses) {
            logger.info("Generate Shadows for TEST CLASS NAME: " + testCase.getName());
            // iterate over the carved tests in each test case
            for (CarvedTest carvedTest : testCase.getCarvedTests()) {
                logger.info("CARVED TEST FOR TEST CLASS: " + carvedTest.getUniqueIdentifier());
                // iterate over the shadows in each carved test
                for (CarvingShadow carvingShadow : carvedTest.getShadows()) {
                    if (shadowToType.containsKey(carvingShadow.getShadowName())) {
                        // this should not occur
                        if (!shadowToType.get(carvingShadow.getShadowName()).equals(carvingShadow.getStubbedType())) {
                            logger.error("Shadow types do not match: " + shadowToType.get(carvingShadow.getShadowName())
                                    + "/" + carvingShadow.getStubbedType());
                            throw new RuntimeException(
                                    "Shadow types do not match: " + shadowToType.get(carvingShadow.getShadowName())
                                            + "/" + carvingShadow.getStubbedType());
                        }
                        // if the shadow was not already in the map, insert it
                        // there and associate it with the correct stubbed type
                    } else {
                        shadowToType.put(carvingShadow.getShadowName(), carvingShadow.getStubbedType());
                    }

                    shadowToMethods.put(carvingShadow.getShadowName(), new HashSet<String>());

                    for (String stubbedMethod : carvingShadow.getStubbedMethods()) {
                        logger.info("Shadow " + carvingShadow.getShadowName() + " has stubbed methods!");
                        Set<String> methods = shadowToMethods.get(carvingShadow.getShadowName());
                        methods.add(stubbedMethod);
                    }
                }
            }
        }
        // generating shadow code
        for (String shadowName : shadowToType.keySet()) {
        
            String shadowID = shadowName.split("Shadow")[1];
            logger.info("Shadow:" + shadowName + " with ID " + shadowID );
            
            CompilationUnit cu = new CompilationUnit();
            cu.setPackageDeclaration(ShadowWriter.SHADOWS_PACKAGE);
            ClassOrInterfaceDeclaration shadowClass = cu.addClass(shadowName);
            shadowClass.setModifiers(Modifier.Keyword.PUBLIC);
            // this is the class the shadow shadows
            ClassOrInterfaceType shadowedType = new ClassOrInterfaceType();
            // we can convert a shadow name to a real class name via the
            // shadowToType map
            shadowedType.setName(shadowToType.get(shadowName));
            logger.info("Target super class:" + getSuperClass(shadowToType.get(shadowName)));
            logger.info("Target super class attempt two:" + getSuperClass(shadowedType.getNameAsString()));
            String superType = getSuperClass(shadowedType.getNameAsString());

            String possiblyOffensiveSuperType = superType.replaceAll("^org\\.robolectric\\.shadows\\.Shadow", "");

            logger.info("possiblyOffensiveSuperType: " + possiblyOffensiveSuperType);

            Set<Map.Entry<String, String>> traversableShadowMappings = shadowToType.entrySet();

            logger.info("traversableShadowMappings: " + traversableShadowMappings);

            for (Map.Entry<String, String> shadowAndClass : traversableShadowMappings) {
                if (shadowAndClass.getValue().contains(possiblyOffensiveSuperType) && !shadowedType.getNameAsString().contains(possiblyOffensiveSuperType)) {
                    // Here we should map the shadows by ID or name?
                    String superShadowID = shadowAndClass.getKey().split("Shadow")[1];
                    
                    if( shadowID.equals( superShadowID ) ) {
                        superType = shadowAndClass.getKey();
                    }
                }
            }

            logger.info("superType: " + superType);
            
            // set super type if any
            if (!superType.equals("")) {
                ClassOrInterfaceType superTypeClass = new ClassOrInterfaceType();
                superTypeClass.setName(superType);
                NodeList<ClassOrInterfaceType> superTypeClasses = new NodeList<ClassOrInterfaceType>();
                superTypeClasses.add(superTypeClass);
                // we set the class to extend (and currently break the inheritance hierarchy)
                // here
                shadowClass.setExtendedTypes(superTypeClasses);
            }
            // add annotation for class
            NormalAnnotationExpr implementsAnnotation = new NormalAnnotationExpr(
                    new Name("org.robolectric.annotation.Implements"),
                    NodeList.nodeList(new MemberValuePair("value", new ClassExpr(shadowedType))));
            shadowClass.addAnnotation(implementsAnnotation);
            // create field for real object
            FieldDeclaration realObjectField = shadowClass.addField(shadowedType, "realObject",
                    Modifier.Keyword.PRIVATE);
            realObjectField.addAnnotation("org.robolectric.annotation.RealObject");
            // create field for mocked calls
            ClassOrInterfaceType mockedCallsType = new ClassOrInterfaceType();
            mockedCallsType.setName("java.util.Map<String," + shadowedType.getNameAsString() + ">");
            ClassOrInterfaceType newMockedCallsType = new ClassOrInterfaceType();
            newMockedCallsType.setName("java.util.HashMap<String," + shadowedType.getNameAsString() + ">");
            ObjectCreationExpr newHasMap = new ObjectCreationExpr();
            newHasMap.setType(newMockedCallsType);
            FieldDeclaration mockedCallsField = shadowClass.addFieldWithInitializer(mockedCallsType, "mockedCalls",
                    newHasMap, Modifier.Keyword.PRIVATE);
            // create field for strict shadow
            FieldDeclaration strictShadowField = shadowClass.addField("boolean", "strictShadow",
                    Modifier.Keyword.PRIVATE);
            strictShadowField.getVariable(0).setInitializer(new BooleanLiteralExpr(false));
            // create set mock method
            MethodDeclaration setMockForMethod = shadowClass.addMethod("setMockFor", Modifier.Keyword.PUBLIC);
            ClassOrInterfaceType methodNameType = new ClassOrInterfaceType();
            methodNameType.setName("java.lang.String");
            setMockForMethod.addParameter(methodNameType, "methodName");
            setMockForMethod.addParameter(shadowedType, "mockedInstance");
            BlockStmt setMockForMethodBody = new BlockStmt();
            NodeList<Statement> stmtsInSetMockForMethodBody = new NodeList<Statement>();
            NodeList<Expression> putArguments = new NodeList<Expression>();
            for (Parameter setMockForMethodParameter : setMockForMethod.getParameters()) {
                putArguments.add(setMockForMethodParameter.getNameAsExpression());
            }
            MethodCallExpr putCallExpr = new MethodCallExpr(mockedCallsField.getVariable(0).getNameAsExpression(),
                    "put", putArguments);
            ExpressionStmt putCallExprStmt = new ExpressionStmt(putCallExpr);
            stmtsInSetMockForMethodBody.add(putCallExprStmt);
            setMockForMethodBody.setStatements(stmtsInSetMockForMethodBody);
            setMockForMethod.setBody(setMockForMethodBody);
            // create set strict method
            MethodDeclaration setStrictMethod = shadowClass.addMethod("setStrictShadow", Modifier.Keyword.PUBLIC);
            BlockStmt setStrictMethodBody = new BlockStmt();
            NodeList<Statement> stmtsInSetStrictMethodBody = new NodeList<Statement>();
            AssignExpr strictMethodAssignExpr = new AssignExpr(strictShadowField.getVariable(0).getNameAsExpression(),
                    new BooleanLiteralExpr(true), AssignExpr.Operator.ASSIGN);
            ExpressionStmt strictMethodAssignStmt = new ExpressionStmt(strictMethodAssignExpr);
            stmtsInSetStrictMethodBody.add(strictMethodAssignStmt);
            setStrictMethodBody.setStatements(stmtsInSetStrictMethodBody);
            setStrictMethod.setBody(setStrictMethodBody);
            // create a method for each mocked method
            Set<String> methodsForShadow = shadowToMethods.get(shadowName);

            if (methodsForShadow == null) {
                logger.info("Shadow name: " + shadowName);
                logger.info("" + shadowToMethods.entrySet());
                logger.info("" + shadowToType.entrySet());
                logger.info("this list is null! something is likely wrong");
            }

            for (String methodSignature : methodsForShadow) {
                String methodName = JimpleUtils.getMethodName(methodSignature);
                String returnType = JimpleUtils.getReturnType(methodSignature);
                String parameters[] = JimpleUtils.getParameterList(methodSignature);
                List<String> parameterTypeList = new ArrayList<String>();
                List<String> parameterNameList = new ArrayList<String>();
                for (String parameter : parameters) {
                    String parameterType = parameter.replace("$", ".");
                    parameterTypeList.add(parameterType);
                    String parameterName = parameter.substring(parameter.lastIndexOf(".") + 1).replace("$", "");
                    int parameterIndex = parameterNameList.size() + 1;
                    parameterName = parameterName.substring(0, 1).toLowerCase() + parameterName.substring(1)
                            + parameterIndex;
                    parameterNameList.add(parameterName);
                }
                MethodDeclaration methodDeclaration = shadowClass.addMethod(methodName, Modifier.Keyword.PROTECTED);
                methodDeclaration.addAnnotation("org.robolectric.annotation.Implementation");
                methodDeclaration.setType(returnType);
                for (int i = 0; i < parameterTypeList.size(); ++i) {
                    String parameterT = parameterTypeList.get(i);
                    String parameterN = parameterNameList.get(i);
                    ClassOrInterfaceType parameterType = new ClassOrInterfaceType();
                    parameterType.setName(parameterT);
                    methodDeclaration.addParameter(parameterType, parameterN);
                }
                BlockStmt methodBody = new BlockStmt();
                NodeList<Statement> stmtsInMethodBody = new NodeList<Statement>();
                IfStmt ifStmt = new IfStmt();
                NodeList<Expression> containsKeyArguments = new NodeList<Expression>();
                containsKeyArguments.add(new StringLiteralExpr(methodSignature));
                ifStmt.setCondition(new MethodCallExpr(mockedCallsField.getVariable(0).getNameAsExpression(),
                        "containsKey", containsKeyArguments));
                NodeList<Expression> getArguments = new NodeList<Expression>();
                getArguments.add(new StringLiteralExpr(methodSignature));
                MethodCallExpr getExpr = new MethodCallExpr(mockedCallsField.getVariable(0).getNameAsExpression(),
                        "get", getArguments);
                NodeList<Expression> methodCallExprArguments = new NodeList<Expression>();
                for (int i = 0; i < parameterTypeList.size(); ++i) {
                    String parameterN = parameterNameList.get(i);
                    methodCallExprArguments.add(new NameExpr(parameterN));
                }
                MethodCallExpr methodCallExpr = new MethodCallExpr(getExpr, methodName, methodCallExprArguments);
                if (methodDeclaration.getType().isVoidType()) {
                    ExpressionStmt expressionStmt = new ExpressionStmt(methodCallExpr);
                    BlockStmt expressionBlockStmt = new BlockStmt();
                    expressionBlockStmt.addStatement(expressionStmt);
                    ifStmt.setThenStmt(expressionBlockStmt);
                } else {
                    ReturnStmt returnStmt = new ReturnStmt(methodCallExpr);
                    BlockStmt returnBlockStmt = new BlockStmt();
                    returnBlockStmt.addStatement(returnStmt);
                    ifStmt.setThenStmt(returnBlockStmt);
                }
                IfStmt strictIf = new IfStmt();
                strictIf.setCondition(strictShadowField.getVariable(0).getNameAsExpression());
                ObjectCreationExpr newRuntimeException = new ObjectCreationExpr();
                ClassOrInterfaceType runtimeExceptionType = new ClassOrInterfaceType();
                runtimeExceptionType.setName("java.lang.RuntimeException");
                newRuntimeException.setType(runtimeExceptionType);
                newRuntimeException.addArgument(new StringLiteralExpr("Should not have executed this method."));
                ThrowStmt throwStmt = new ThrowStmt(newRuntimeException);
                BlockStmt throwBlockStmt = new BlockStmt();
                throwBlockStmt.addStatement(throwStmt);
                strictIf.setThenStmt(throwBlockStmt);

                NodeList<Expression> directlyOnArgs = new NodeList<Expression>();
                directlyOnArgs.add(realObjectField.getVariable(0).getNameAsExpression());

                // Ensure that when we call directlyOn we cast the first argument to the class
                // that indeed DEFINE the method
                // You can check the method signature: for example, <android.view.View: void
                // setOnClickListener(android.view.View$OnClickListener)>
                // Means that we need specify as second parameter of directlyOn the following
                // class: android.view.View.class

                // Extract the class name from the methodSignature
                String shadowedTypeDeclaringTheMethodName = JimpleUtils.getClassNameForMethod(methodSignature);
                // Define the AST element to host the class reference
                ClassOrInterfaceType shadowedTypeDeclaringTheMethod = new ClassOrInterfaceType();
                shadowedTypeDeclaringTheMethod.setName(shadowedTypeDeclaringTheMethodName);
                // Use it as second argument of directlyOn
                directlyOnArgs.add(new ClassExpr(shadowedTypeDeclaringTheMethod));

                MethodCallExpr directlyOnExpr = new MethodCallExpr(new NameExpr("org.robolectric.shadow.api.Shadow"),
                        "directlyOn", directlyOnArgs);
                NodeList<Expression> methodCallOfDirectlyOnExprArguments = new NodeList<Expression>();
                for (int i = 0; i < parameterTypeList.size(); ++i) {
                    String parameterN = parameterNameList.get(i);
                    methodCallOfDirectlyOnExprArguments.add(new NameExpr(parameterN));
                }
                MethodCallExpr methodCallOfDirectlyOnExpr = new MethodCallExpr(directlyOnExpr, methodName,
                        methodCallOfDirectlyOnExprArguments);
                if (methodDeclaration.getType().isVoidType()) {
                    ExpressionStmt expressionStmt = new ExpressionStmt(methodCallOfDirectlyOnExpr);
                    BlockStmt expressionBlockStmt = new BlockStmt();
                    expressionBlockStmt.addStatement(expressionStmt);
                    strictIf.setElseStmt(expressionBlockStmt);
                } else {
                    ReturnStmt returnStmt = new ReturnStmt(methodCallOfDirectlyOnExpr);
                    BlockStmt returnBlockStmt = new BlockStmt();
                    returnBlockStmt.addStatement(returnStmt);
                    strictIf.setElseStmt(returnBlockStmt);
                }
                BlockStmt strictIfBlockStmt = new BlockStmt();
                strictIfBlockStmt.addStatement(strictIf);
                ifStmt.setElseStmt(strictIfBlockStmt);
                stmtsInMethodBody.add(ifStmt);
                methodBody.setStatements(stmtsInMethodBody);
                methodDeclaration.setBody(methodBody);
            }
            // write to file
            File shadowFileFolder = new File(sourceFolder,
                    ShadowWriter.SHADOWS_PACKAGE.replaceAll("\\.", File.separator));
            shadowFileFolder.mkdirs();
            // we will need to modify the shadow name
            File testFile = new File(shadowFileFolder, shadowName + ".java");
            try {
                PrintStream out = new PrintStream(new FileOutputStream(testFile));
                out.print(cu.toString());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create shadow:" + shadowName);
            }
        }
    }

    public String getSuperClass(String type) {
        String superType = "";
        if (type.equals("android.widget.TextView")) {
            return "org.robolectric.shadows.ShadowTextView";
        } else if (type.equals("android.widget.EditText")) {
            return "org.robolectric.shadows.ShadowTextView";
        } else if (type.equals("android.widget.Button")) {
            return "org.robolectric.shadows.ShadowTextView";
        }
        return superType;
    }
}
