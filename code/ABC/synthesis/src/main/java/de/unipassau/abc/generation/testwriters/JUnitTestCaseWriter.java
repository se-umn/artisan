package de.unipassau.abc.generation.testwriters;

import static com.github.javaparser.StaticJavaParser.parseClassOrInterfaceType;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodCallLiteralValue;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.PrimitiveValue;
import de.unipassau.abc.generation.SyntheticMethodSignatures;
import de.unipassau.abc.generation.TestCaseWriter;
import de.unipassau.abc.generation.data.AndroidCarvedTest;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.data.CatchBlock;
import de.unipassau.abc.generation.utils.TestCase;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TypeUtils;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate the source code implementing the carved tests as tests were
 * implemented using JUnit. For each
 * 
 * @author gambitemp
 *
 */
public class JUnitTestCaseWriter implements TestCaseWriter {

	private static final Logger logger = LoggerFactory.getLogger(JUnitTestCaseWriter.class);

	@Override
	public void write(File outputFolder, TestCaseOrganizer testOrganizer, CarvedTest... carvedTests)
			throws IOException {
		// Group Carved Tests into Test Cases and generate the classes
		Set<CompilationUnit> junitTestClasses = new HashSet<>();

		for (TestCase testCase : testOrganizer.organize(carvedTests)) {
			junitTestClasses.add(generateJUnitTestCase(testCase));
		}

		// TODO Some post processing? Like wrapping together @before methods or
		// something...

		for (CompilationUnit junitTestClass : junitTestClasses) {
			File packageFolder = null;
			if (junitTestClass.getPackageDeclaration().isPresent()) {
				packageFolder = new File(outputFolder,
						junitTestClass.getPackageDeclaration().toString().replaceAll("\\.", File.separator));
			} else {
				packageFolder = outputFolder;
			}
			File outputJavaFile = new File(packageFolder, "className" + ".java");

			// TODO Add Pretty print? Change Charser?
			Files.write(outputJavaFile.toPath(), junitTestClass.toString().getBytes(StandardCharsets.UTF_16));
		}

	}

	public CompilationUnit generateJUnitTestCase(TestCase testCase) {
		logger.info("Generate source code for " + testCase.getName());

		CompilationUnit cu = new CompilationUnit();

		cu.setPackageDeclaration(testCase.getPackageName());

		ClassOrInterfaceDeclaration testClass = cu.addClass(testCase.getName());

		testClass.setModifiers(Modifier.Keyword.PUBLIC);

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
		}

		// TODO Create imports

		AtomicInteger testId = new AtomicInteger(1);

		for (CarvedTest carvedTest : testCase.getCarvedTests()) {
			// TODO Name tests
			MethodDeclaration testMethod = testClass.addMethod("test" + testId.getAndIncrement(),
					Modifier.Keyword.PUBLIC);
			// Annotate the method with JUnit @Test, add time out
			NormalAnnotationExpr annotation = testMethod.addAndGetAnnotation(Test.class);
			MemberValuePair timeout = new MemberValuePair("timeout", new NameExpr("4000"));
			annotation.getPairs().add(timeout);
			// Add the generic Exception throwing
			testMethod.addThrownException(Exception.class);

			// Generate method body
			generateMethodBody(testMethod, carvedTest);
		}

