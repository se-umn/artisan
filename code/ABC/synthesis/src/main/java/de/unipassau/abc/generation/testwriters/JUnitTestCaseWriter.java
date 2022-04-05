package de.unipassau.abc.generation.testwriters;

import static com.github.javaparser.StaticJavaParser.parseClassOrInterfaceType;
import static com.github.javaparser.StaticJavaParser.parseType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import de.unipassau.abc.data.AndroidMethodInvocation;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.EnumConstant;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodCallLiteralValue;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.PlaceholderDataNode;
import de.unipassau.abc.data.PrimitiveValue;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.generation.SyntheticMethodSignatures;
import de.unipassau.abc.generation.TestCaseWriter;
import de.unipassau.abc.generation.ast.visitors.DefVisitor;
import de.unipassau.abc.generation.ast.visitors.ModVisitor;
import de.unipassau.abc.generation.ast.visitors.UseVisitor;
import de.unipassau.abc.generation.data.AndroidCarvedTest;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.data.CatchBlock;
import de.unipassau.abc.generation.mocks.CarvingShadow;
import de.unipassau.abc.generation.shadowwriter.ShadowWriter;
import de.unipassau.abc.generation.utils.TestClass;
import de.unipassau.abc.generation.utils.TestMethodNamer;
import de.unipassau.abc.generation.utils.TypeUtils;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

/**
 * Generate the source code implementing the carved tests as tests were
 * implemented using JUnit. For each
 *
 * @author gambitemp
 */
public class JUnitTestCaseWriter implements TestCaseWriter {

    private static final Logger logger = LoggerFactory.getLogger(JUnitTestCaseWriter.class);

    private final Set<String> robolectricIntialLifeCycleMethods = new HashSet<String>(
            Arrays.asList("create", "start", "resume"));

//	public static final String ABC_CATEGORY = "de.unipassau.abc.Carved";

//    @Override
//    public void write(File outputFolder, TestCaseOrganizer testOrganizer, CarvedTest... carvedTests)
//            throws IOException {
//        // Group Carved Tests into Test Cases and generate the classes
//        Set<CompilationUnit> junitTestClasses = new HashSet<>();
//
//        for (TestClass testCase : testOrganizer.organize(carvedTests)) {
//            junitTestClasses.add(generateJUnitTestCase(testCase));
//        }
//
//        // TODO Some post processing? Like wrapping together @before methods or
//        // something...
//
//        for (CompilationUnit junitTestClass : junitTestClasses) {
//            File packageFolder = null;
//            if (junitTestClass.getPackageDeclaration().isPresent()) {
//                packageFolder = new File(outputFolder,
//                        junitTestClass.getPackageDeclaration().toString().replaceAll("\\.", File.separator));
//            } else {
//                packageFolder = outputFolder;
//            }
//            File outputJavaFile = new File(packageFolder, "className" + ".java");
//
//            // TODO Add Pretty print? Change Charser?
//            Files.write(outputJavaFile.toPath(), junitTestClass.toString().getBytes(StandardCharsets.UTF_16));
//        }
//
//    }

    private final static String[] genericImports = new String[] { //
            "org.junit.Before", //
            // This is not always used
            "androidx.test.core.app.ApplicationProvider" };

    // TODO I do not like to use this variable for temporary storing the method
    // under test...
    private MethodInvocation methodInvocationUnderTest;

    private void handleUnusedVariables(MethodDeclaration testMethod) {
        DefVisitor dv = new DefVisitor();
        testMethod.accept(dv, null);
        Set<String> declaredVars = dv.getDeclaredVars();
        Set<String> unusedVars = new HashSet<String>();
        for (String declaredVar : declaredVars) {
            UseVisitor uv = new UseVisitor();
            testMethod.accept(uv, declaredVar);
            boolean used = uv.isUsed();
            if (!used) {
                unusedVars.add(declaredVar);
            }
        }
        for (String unusedVar : unusedVars) {
            ModVisitor mv = new ModVisitor();
            testMethod.accept(mv, unusedVar);
        }
    }

