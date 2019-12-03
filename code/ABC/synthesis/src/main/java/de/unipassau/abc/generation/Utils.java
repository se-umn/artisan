package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.instrumentation.UtilInstrumenter2;
import soot.Body;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;

public class Utils {

	private final static Logger logger = LoggerFactory.getLogger(Utils.class);

	// create a @Before method which invokes resetEnvironment by unless there's
	// already one. TODO in that case what we do ?!
	public static void createAtBeforeResetMethod(String resetEnvironmentBy, CompilationUnit testClass) {
		if (resetEnvironmentBy == null) {
			return;
		}
		//
		TypeDeclaration<?> testType = testClass.getTypes().get(0);
		for (MethodDeclaration md : testType.getMethods()) {
			if (md.isAnnotationPresent(Before.class)) {
				return;
			}
		}
		//
		MethodDeclaration setupMethod = testType.addMethod("setup", Modifier.PUBLIC);
		setupMethod.addAnnotation(Before.class);
		setupMethod.addThrownException(Exception.class);
		BlockStmt body = new BlockStmt();
		body.addStatement(new NameExpr(resetEnvironmentBy));
		setupMethod.setBody(body);

	}
	
	public static void removePureMethods(CompilationUnit testClass) {
		testClass.accept(new ModifierVisitor<Void>() {

			// This is a void or a method call whose return value is not
			// assigned to anyone
			@Override
			public Visitable visit(MethodCallExpr n, Void arg) {
				if (isPure(n)) {
					logger.info(" Remove pure method call " + n);
					return null;
				}
				return super.visit(n, arg);
			}

		}, null);

	}

	// Purity methods are checked for Method Calls, that is calls to methods
	// whose value is not assigned !
	public static boolean isPure(MethodCallExpr methodCall) {
		// Those are removed ONLY if the value they return is NOT used !
		// This includes ALSO the name of the variable
		String m = methodCall.getNameAsString();

		return m.equals("length(") || m.equals("startsWith(") || m.equals("endsWith(") || m.equals("lastIndexOf(")
				|| m.equals("equals(") || m.equals("trim(") || // String
				m.equals("getAutoCommit(") || // Sql Connection
				m.equals("getResultSet(") || // Sql statement
				m.equals("getInt(") || // Sql result set
				m.equals("size(") || // List/Collections
				// TODO Check the pure method is not also the MUT !
				// m.equals("getPrice") || m.equals("getTotalPrice") ||
				// m.equals("getAdults") || m.equals("getChildren")
				// || m.equals("getMaxOccupancy") || m.equals("getRoomID") ||
				// m.equals("getFname") || // --> This leads to breaking the
				// tests? I suspect that this is removed ALSO in the assignmStmt
				// m.equals("getTotalPrice") || // HotelMe
				// "getCheckOut", getCheckIn
				false;

	}

	// Return the files !
	public static Set<File> storeToFile(Set<CompilationUnit> generatedTestClasses, File outputDir) throws IOException {
		logger.info("Storing tests to " + outputDir.getAbsolutePath());

		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new RuntimeException("Output dir " + outputDir.getAbsolutePath() + " cannot be created ");
		}

		Set<File> stored = new HashSet<>();

		for (CompilationUnit testClass : generatedTestClasses) {
			// Store to file
			File packageDir = new File(outputDir,
					testClass.getPackageDeclaration().get().getNameAsString().replaceAll("\\.", File.separator));
			packageDir.mkdirs();
			File classFile = new File(packageDir, testClass.getType(0).getNameAsString() + ".java");
			//
			Files.write(classFile.toPath(), testClass.toString().getBytes(), StandardOpenOption.CREATE_NEW);
			//
			stored.add(classFile);
		}