		return cu;
	}

	// TODO Those are scoped by the method, not the test !!
	// Keep track of the variables declared so far.
	private Map<DataNode, String> declaredVariables = new HashMap<>();

	// Simple Index to name variables by their class name
	private Map<String, AtomicInteger> declaredVariablesIndex = new HashMap<>();

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
	public BlockStmt generateBlockStmtFrom(ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {
		BlockStmt blockStmt = new BlockStmt();
		// TODO First declare all the variables in the scope of this block ?
		Set<ObjectInstance> objectInstances = new HashSet<ObjectInstance>(dataDependencyGraph.getObjectInstances());
		for (ObjectInstance objectInstance : objectInstances) {
			// Default initialization. Avoid variable not initialize warning/errors
			declareVariableFor(objectInstance, blockStmt, new NullLiteralExpr());
		}

		// Next implement all the methods, making sure variables and parameters match
		for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
			if (methodInvocation.isConstructor()) {
				generateConstructorCall(methodInvocation, blockStmt);
			} else {
				generateMethodCall(methodInvocation, blockStmt);
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

		BlockStmt cb = generateBlockStmtFrom(catchBlock.getExecutionFlowGraph(), catchBlock.getDataDependencyGraph());
		catchClause.setBody(cb);
		return catchClause;
	}

	private void generateMethodBody(MethodDeclaration testMethod, CarvedTest carvedTest) {
		BlockStmt methodBody = generateBlockStmtFrom(carvedTest.getExecutionFlowGraph(),
				carvedTest.getDataDependencyGraph());

		if (carvedTest.expectException()) {
			// Build the try statement that encapsulate the entire method body
			TryStmt tryStatement = new TryStmt();
			tryStatement.setTryBlock(methodBody);

			CatchBlock expectedExceptionCatchBlock = carvedTest.getExpectedExceptionCatchBlock();
			CatchClause expectedExceptionCatchClause = generateCatchClause(expectedExceptionCatchBlock, "expected");

			CatchBlock unexpectedExceptionCatchBlock = carvedTest.getUnexpectedExceptionCatchBlock();
			CatchClause unexpectedExceptionCatchClause = generateCatchClause(unexpectedExceptionCatchBlock,
					"unexpected");

			NodeList<CatchClause> multiCatchBlock = new NodeList<>();
			multiCatchBlock.add(expectedExceptionCatchClause);
			multiCatchBlock.add(unexpectedExceptionCatchClause);
			//
			tryStatement.setCatchClauses(multiCatchBlock);

			BlockStmt wrappedMethodBody = new BlockStmt();
			wrappedMethodBody.addStatement(tryStatement);
			testMethod.setBody(wrappedMethodBody);

		} else {
			testMethod.setBody(methodBody);
		}

	}

	private void generateConstructorCall(MethodInvocation methodInvocation, BlockStmt methodBody) {
		/*
		 * Despite it might seem better to update the previous variable declaration,
		 * doing so it is tricky as arguments used in the constructor must be
		 * initialized and set before use so we simply reassign the variable...
		 */
		ObjectInstance owner = methodInvocation.getOwner();
		String variableName = getVariableFor(owner, methodBody);

		// TODO No idea what scope is
		ClassOrInterfaceType type = new ClassOrInterfaceType(null,
				JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()));
		ObjectCreationExpr constructorCall = new ObjectCreationExpr();
		constructorCall.setType(type);
		for (DataNode parameter : methodInvocation.getActualParameterInstances()) {
			constructorCall.addArgument(getParameterFor(parameter, methodBody));
		}

		AssignExpr variableAssignemt = new AssignExpr(new NameExpr(variableName), constructorCall, Operator.ASSIGN);
		methodBody.addStatement(variableAssignemt);

	}

	private void generateMethodCall(MethodInvocation methodInvocation, BlockStmt methodBody) {

		// TODO Handle return type that must be assigned to a variable probably
		String methodName = JimpleUtils.getMethodName(methodInvocation.getMethodSignature());
		MethodCallExpr methodCallExpr = null;

		if (!methodInvocation.isStatic()) {
			// Instance method calls
			ObjectInstance owner = methodInvocation.getOwner();
			String variableName = getVariableFor(owner, methodBody);

			if (methodInvocation.isSynthetic()) {
				throw new NotImplementedException("Binding not defined for synthetic method call: " + methodInvocation);
			} else {
				NameExpr nameExpr = new NameExpr(variableName);
				methodCallExpr = new MethodCallExpr(nameExpr, methodName);
			}

		} else {
			// Static methods
			// Include the Type, but shoud we use TypeExpr instead?
			if (methodInvocation.isSynthetic()) {
				switch (methodInvocation.getMethodSignature()) {
				case SyntheticMethodSignatures.FAIL_WITH_MESSAGE:
					methodCallExpr = new MethodCallExpr(
							JimpleUtils.getFullyQualifiedMethodName("<org.junit.Assert: void fail(java.lang.String)>"));
					break;
				default:
					throw new NotImplementedException(
							"Binding not defined for synthetic method call: " + methodInvocation);
				}
			} else {
				methodCallExpr = new MethodCallExpr(
						JimpleUtils.getFullyQualifiedMethodName(methodInvocation.getMethodSignature()));
			}
		}

		for (DataNode parameter : methodInvocation.getActualParameterInstances()) {
			methodCallExpr.addArgument(getParameterFor(parameter, methodBody));
		}

		// If a method is exceptional it cannot return a value despite it is non-void
		if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())
				&& !methodInvocation.isExceptional()) {
			// Generate the left side of the invocation. We store values into new variables
			// no matter what ?
			declareVariableFor(methodInvocation.getReturnValue(), methodBody, methodCallExpr);
		} else {
			methodBody.addStatement(methodCallExpr);
		}

	}

	/**
	 * Either return the variable corresponding to the parameter or the value of
	 * that's a primitive type
	 * 
	 * @param parameter
	 * @return
	 */
	private String getParameterFor(DataNode dataNode, BlockStmt methodBody) {
		if (dataNode instanceof PrimitiveValue) {

			if (Class.class.getName().equals(((PrimitiveValue) dataNode).getType())) {
				return ((PrimitiveValue) dataNode).getStringValue();
			} else {
				/*
				 * We use toString() instead of getStringValue() to wrap strings which have two
				 * different representations.
				 */
				return ((PrimitiveValue) dataNode).toString();
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

	// TODO Contenxtualize on MethodBody/scope !
	private void declareVariableFor(DataNode dataNode, BlockStmt methodBody, Expression initializer) {
		String variableType = TypeUtils.getActualTypeFor(dataNode);
		// TODO Move to Utils methods as this is used in many places
		String className = variableType.substring(variableType.lastIndexOf('.') + 1, variableType.length());

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
		declaredVariables.put(dataNode, variableName);

		// Link all the alias to the same variable if any
		// TODO Why there are aliases here? Super/sub type? Maybe this can be resolved
		// directly from the CarvedTest
//		if (dataNode instanceof ObjectInstance) {
//			for (ObjectInstance alias : dataDependencyGraph.getAliasesOf((ObjectInstance) dataNode)) {
//				declaredVariables.put(alias, variable);
//			}
//		}

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
		//
		NodeList<VariableDeclarator> variableDeclarators = new NodeList<>();
		variableDeclarators.add(variableDeclarator);
		variableDeclarationExpr.setVariables(variableDeclarators);
		expressionStmt.setExpression(variableDeclarationExpr);
		methodBody.addStatement(expressionStmt);
	}

}