    public CompilationUnit generateJUnitTestCase(TestClass testCase, TestMethodNamer testMethodNamer) {
        
        CompilationUnit cu = new CompilationUnit();

        cu.setPackageDeclaration(testCase.getPackageName());

        ClassOrInterfaceDeclaration testClass = cu.addClass(testCase.getName());
        
        logger.info("-------------------------");
        logger.info("Generating JUnit Test Case " + testCase.getName());
        logger.info("-------------------------");
                
        testClass.setModifiers(Modifier.Keyword.PUBLIC);

        // TODO For the moment this creates problems because we need to put a jar on the
        // classpath in gradle
        // Include the carved Category annotation
//		cu.addImport(ABC_CATEGORY);
//		ClassOrInterfaceType carvedCategoryAnnotation = parseClassOrInterfaceType(ABC_CATEGORY);
//		testClass.addSingleMemberAnnotation(Category.class, carvedCategoryAnnotation.getNameAsString() + ".class");

        // add import for intent carving, in 4.x ShadowApplication was deprecated in
        // favor to ApplicationProvider
        // Also ShadowApplication does not have a method getApplicationContext()
//        cu.addImport("org.robolectric.shadows.ShadowApplication");
//        cu.addImport("androidx.test.core.app.ApplicationProvider");
        for (String imported : genericImports) {
            cu.addImport(imported);
        }

        // adding import for shadows
        for (CarvedTest carvedTest : testCase.getCarvedTests()) {
            for (CarvingShadow cs : carvedTest.getShadows()) {
                cu.addImport(ShadowWriter.SHADOWS_PACKAGE + "." + cs.getShadowName());
            }
        }

        // If any of the tests in this test case requires a specific runner we need to
        // add it to the class
        boolean requiresRobolectricTestRunner = false;
        for (CarvedTest carvedTest : testCase.getCarvedTests()) {
            if (carvedTest instanceof AndroidCarvedTest) {
                requiresRobolectricTestRunner = true;
                break;
            }
        }

        if (requiresRobolectricTestRunner) {
            // https://stackoverflow.com/questions/36082370/i-need-both-robolectric-and-mockito-in-my-test-each-one-proposes-their-own-test
            cu.addImport(RobolectricTestRunner.class);
            ClassOrInterfaceType robolectricTestRunner = parseClassOrInterfaceType("RobolectricTestRunner");
            testClass.addSingleMemberAnnotation(RunWith.class, robolectricTestRunner.getNameAsString() + ".class");
            Set<String> shadowTypes = new HashSet<>();
            testCase.getCarvedTests()
                    .forEach(test -> test.getShadowsNames().forEach(shadow -> shadowTypes.add(shadow)));
            if (!shadowTypes.isEmpty()) {
                List<Expression> shadowClasses = shadowTypes.stream().map(s -> new ClassOrInterfaceType(null, s))
                        .map(ClassExpr::new).collect(Collectors.toList());
                NormalAnnotationExpr annotation = new NormalAnnotationExpr(
                        new Name("org.robolectric.annotation.Config"), NodeList.nodeList(new MemberValuePair("shadows",
                                new ArrayInitializerExpr(NodeList.nodeList(shadowClasses)))));
                testClass.addAnnotation(annotation);
            }

            // https://stackoverflow.com/questions/13513568/testing-listview-with-robolectric
            // Create the test setup method only if the test requires robolectric
            MethodDeclaration setupMethod = testClass.addMethod("setup", Modifier.Keyword.PUBLIC);
            setupMethod.addAndGetAnnotation(Before.class);
            BlockStmt block = new BlockStmt();
            setupMethod.setBody(block);
            // add a statement do the method body:
            NameExpr clazz = new NameExpr("org.robolectric.shadows.ShadowSQLiteConnection");
            MethodCallExpr call = new MethodCallExpr(clazz, "reset");
            block.addStatement(call);
            // add the redirection between ShadowLog and System.out
            // This is for printing log messages in console
            // TODO This breaks the synthesis. Probably it misses some additional part or
            // simply is not a "Statement"
//            block.addStatement("org.robolectric.shadows.ShadowLog.stream = System.out");

        }

        for (CarvedTest carvedTest : testCase.getCarvedTests()) {
            // TODO Name tests
            MethodDeclaration testMethod = testClass.addMethod(testMethodNamer.generateTestMethodName(carvedTest),
                    Modifier.Keyword.PUBLIC);
                        
            // Annotate the method with JUnit @Test, add time out
            NormalAnnotationExpr annotation = testMethod.addAndGetAnnotation(Test.class);
            MemberValuePair timeout = new MemberValuePair("timeout", new NameExpr("4000"));
            annotation.getPairs().add(timeout);
            // Add the generic Exception throwing
            testMethod.addThrownException(Exception.class);

            String generatedFromComment = String.format("%nGenerated from %s%nMethod invocation under test: %s",
                    carvedTest.getTraceId(), carvedTest.getMethodUnderTest());
            testMethod.setComment(new JavadocComment(generatedFromComment));

            // 
            logger.info(" - Test Method " + carvedTest.getMethodUnderTest()  + " " + generatedFromComment );

            
            // Generate method body
            generateMethodBody(testMethod, carvedTest);
            handleUnusedVariables(testMethod);
        }

        return cu;
    }

    // TODO Those are scoped by the method, not the test !!
    // Keep track of the variables declared so far.
    private Map<DataNode, String> declaredVariables = new HashMap<>();

    private Map<String, DataNode> declaredControllers = new HashMap<>();

    // Simple Index to name variables by their class name
    private Map<String, AtomicInteger> declaredVariablesIndex = new HashMap<>();

    private List<String> filterMethods = new ArrayList<String>();