		return stored;

	}

	public static List<Unit> generateValidationUnit(final SootMethod testMethod, final String xmlOwner,
			final String xmlReturnValue, //
			final Value primitiveReturnValue) {

		final List<Unit> validationUnits = new ArrayList<>();

		final Body body = testMethod.getActiveBody();
		// The very last unit is the "return" we need the one before it...
		PatchingChain<Unit> units = body.getUnits();

		final Unit methodUnderTest = units.getPredOf(units.getLast());

		// System.out.println("DeltaDebugger.generateValidationUnit() for " +
		// methodUnderTest);
		methodUnderTest.apply(new AbstractStmtSwitch() {

			final SootMethod xmlDumperVerify = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.XMLVerifier: void verify(java.lang.Object,java.lang.String)>");

			final SootMethod xmlDumperVerifyPrimitive = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.XMLVerifier: void verifyPrimitive(java.lang.Object,java.lang.String)>");

			// return value
			public void caseAssignStmt(soot.jimple.AssignStmt stmt) {

				// In some case ? the right side of the stmt is not an
				// invocation (e.g., arrayref)

				System.out.println("Validation for " + stmt);

				if (stmt.containsInvokeExpr()) {

					InvokeExpr invokeExpr = stmt.getInvokeExpr();

					if (!(invokeExpr instanceof StaticInvokeExpr)) {
						generateValidationForCut(((InstanceInvokeExpr) invokeExpr).getBase());
					}
				}
				generateValidationForReturnValue(stmt.getLeftOp());
			};

			// No Return value
			public void caseInvokeStmt(soot.jimple.InvokeStmt stmt) {
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				if (stmt.containsInvokeExpr()) {
					if (!(stmt.getInvokeExpr() instanceof StaticInvokeExpr)) {
						// Extract OWNER
						generateValidationForCut(((InstanceInvokeExpr) invokeExpr).getBase());
					}
				}
			}

			private void generateValidationForReturnValue(Value value) {
				Value wrappedValue = UtilInstrumenter2.generateCorrectObject(body, value, validationUnits);
				if (JimpleUtils.isPrimitive(value.getType())) {
					generateValidationForPrimitiveValue(wrappedValue, primitiveReturnValue, validationUnits);
				} else {
					generateValidationForValue(wrappedValue, xmlReturnValue, validationUnits);
				}
			}

			private void generateValidationForCut(Value value) {
				generateValidationForValue(value, xmlOwner, validationUnits);
			}

			private void generateValidationForPrimitiveValue(Value actualValue, Value expectedValue,
					List<Unit> validationUnits) {

				List<Value> methodStartParameters = new ArrayList<Value>();
				methodStartParameters.add(actualValue);

				// Wrap everythign into a String ! Why ? Can't I simply box the
				// expected Value as well, because this will be invoked on java
				// and fail !
				methodStartParameters.add(StringConstant.v(expectedValue.toString()));

				// Create the invocation object
				validationUnits.add(Jimple.v().newInvokeStmt(
						Jimple.v().newStaticInvokeExpr(xmlDumperVerifyPrimitive.makeRef(), methodStartParameters)));
			};

			private void generateValidationForValue(Value value, String xmlFile, List<Unit> validationUnits) {

				if (xmlFile == null || xmlFile.trim().length() == 0) {
					// it can be a void call
					logger.debug("Null XML File for " + value + " cannot do not create validation call");
					return;
				}

				if (!new File(xmlFile).exists()) {
					logger.warn("Cannot find XML File" + xmlFile + ", do not create validation call");
					return;
				}

				List<Value> methodStartParameters = new ArrayList<Value>();
				methodStartParameters.add(value);
				methodStartParameters.add(StringConstant.v(xmlFile));

				// Create the invocation object
				validationUnits.add(Jimple.v().newInvokeStmt(
						Jimple.v().newStaticInvokeExpr(xmlDumperVerify.makeRef(), methodStartParameters)));
			};
		});

		// This can be a void method -> validate only CUT
		// a static method call -> validate only ReturnValue
		// a static void method call -> no validation needed
		// other -> validate both CUT and ReturnValue

		return validationUnits;
	}

	// TODO Assume tMethodCall are not Assign Expt !
	public static boolean isMandatory(Statement statement) {
		// return m.equals("close") || false;
		if (statement instanceof ExpressionStmt) {
			ExpressionStmt es = (ExpressionStmt) statement;
			Expression e = es.getExpression();
			// Naive
			String exp = e.toString();
			// System.out.println("TestSuiteMinimizer.isMandatory() Processing "
			// + statement );
			// Thos are all hardcoded
			return exp.contains(".close") || //
					exp.contains(".setAutoCommit") || //
					exp.contains("XMLVerifier.verify") || // Include also
															// verifyPrimitives
					exp.contains("= null");
		}
		return false;
	}

	// this requires the SymbolSover to get the type of S
	// A statement might include several calls !

	// Processing a file to extract which external calls are made shall be done
	// only once...
	@SuppressWarnings("unchecked")
	public static boolean isExternalInterface(Statement s) {
		final AtomicBoolean isExternalInterface = new AtomicBoolean(false);
		// System.out.println("Carver.isExternalInterface() " + s);
		s.accept(new VoidVisitorAdapter<JavaParserFacade>() {
			@Override
			public void visit(ExpressionStmt n, JavaParserFacade arg) {
				// TODO Auto-generated method stub
				// System.out.println("visit() ExpressionStmt " + n);
				super.visit(n, arg);
			}

			@Override
			public void visit(AssignExpr n, JavaParserFacade arg) {
				// TODO Auto-generated method stub
				System.out.println("visit() AssignExpr " + n);
				// ResolvedType targetType =
				// javaParserFacade.getType(n.getTarget());
				// ResolvedType valueType =
				// javaParserFacade.getType(n.getValue());
				super.visit(n, arg);
			}

			@Override
			public void visit(MethodCallExpr n, JavaParserFacade arg) {
				// TODO Auto-generated method stub
				// Unless its a static ?
				try {
					// System.out.println("visit() MethodCallExpr " + n + " " +
					// n.getScope().get());
					String typeName = arg.getType(n.getScope().get()).describe();
					// System.out.println("Resolved type: " + typeName);

					if (externalInterfaces.contains(typeName)) {
						// System.out.println("External interface : " +
						// typeName);
						isExternalInterface.set(true);
					}

				} catch (Exception e) {
//					logger.warn("Cannot resolve type for " + n + ". " + e.getMessage());
				}
				super.visit(n, arg);

			}
		}, JavaParserFacade.get(TYPE_SOLVER));

		return isExternalInterface.get();
	}

	// VERY BAD !

	private static TypeSolver TYPE_SOLVER;

	private static void setupTypeSolver(List<File> projectJars) throws IOException {
		// Resolve the missing generics
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());

		for (File jar : projectJars) {
			combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
		}
		TYPE_SOLVER = combinedTypeSolver;
	}

	private static final Set<String> externalInterfaces = new HashSet<>(Arrays.asList(new String[] { //
			// "java.util.Scanner", // Leave the calls to Scanner for the moment
			"java.io.FileWriter", //
			"java.nio.Path", //
			"java.io.BufferedWriter", //
			"java.io.Writer", //
			"java.io.File", //
			"java.nio.Files", //
			"java.sql.Connection", //
			"java.sql.Statement", //
			"java.sql.PreparedStatement", //
			"java.sql.ResultSet", //
	}));
}
