package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;

public class StringConstantBoxingBodyTransformer extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(InstrumentTracer.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {
		final SootMethod containerMethod = body.getMethod();

		System.out.println("StringConstantBoxingBodyTransformer.internalTransform() >>> STARTING " + containerMethod);

		final PatchingChain<Unit> units = body.getUnits();
		for (final Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {

			final Unit currentStatement = iter.next();

			currentStatement.apply(new AbstractStmtSwitch() {
				public void caseAssignStmt(AssignStmt stmt) {
					if (!stmt.containsInvokeExpr()) {
						// Look only for method calls which have ConstantString
						// as Parameters
						return;
					}
					InvokeExpr invokeExpr = stmt.getInvokeExpr();

					int[] positionsOfStringConstants = new int[invokeExpr.getArgs().size()];
					Map<Integer, Local> extractedStrings = new HashMap<>();
					List<Unit> toInsert = new ArrayList<Unit>();

					for (int i = 0; i < positionsOfStringConstants.length; i++) {
						if (invokeExpr.getArg(i) instanceof StringConstant) {
							System.out.println("StringConstantBoxingBodyTransformer FOUDN STRING CONST in position " + i
									+ " as PARAMETER FOR " + stmt + " inside " + containerMethod);
							Local l = UtilInstrumenter.generateFreshLocal(body, RefType.v("java.lang.String"));
							extractedStrings.put(new Integer(i), l);
							toInsert.add(Jimple.v().newAssignStmt(l, invokeExpr.getArg(i)));
						}
					}
					if (toInsert.isEmpty()) {
						return;
					}
					// Put the assignments before the method
					units.insertBefore(toInsert, currentStatement);
					// add a new call to the original method with the locals
					for (Entry<Integer, Local> extractedString : extractedStrings.entrySet()) {
						System.out.println("Replacing parameter in call ");
						invokeExpr.setArg(extractedString.getKey().intValue(), extractedString.getValue());
					}
					// remove the original method
					// units.remove( currentStatement );

					// // Not sure why this should happen is give
					// "containsArrayRef" ? Probably to rule ou
					// Value rigthValue = stmt.getRightOp();
					// // Replace Constanst with Constant.toString();
					// if (rigthValue instanceof StringConstant) {
					// System.out.println("UtilInstrumenter.generateParameterArray()
					// STRING CONSTANT " + parameterList.get(i).getType());
					//
					// Local stringLocal = generateFreshLocal(body,
					// parameterList.get(i).getType());
					//
					//// SootClass sootClass =
					// Scene.v().getSootClass("java.lang.String");
					//// // <java.lang.String: java.lang.String toString()>
					//// for( SootMethod m : sootClass.getMethods() ){
					//// System.out.println("UtilInstrumenter.generateParameterArray()
					// " + m.getSignature());
					////
					//// }
					//// SootMethod valueOfMethod =
					// Scene.v().getMethod("<java.lang.String: java.lang.String
					// format(java.lang.String,java.lang.Object[])>");
					//// List<Value> parameters = new ArrayList<>();
					//// parameters.add( StringConstant.v("\"%s\""));
					//// parameters .add( parameterList.get(i));
					//// StaticInvokeExpr staticInvokeExpr =
					// Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(),
					// parameters);
					//// Unit newStringAssignStmt =
					// Jimple.v().newAssignStmt(stringLocal, staticInvokeExpr);
					// Unit newStringAssignStmt =
					// Jimple.v().newAssignStmt(stringLocal,
					// parameterList.get(i));
					// generated.add(newStringAssignStmt );
					//
					// rightSide = stringLocal;
					//
					//
					//
					// }
				}
			});

		}
	}

}