    /**
     * Generate a "self" contained block of code from the given carvedTest
     *
     * @return
     */
    public BlockStmt generateBlockStmtFrom(CarvedTest carvedTest) {

        logger.debug("Generate Method Body");
        // Alessio: At this point the execution flow graph already contains the code for
        // the mocks and the shadows
        ExecutionFlowGraph executionFlowGraph = carvedTest.getExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = carvedTest.getDataDependencyGraph();

        BlockStmt blockStmt = new BlockStmt();

        logger.debug("##################Objects");
        // TODO First declare all the variables for non-primitive-like types in the
        // scope of this block ?
        Set<ObjectInstance> objectInstances = new HashSet<>(dataDependencyGraph.getObjectInstances());
        for (ObjectInstance objectInstance : objectInstances) {
            logger.debug("" + objectInstance);
            // Default initialization. Avoid variable not initialized variable
            // warning/errors
            if (objectInstance.isAndroidActivity() || objectInstance.isAndroidFragment()) {
                declareControllerFor(objectInstance, blockStmt, new NullLiteralExpr());
            }
            if (objectInstance instanceof EnumConstant) {
                // creates statement for object
                declareVariableFor(carvedTest.getDataDependencyGraph(), objectInstance, blockStmt,
                        new NameExpr(((EnumConstant) objectInstance).getFullyQualifiedName()));
            } else {
                declareVariableFor(carvedTest.getDataDependencyGraph(), objectInstance, blockStmt,
                        new NullLiteralExpr());
            }
        }

        logger.debug("##################Body");
        // Next implement all the methods, making sure variables and parameters match
        String referenceToActualValue = null;
        // Since at this point mocks and shadows have been already forced into the
        // execution flow we return all of them
        for (MethodInvocation methodInvocation : executionFlowGraph.getMethodInvocationsSortedByID()) {
            logger.debug("" + methodInvocation);

            String fqn = JimpleUtils.getFullyQualifiedMethodName(methodInvocation.getMethodSignature());
            if (filterMethods.contains(fqn)) {
                logger.debug("Force Filter method invocation" + methodInvocation);
                continue;
            }
            // Now we create each method calls making sure that variables and the like
            // matches
            if (methodInvocation.isConstructor() && methodInvocation.isStatic()) {
                logger.warn("Skipping Static Constructor " + methodInvocation);
            } else if (methodInvocation.isConstructor()) {
                String generatedVariable = generateConstructorCall(methodInvocation, blockStmt);

                if (methodInvocation.equals(carvedTest.getMethodUnderTest())) {
                    referenceToActualValue = generatedVariable;
                }
            } else {
                Optional<String> generatedVariable = generateMethodCall(carvedTest.getDataDependencyGraph(),
                        methodInvocation, blockStmt);
                // Method calls might be void, so no variable is generated after invoking
                // them...
                if (methodInvocation.equals(carvedTest.getMethodUnderTest()) && generatedVariable.isPresent()) {
                    referenceToActualValue = generatedVariable.get();
                }

            }
        }

//		logger.debug("##################Shadows");
//        for (CarvingShadow carvingShadow : carvedTest.getShadows()) {
//              for (Pair<ExecutionFlowGraph, DataDependencyGraph> pair : zip(
//                  carvingShadow.executionFlowGraphs,
//                  carvingShadow.dataDependencyGraphs)) {
//                ExecutionFlowGraph carvingExecutionFlowGraph = pair.getFirst();
//                for (MethodInvocation methodInvocation : carvingExecutionFlowGraph
//                    .getOrderedMethodInvocations()) {
//                	logger.debug(methodInvocation);
//                    if (methodInvocation.isConstructor()) {
//                        generateConstructorCall(methodInvocation, blockStmt);
//                    } else {
//                        generateMethodCall(methodInvocation, blockStmt);
//                    }
//                }
//            }
//        }

        // TODO ALESSIO: The asseritions should be recomputed after simplification !!
        logger.warn("Synthesis of Assertion is currently DISABLED!");
//        logger.debug("##################Assertions");
//        // MF: FIXME should we really have assertions?
//        // Next implement all the assertions using REFERENCE TO ACTUAL VALUE for the
//        // assertions !
//        for (CarvingAssertion carvingAssertion : carvedTest.getAssertions()) {
//            for (Pair<ExecutionFlowGraph, DataDependencyGraph> pair : zip(carvingAssertion.executionFlowGraphs,
//                    carvingAssertion.dataDependencyGraphs)) {
//                // TODO Join the variable instead of the value for primitives
//                ExecutionFlowGraph assertionExecutionGraph = pair.getFirst();
//                for (MethodInvocation methodInvocation : assertionExecutionGraph.getOrderedMethodInvocations()) {
//                    // By design assertions goes by the end of the test
//                    if (methodInvocation.isConstructor()) {
//                        generateConstructorCall(methodInvocation, blockStmt);
//                    } else {
//                        generateMethodCall(methodInvocation, blockStmt);
//
//                    }
//                }
//            }
//
//        }

        // reorder statements
        reorderStatements(blockStmt.getStatements());

        return blockStmt;
    }

    /**
     * Generate a "self" contained block of code from the given data structures by
     * first declaring variables and the implementing the calls, one after the
     * other. The method takes care to define the bindings from synthetic methods
     * and actual methods
     *
     * @param executionFlowGraph
     * @param dataDependencyGraph
     * @return
     */
    public BlockStmt generateBlockStmtFromGraphs(ExecutionFlowGraph executionFlowGraph,
            DataDependencyGraph dataDependencyGraph) {

        BlockStmt blockStmt = new BlockStmt();
        // TODO First declare all the variables for non-primitive-like types in the
        // scope of this block ?
        Set<ObjectInstance> objectInstances = new HashSet<ObjectInstance>(dataDependencyGraph.getObjectInstances());
        for (ObjectInstance objectInstance : objectInstances) {
            // Default initialization. Avoid variable not initialized variable
            // warning/errors
            declareVariableFor(dataDependencyGraph, objectInstance, blockStmt, new NullLiteralExpr());
        }

        // Next implement all the methods, making sure variables and parameters match
        for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
            if (methodInvocation.isConstructor()) {
                generateConstructorCall(methodInvocation, blockStmt);
            } else {
                generateMethodCall(dataDependencyGraph, methodInvocation, blockStmt);
            }
        }

