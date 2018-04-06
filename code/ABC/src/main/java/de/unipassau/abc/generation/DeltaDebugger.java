package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.instrumentation.UtilInstrumenter;
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

public class DeltaDebugger {

	public static void minimize(final Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase) {
		// Generate the soot units to validate the given test
		MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();
		System.out.println("DeltaDebugger.minimize() mut is " + mut );
		List<Unit> validation = generateValidationUnit(carvedTestCase.getThird(), mut.getXmlDumpForOwner(),
				mut.getXmlDumpForReturn());
		
		System.out.println("DeltaDebugger.minimize() Validation unit for " + carvedTestCase.getThird() + "\n"
				+ validation );

	}

	public static List<Unit> generateValidationUnit(final SootMethod testMethod, final String xmlOwner,
			final String xmlReturnValue) {

		final List<Unit> validationUnits = new ArrayList<>();

		final Body body = testMethod.getActiveBody();
		// The very last unit is the "return" we need the one before it...
		PatchingChain<Unit> units = body.getUnits();
		final Unit methodUnderTest = units.getPredOf( units.getLast());

		System.out.println("DeltaDebugger.generateValidationUnit() for " + methodUnderTest);
		methodUnderTest.apply(new AbstractStmtSwitch() {

			final SootMethod xmlDumperVerify = Scene.v()
					.getMethod("<de.unipassau.abc.tracing.XMLDumper: void verify(java.lang.Object,java.lang.String)>");

			// return value
			public void caseAssignStmt(soot.jimple.AssignStmt stmt) {
				InvokeExpr invokeExpr = stmt.getInvokeExpr();

				if (!(invokeExpr instanceof StaticInvokeExpr)) {
					generateValidationForCut(((InstanceInvokeExpr) invokeExpr).getBase());
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
				// Object -> actualValue = this is LOCAL by design but might
				// require boxing !
				// Pair<Value, List<Unit>> tmpArgsListAndInstructions =
				// UtilInstrumenter
				// .generateParameterArray(RefType.v("java.lang.Object"),
				// parameterList, body);
				// Returns a Pair which contains the array and the instructions
				// to
				// create it
				Value wrappedValue = UtilInstrumenter.generateCorrectObject(body, value, validationUnits);
				generateValidationForValue(wrappedValue, xmlReturnValue, validationUnits);
			}

			private void generateValidationForCut(Value value) {
				generateValidationForValue(value, xmlOwner, validationUnits);
			}

			// This expects Objects as values, not primitives... I hope that's
			// not a problem ...
			private void generateValidationForValue(Value value, String xmlFile, List<Unit> validationUnits) {

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

}