        return blockStmt;
    }

    /**
     * Generate a javaparser catch clause from an abc CatchBlock
     *
     * @param catchBlock
     * @return
     */
    private CatchClause generateCatchClause(CatchBlock catchBlock, String exceptionName) {
        // TODO Handle multi catch block? Not sure we will ever use it
        ClassOrInterfaceType capturedExceptionType = new ClassOrInterfaceType(
                catchBlock.getCapturedExceptions().get(0));

        CatchClause catchClause = new CatchClause();
        Parameter p = new Parameter();
        p.setName(exceptionName);
        p.setType(capturedExceptionType);
        catchClause.setParameter(p);

        BlockStmt cb = generateBlockStmtFromGraphs(catchBlock.getExecutionFlowGraph(),
                catchBlock.getDataDependencyGraph());
        catchClause.setBody(cb);
        return catchClause;
    }

    // MOVE THIS TO UTILITY CLASS
    public static <A, B> List<Pair<A, B>> zip(List<A> as, List<B> bs) {
        Iterator<A> it1 = as.iterator();
        Iterator<B> it2 = bs.iterator();
        List<Pair<A, B>> result = new ArrayList<>();
        while (it1.hasNext() && it2.hasNext()) {
            result.add(new Pair<A, B>(it1.next(), it2.next()));
        }
        return result;
    }

    private void reorderStatements(NodeList<Statement> statements) {
        // move get after consecutive lifecycle methods
        int getMethodCallIndex = -1;
        for (int i = 0; i < statements.size(); ++i) {
            Statement stmt = statements.get(i);
            if (stmt instanceof ExpressionStmt) {
                ExpressionStmt exprStmt = (ExpressionStmt) stmt;
                if (exprStmt.getExpression() instanceof AssignExpr) {
                    AssignExpr aExpr = (AssignExpr) exprStmt.getExpression();
                    if (aExpr.getValue() instanceof MethodCallExpr) {
                        MethodCallExpr mcExpr = (MethodCallExpr) aExpr.getValue();
                        if (mcExpr.getNameAsString().equals("get")) {
                            if (mcExpr.getScope().isPresent() && mcExpr.getScope().get() instanceof NameExpr) {
                                NameExpr nExpr = (NameExpr) mcExpr.getScope().get();
                                String varName = nExpr.getNameAsString();
                                boolean isControlleerVarName = false;
                                for (DataNode declaredVariableDataNode : declaredVariables.keySet()) {
                                    if (declaredVariables.get(declaredVariableDataNode).equals(varName)) {
                                        if (declaredControllers.values().contains(declaredVariableDataNode)) {
                                            isControlleerVarName = true;
                                        }
                                    }
                                }
                                if (isControlleerVarName) {
                                    getMethodCallIndex = i;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (getMethodCallIndex != -1) {
            int lastConsecutiveLifecylePosition = -1;
            for (int i = (getMethodCallIndex + 1); i < statements.size(); ++i) {
                Statement stmt = statements.get(i);
                ExpressionStmt exprStmt = (ExpressionStmt) stmt;
                if (exprStmt.getExpression() instanceof MethodCallExpr) {
                    MethodCallExpr mcExpr = (MethodCallExpr) exprStmt.getExpression();
                    if (robolectricIntialLifeCycleMethods.contains(mcExpr.getNameAsString())) {
                        if (mcExpr.getScope().isPresent() && mcExpr.getScope().get() instanceof NameExpr) {
                            NameExpr nExpr = (NameExpr) mcExpr.getScope().get();
                            String varName = nExpr.getNameAsString();
                            boolean isControlleerVarName = false;
                            for (DataNode declaredVariableDataNode : declaredVariables.keySet()) {
                                if (declaredVariables.get(declaredVariableDataNode).equals(varName)) {
                                    if (declaredControllers.values().contains(declaredVariableDataNode)) {
                                        isControlleerVarName = true;
                                    }
                                }
                            }
                            if (isControlleerVarName) {
                                lastConsecutiveLifecylePosition = i;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            if (lastConsecutiveLifecylePosition != -1) {
                // reorder statements
                Statement getStmt = statements.remove(getMethodCallIndex);
                statements.add(lastConsecutiveLifecylePosition, getStmt);
            }
        }
    }

    private void generateMethodBody(MethodDeclaration testMethod, CarvedTest carvedTest) {
        // logging
        logger.debug("Method under test:");
        logger.debug(carvedTest.getMethodUnderTest().toString());
        logger.debug("Statements:");
        carvedTest.getStatements().forEach(methodInvocation -> logger.debug(methodInvocation.toFullString()));

        if (carvedTest.expectException()) {
            BlockStmt methodBody = generateBlockStmtFrom(carvedTest);

            // Build the try statement that encapsulate the entire method body
            TryStmt tryStatement = new TryStmt();
            tryStatement.setTryBlock(methodBody);

            // If the test should fail with an exception. It must be the correct exception
            CatchBlock expectedExceptionCatchBlock = carvedTest.getExpectedExceptionCatchBlock();
            CatchClause expectedExceptionCatchClause = generateCatchClause(expectedExceptionCatchBlock, "expected");

            // Catch and rethrow AssertionError
            CatchClause assertionErrorCatchClause = new CatchClause();
            Parameter p = new Parameter();
            p.setName("assertionException");
            p.setType(AssertionError.class.getName());
            assertionErrorCatchClause.setParameter(p);
            BlockStmt cb = new BlockStmt();
            // TODO
            cb.addStatement(new ThrowStmt(new NameExpr("assertionException")));
            assertionErrorCatchClause.setBody(cb);

            // If the test fails with another exception, which is not AssertionError, then
            // myst be fail
            CatchBlock unexpectedExceptionCatchBlock = carvedTest.getUnexpectedExceptionCatchBlock();
            CatchClause unexpectedExceptionCatchClause = generateCatchClause(unexpectedExceptionCatchBlock,
                    "unexpected");

            NodeList<CatchClause> multiCatchBlock = new NodeList<>();
            multiCatchBlock.add(expectedExceptionCatchClause);
            multiCatchBlock.add(assertionErrorCatchClause);
            multiCatchBlock.add(unexpectedExceptionCatchClause);
            //
            tryStatement.setCatchClauses(multiCatchBlock);

            BlockStmt wrappedMethodBody = new BlockStmt();
            wrappedMethodBody.addStatement(tryStatement);
            testMethod.setBody(wrappedMethodBody);

        } else {
            BlockStmt methodBody = generateBlockStmtFrom(carvedTest);
            testMethod.setBody(methodBody);
        }

    }

    private String generateConstructorCall(MethodInvocation methodInvocation, BlockStmt methodBody) {
        /*
         * Despite it might seem better to update the previous variable declaration,
         * doing so it is tricky as arguments used in the constructor must be
         * initialized and set before use so we simply reassign the variable...
         */
        logger.debug("JUnitTestCaseWriter.generateConstructorCall() " + methodInvocation);
        ObjectInstance owner = methodInvocation.getOwner();
        String variableName = getVariableFor(owner, methodBody);
        Expression variableInitializingExpr;

//        if (owner.isAndroidActivity()) {
//            MethodCallExpr methodCallExpr = null;
//            if (owner.requiresIntent()) {
//                // TODO This is still temporary, but unless this variable is found in the data
//                // graph, we can safely discard it?
//                // The question is: do we need that intent in the first place?
//
//                ObjectInstance intent = owner.getIntent();
//                // Retrieve intent's variable name - Note since there's taining we need to
//                // understand how to handle this case
//                // when we do NOT taint intents... or the entire app is startet with an Intent
//                final String intentVariableName = declaredVariables.get(intent);
//
////                assert (intentVariableName != null) : "Intent Variable for " + intent + " is NOT YET defined?!";
//                if (intentVariableName != null) {
//                    final NameExpr intentNameExpr = new NameExpr(intentVariableName);
//
//                    // We first build and initialize the activity controller for the corresponding
//                    // activity
//                    methodCallExpr = new MethodCallExpr(JimpleUtils.getFullyQualifiedMethodName(
//                            "<org.robolectric.Robolectric: ActivityController buildActivity(java.lang.Class,android.content.Intent)>"),
//                            // First parameter
//                            new TypeExpr(new ClassOrInterfaceType(null, owner.getType() + ".class")),
//                            // Second parameter
//                            intentNameExpr);
//                } else {
//                    logger.warn("Intent Variable for " + intent + " is NOT YET defined!");
//                }
//
//            } else {
//                // We first build and initialize the activity controller for the corresponding
//                // activity
//                methodCallExpr = new MethodCallExpr(
//                        JimpleUtils.getFullyQualifiedMethodName(
//                                "<org.robolectric.Robolectric: ActivityController buildActivity(java.lang.Class)>"),
//                        new TypeExpr(new ClassOrInterfaceType(null, owner.getType() + ".class")));
//
//            }
//
//            // Retrieve controller's variable name
//            final String controllerVariableName = declaredVariables.get(declaredControllers.get(owner.getType()));
//            final NameExpr controllerNameExpr = new NameExpr(controllerVariableName);
//
//            // Assign the controller to the variable
//            methodBody.addStatement(new AssignExpr(controllerNameExpr, methodCallExpr, Operator.ASSIGN));
//
//            // Now get the activity from the robolectric controller
//            variableInitializingExpr = new MethodCallExpr(controllerNameExpr, "get");
//
//        } else 
        if (owner.isAndroidFragment()) {
            // TODO At the moment we do not
            throw new NotImplementedException("We cannot generate test cases involving AndroidFragments");

        } else {
            String className = JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature());

            List<String> arguments = methodInvocation.getActualParameterInstances().stream().map(parameter -> {
                if (parameter instanceof PlaceholderDataNode) {
                    PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) parameter;
                    return declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                } else {
                    return getParameterFor(parameter, methodBody);
                }
            }).collect(Collectors.toList());

            // Arrays must be created differently with javaparser, otherwise we're gonna get
            // something like new String[](2)
            if (JimpleUtils.isArray(className)) {
                // TODO this only considers things like new int[2],
                // but no direct array initialization, i.e., new int[] {1, 2, 3}
                ClassOrInterfaceType type = new ClassOrInterfaceType(null, JimpleUtils.getArrayElementType(className));

                List<ArrayCreationLevel> levels = arguments.stream().map(arg -> {
                    Expression dimension = new IntegerLiteralExpr(arg);
                    return new ArrayCreationLevel(dimension);
                }).collect(Collectors.toList());

                variableInitializingExpr = new ArrayCreationExpr(type, NodeList.nodeList(levels), null);
            } else {
                // TODO No idea what scope is
                ClassOrInterfaceType type = new ClassOrInterfaceType(null,
                        JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()));
                ObjectCreationExpr constructorCall = new ObjectCreationExpr();
                constructorCall.setType(type);
                arguments.forEach(constructorCall::addArgument);

                variableInitializingExpr = constructorCall;
            }
        }

        AssignExpr variableAssignment = new AssignExpr(new NameExpr(variableName), variableInitializingExpr,
                Operator.ASSIGN);
        methodBody.addStatement(variableAssignment);

        // Here the variable name is the one returned by the constructor
        return variableName;
    }

    // TODO Move this to RobolectricSimplifier
    private String generateRobolectricLifecycleCall(MethodInvocation methodInvocation, BlockStmt methodBody) {
        // Does such a method invocation always have an owner?
        final String methodName = JimpleUtils.getMethodName(methodInvocation.getMethodSignature());
        final ObjectInstance owner = methodInvocation.getOwner();
        final String controllerVariableName = declaredVariables.get(declaredControllers.get(owner.getType()));

        // Robolectric defines its lifecycle method names without `on` prefix
        final String robolectricLifecycleMethodName = firstLetterToLowerCase(methodName.substring(2));

        // Invoke the lifecycle method on the controller, which (hopefully) updates the
        // activity
        // instance, so we don't have to update the activity instance manually
        final MethodCallExpr robolectricLifecycleCall = new MethodCallExpr(new NameExpr(controllerVariableName),
                robolectricLifecycleMethodName);
        final String ownerVariableName = getVariableFor(owner, methodBody);

        // Add arguments if needed
        for (DataNode parameter : methodInvocation.getActualParameterInstances()) {
            if (parameter instanceof PlaceholderDataNode) {
                // No idea what this is used for, but this is checked for while
                // creating parameters in other places too
                PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) parameter;
                String variable = declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                robolectricLifecycleCall.addArgument(variable);
            } else {
                robolectricLifecycleCall.addArgument(getParameterFor(parameter, methodBody));
            }
        }

        methodBody.addStatement(robolectricLifecycleCall);
        return ownerVariableName;
    }

    /**
     * We need to patch the call to findViewById(int,String, String)
     * 
     * @param methodInvocation
     * @param methodBody
     * @return
     */
    private Optional<String> generateMethodCall(DataDependencyGraph dataDependencyGraph,
            MethodInvocation methodInvocation, BlockStmt methodBody) {
        String methodName = JimpleUtils.getMethodName(methodInvocation.getMethodSignature());
        Expression methodCallExpr;
        String variableName = null;

        logger.debug("GENERATING CODE FOR " + methodInvocation);

        /**
         * Step 1: prepare the invocation of the method
         */
        if (methodInvocation instanceof AndroidMethodInvocation
                && ((AndroidMethodInvocation) methodInvocation).isAndroidActivityCallback()) {
            // TODO what about fragment callbacks?
            return Optional.ofNullable(generateRobolectricLifecycleCall(methodInvocation, methodBody));
        } else if (!methodInvocation.isStatic()) {
            ObjectInstance owner = methodInvocation.getOwner();
            String variableNameOwner = getVariableFor(owner, methodBody);
            NameExpr nameExpr = new NameExpr(variableNameOwner);

            if (!JimpleUtils.isArray(owner.getType())) {
                methodCallExpr = new MethodCallExpr(nameExpr, methodName);
            } else {

                // A writable array access like arr.store(0, "value")
                if (methodName.equals("store")) {
                    // The first argument must be an index, while the second one ist the new value
                    List<String> arguments = methodInvocation.getActualParameterInstances().stream().map(parameter -> {
                        if (parameter instanceof PlaceholderDataNode) {
                            PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) parameter;
                            return declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                        } else {
                            return getParameterFor(parameter, methodBody);
                        }
                    }).limit(2).collect(Collectors.toList());

                    IntegerLiteralExpr index = new IntegerLiteralExpr(arguments.get(0));
                    NameExpr value = new NameExpr(arguments.get(1));

                    ArrayAccessExpr arrayAccessExpr = new ArrayAccessExpr(nameExpr, index);
                    methodCallExpr = new AssignExpr(arrayAccessExpr, value, Operator.ASSIGN);
                } else {
                    throw new NotImplementedException("Operations other then array store are not implemented yet");
                }

            }
        } else {
            // TODO Here we use JUnit assertions, while we should be using Hamcrest
            // https://stackoverflow.com/questions/56772801/assert-fail-equivalent-using-hamcrest
            // Static methods
            // Include the Type, but shoud we use TypeExpr instead?
            if (methodInvocation.isSynthetic()) {
                switch (methodInvocation.getMethodSignature()) {
                // TODO I am not sure this is used at all
                case SyntheticMethodSignatures.FAIL_WITH_MESSAGE:
                    methodCallExpr = new MethodCallExpr(
                            JimpleUtils.getFullyQualifiedMethodName("<org.junit.Assert: void fail(java.lang.String)>"));
                    break;
                case SyntheticMethodSignatures.GENERATE_DEFAULT_CONTEXT:
                    // Add a patch to replace abc.DefaultContextGenerator.generateDefaultContext();
                    // Note that the last () are missing by design
                    methodCallExpr = new MethodCallExpr("ApplicationProvider.getApplicationContext");
                    break;
                default:
                    throw new NotImplementedException(
                            "Binding not defined for synthetic method call: " + methodInvocation.getMethodSignature());
                }
            } else {
                methodCallExpr = new MethodCallExpr(
                        JimpleUtils.getFullyQualifiedMethodName(methodInvocation.getMethodSignature()));
            }
        }

        /**
         * Step 2: add the parameters to make the invocation make sure that expected
         * return values are used correctly...
         */
        if (methodInvocation.getMethodSignature().equals(
                "<android.app.Activity: android.view.View findViewById(int,java.lang.String,java.lang.String)>")) {

            // Do the magic trick: replace the first argument (the int) with the "string"
            DataNode classR = methodInvocation.getActualParameterInstances().get(1);
            DataNode resourceName = methodInvocation.getActualParameterInstances().get(2);

            String referenceToR = getParameterFor(classR, methodBody).replaceAll("\"", "").replace('$', '.');
            String referenceToViewName = getParameterFor(resourceName, methodBody).replaceAll("\"", "").replace('$',
                    '.');

            String referenceToViewId = referenceToR + "." + referenceToViewName;

            methodCallExpr.asMethodCallExpr().addArgument(referenceToViewId);
        } else if (methodInvocation.getMethodSignature().equals("<android.app.Activity: void setContentView(int)>")) {
            // TODO Try to check every method call that accepts an integer the represent an
            // identifier
            DataNode dataValueForInt = methodInvocation.getActualParameterInstances().get(0);
            String dataValueString = getParameterFor(dataValueForInt, methodBody);
            Integer integerValue = Integer.parseInt(dataValueString);
            if (Main.idsInApk.containsKey(integerValue)) {
                methodCallExpr.asMethodCallExpr().addArgument(Main.idsInApk.get(integerValue));
            } else {
                methodCallExpr.asMethodCallExpr().addArgument(dataValueString);
            }
        } else if (methodInvocation.getMethodSignature().equals("<android.widget.TextView: void setText(int)>")) {

            DataNode dataValueForInt = methodInvocation.getActualParameterInstances().get(0);
            String dataValueString = getParameterFor(dataValueForInt, methodBody);
            Integer integerValue = Integer.parseInt(dataValueString);
            if (Main.idsInApk.containsKey(integerValue)) {
                methodCallExpr.asMethodCallExpr().addArgument(Main.idsInApk.get(integerValue));
            } else {
                methodCallExpr.asMethodCallExpr().addArgument(dataValueString);
            }
        } else if (methodInvocation.getMethodSignature()
                .equals("<org.mockito.Mockito: org.mockito.stubbing.Stubber doReturn(java.lang.Object)>")) {

            DataNode dataValue = methodInvocation.getActualParameterInstances().get(0);

            if (dataValue.getType() != null && dataValue.getType().equals("int")) {
                String dataValueString = getParameterFor(dataValue, methodBody);
                Integer integerValue = Integer.parseInt(dataValueString);
                if (Main.idsInApk.containsKey(integerValue)) {
                    methodCallExpr.asMethodCallExpr().addArgument(Main.idsInApk.get(integerValue));
                } else {
                    if (dataValue instanceof PlaceholderDataNode) {
                        PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) dataValue;
                        String variable = declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                        methodCallExpr.asMethodCallExpr().addArgument(variable);
                    } else {
                        methodCallExpr.asMethodCallExpr().addArgument(getParameterFor(dataValue, methodBody));
                    }
                }
            } else {
                if (dataValue instanceof PlaceholderDataNode) {
                    PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) dataValue;
                    String variable = declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                    methodCallExpr.asMethodCallExpr().addArgument(variable);
                } else {
                    methodCallExpr.asMethodCallExpr().addArgument(getParameterFor(dataValue, methodBody));
                }
            }
        } else if (methodInvocation.getMethodSignature()
                .equals("<org.mockito.Mockito: java.lang.Object mock(java.lang.Class)>")) {

            // Make sure we use the right type here !

            DataNode dataValue = methodInvocation.getActualParameterInstances().get(0);

            String classExpression = dataValue.toString();
            // Does this require to be adjusted?
            String replacementeType = TypeUtils.convertType(classExpression.replace(".class", ""));
            if (replacementeType == null) {
                // No it does not
                methodCallExpr.asMethodCallExpr().addArgument(new NameExpr(classExpression));
            } else {
                methodCallExpr.asMethodCallExpr().addArgument(new NameExpr(replacementeType + ".class"));
            }

        }

        else if (!methodInvocation.isStatic() && JimpleUtils.isArray(methodInvocation.getOwner().getType())) {
            // Skip, we already defined the array statement
        } else if (methodInvocation.getMethodSignature().equals(
                "<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>")
                || methodInvocation.getMethodSignature().equals(
                        "<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.io.Serializable)>")) {

            // Parameter 1 -- TODO Simplify
            DataNode parameter_1 = methodInvocation.getActualParameterInstances().get(0);
            if (parameter_1 instanceof PlaceholderDataNode) {
                PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) parameter_1;
                String variable = declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                methodCallExpr.asMethodCallExpr().addArgument(variable);
            } else {
                methodCallExpr.asMethodCallExpr().addArgument(getParameterFor(parameter_1, methodBody));

            }

            DataNode parameter_2 = methodInvocation.getActualParameterInstances().get(1);
            String typeToCast = JimpleUtils.getParameterList(methodInvocation.getMethodSignature())[1];

            if (parameter_2 instanceof PlaceholderDataNode) {
                PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) parameter_2;
                String variable = declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                CastExpr castedVariable = new CastExpr(parseType(typeToCast), new NameExpr(variable));
                methodCallExpr.asMethodCallExpr().addArgument(castedVariable);
            } else {
                CastExpr castedVariable = new CastExpr(parseType(typeToCast),
                        new NameExpr(getParameterFor(parameter_2, methodBody)));
                methodCallExpr.asMethodCallExpr().addArgument(castedVariable);

            }

        } else {
            for (DataNode parameter : methodInvocation.getActualParameterInstances()) {
                if (parameter instanceof PlaceholderDataNode) {
                    PlaceholderDataNode placeholderDataNode = (PlaceholderDataNode) parameter;
                    String variable = declaredVariables.get(placeholderDataNode.getOriginalDataNode());
                    methodCallExpr.asMethodCallExpr().addArgument(variable);
                } else {
                    methodCallExpr.asMethodCallExpr().addArgument(getParameterFor(parameter, methodBody));
                }
            }
        }

        /**
         * Step 3 capture the return value into a "new?" variable. This probably should
         * be improved for reusing the "same" variables...
         */
        if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())
                && !methodInvocation.isExceptional()) {

            // Resolve the type of the returned object

            String staticReturnType = JimpleUtils.getReturnType(methodInvocation.getMethodSignature());
            // TODO Move this into some "resolve type" method?
            String expectedReturnType = null;
            try {
                expectedReturnType = TypeUtils.getActualTypeFor(methodInvocation.getReturnValue());
            } catch (NotImplementedException e) {
                if (methodInvocation.getReturnValue() instanceof ObjectInstance) {
                    expectedReturnType = TypeUtils.getFormalTypeFor((ObjectInstance) methodInvocation.getReturnValue(),
                            dataDependencyGraph);
                } else {
                    throw e;
                }
            }

            /*
             * TODO to be really sure before casting one can check their runtime types, but
             * some won't be on the classpath, e.g., android-specific stuff
             * 
             * Class<?> staticReturnClass = Class.forName(staticReturnType); Class<?>
             * expectedReturnClass = Class.forName(expectedReturnType); if
             * (!staticReturnClass.isAssignableFrom(expectedReturnClass)) { ... }
             */

            // Assume that the return type can be down-casted, otherwise there is an error
            // somewhere up the stream
            if (!staticReturnType.equals(expectedReturnType) && !methodInvocation.hasGenericReturnType()) {
                final CastExpr castedMethodCallExpr = new CastExpr(parseType(expectedReturnType), methodCallExpr);
                variableName = declareVariableFor(dataDependencyGraph, methodInvocation.getReturnValue(), methodBody,
                        castedMethodCallExpr);
            } else {
                variableName = declareVariableFor(dataDependencyGraph, methodInvocation.getReturnValue(), methodBody,
                        methodCallExpr);
            }

        } else {
            methodBody.addStatement(methodCallExpr);
        }

        return Optional.ofNullable(variableName);

    }

    /**
     * Either return the variable corresponding to the parameter or the value of
     * that's a primitive type
     *
     * @return
     */
    private String getParameterFor(DataNode dataNode, BlockStmt methodBody) {
        if (dataNode instanceof PrimitiveValue) {
            logger.debug("Get Parameter for data node " + dataNode + " of type " + dataNode.getType());
            try {
                if (Class.class.getName().equals(dataNode.getType())) {
                    // Try to represent inner classes with dot notation
                    return ((PrimitiveValue) dataNode).getStringValue().replaceAll("\\$", ".");
                } else {
                    /*
                     * We use toString() instead of getStringValue() to wrap strings which have two
                     * different representations.
                     */
                    return dataNode.toString();
                }
            } catch (Throwable t) {
                logger.error("Error processing data node" + dataNode, t);
                // TODO: handle exception
                throw t;
            }

        } else if (dataNode instanceof MethodCallLiteralValue) {
            throw new NotImplementedException();
//			MethodCallLiteralValue methodCall = (MethodCallLiteralValue) dataNode;
//			// This is to get the reference to the mocked object
//			String mockedObject = getVariableFor(methodCall.getOwner()).getSecond();
//			String methodToInvoke = JimpleUtils.getMethodName(methodCall.getMethodSignature());
//			// THIS IS TO DO
//			String parameterList = "";
//			return mockedObject + "." + methodToInvoke + "(" + parameterList + ")";
        }
        // TODO Keep the reference
//			else if (dataNode instanceof NullInstance) {
//			return "null";
//		} 
        else if (dataNode instanceof ObjectInstance) {
            return getVariableFor((ObjectInstance) dataNode, methodBody);
        } else {
            throw new RuntimeException("Cannot handle type " + dataNode);
        }
    }

    // TODO Consider using Builder methodBuilder
    private String getVariableFor(ObjectInstance objectInstance, BlockStmt methodBody) {
        // TODO Scope by methodBody
        return declaredVariables.get(objectInstance);
    }

    private String declareControllerFor(ObjectInstance instance, BlockStmt methodBody, Expression initializer) {
        final String activityType = instance.getType();
        // Make sure that we use fully qualified names or get the imports right
        final String type = "org.robolectric.android.controller.ActivityController<" + activityType + ">";

        if (!declaredVariablesIndex.containsKey(type)) {
            declaredVariablesIndex.put(type, new AtomicInteger(0));
        }

        final String variableName = firstLetterToLowerCase(activityType.substring(activityType.lastIndexOf(".") + 1))
                + "Controller" + declaredVariablesIndex.get(type).getAndIncrement();
        ObjectInstance objectInstance = new ObjectInstance(variableName);

        // This is kind of work around to keep existing datastructures untouched, but we
        // need a way to
        // retrieve the variable name of the artificial variable
        declaredControllers.put(activityType, objectInstance);
        // Assuming we only have one activity or whatsoever per test, so we can use the
        // type as key
//        System.out.println("JUnitTestCaseWriter.declareControllerFor()" + objectInstance + "-->" + variableName);
        declaredVariables.put(objectInstance, variableName);

        return declareVariableFor(variableName, type, methodBody, initializer);
    }

    // TODO Contenxtualize on MethodBody/scope !
    private String declareVariableFor(DataDependencyGraph dataDependencyGraph, DataNode dataNode, BlockStmt methodBody,
            Expression initializer) {

        // TODO Replace this with get Best Type For?
        String variableType = null;
        try {
            variableType = TypeUtils.getActualTypeFor(dataNode);
        } catch (NotImplementedException e) {
            // If we cannot find a valid actual type
            if (dataNode instanceof ObjectInstance) {
                variableType = TypeUtils.getFormalTypeFor((ObjectInstance) dataNode, dataDependencyGraph);
            } else {
                throw e;
            }
        } catch (Exception e) {
            throw e;
        }

        // TODO Move to Utils methods as this is used in many places
        String className = variableType.substring(variableType.lastIndexOf('.') + 1);

        // Avoid duplicate ObjectInstances. How this can be possible? Maybe super/sub
        // classes?
        if (!declaredVariablesIndex.containsKey(className)) {
            declaredVariablesIndex.put(className, new AtomicInteger(0));
        }

        // Use classname to avoid clashing with variables of different classes
        // with the same name...
        String variableName = className.toLowerCase() + "" + declaredVariablesIndex.get(className).getAndIncrement();
        // Ensure names are valid (i.e., remove [] from the name
        variableName = variableName.replaceAll("\\[", "").replaceAll("\\]", "");

        // We need to match the method invocations with variable names
//        System.out.println("JUnitTestCaseWriter.declareVariableFor() " +dataNode + " --> " + variableName );

        declaredVariables.put(dataNode, variableName);

        // Link all the alias to the same variable if any
        // TODO Why there are aliases here? Super/sub type? Maybe this can be resolved
        // directly from the CarvedTest
//		if (dataNode instanceof ObjectInstance) {
//			for (ObjectInstance alias : dataDependencyGraph.getAliasesOf((ObjectInstance) dataNode)) {
//				declaredVariables.put(alias, variable);
//			}
//		}

        return declareVariableFor(variableName, variableType, methodBody, initializer);
    }

    private String declareVariableFor(String variableName, String variableType, BlockStmt methodBody,
            Expression initializer) {
        // Create the variable declaration
        // TODO store it for later when we need to look back and call the constructor?
        //
        ExpressionStmt expressionStmt = new ExpressionStmt();
        //
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
        VariableDeclarator variableDeclarator = new VariableDeclarator();
        variableDeclarator.setName(variableName);
        // TODO This might require some massage...
        variableDeclarator.setType(new ClassOrInterfaceType(variableType));
        // Default to null. We might revise this later when we get to the constructor
        variableDeclarator.setInitializer(initializer);
        MethodCallExpr me = new MethodCallExpr();
        //
        NodeList<VariableDeclarator> variableDeclarators = new NodeList<>();
        variableDeclarators.add(variableDeclarator);
        variableDeclarationExpr.setVariables(variableDeclarators);
        expressionStmt.setExpression(variableDeclarationExpr);
        methodBody.addStatement(expressionStmt);

        return variableName;
    }

    private String firstLetterToLowerCase(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public void setFilterMethods(List<String> filterMethods) {
        this.filterMethods.addAll(filterMethods);
    }

}
