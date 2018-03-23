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
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
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
					// For invocations which take a string constant parameter
					// insert a string variable which is initialized with the
					// original constant
					
					System.out.println(">>> " +stmt + " " + stmt.getClass()) ;
					
					if (stmt.containsInvokeExpr()) {
						InvokeExpr invokeExpr = stmt.getInvokeExpr();

						int[] positionsOfStringConstants = new int[invokeExpr.getArgs().size()];
						Map<Integer, Local> extractedStrings = new HashMap<>();
						List<Unit> toInsert = new ArrayList<Unit>();

						for (int i = 0; i < positionsOfStringConstants.length; i++) {
							if (invokeExpr.getArg(i) instanceof StringConstant) {
								System.out.println("StringConstantBoxingBodyTransformer FOUND STRING CONST in position "
										+ i + " as PARAMETER FOR " + stmt + " inside " + containerMethod);
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
					} else if ( stmt.containsArrayRef() ){
						// For assignments of string constant to arrays insert a
						// string variable which is initialized with the original constant
						
						// Is the array the leftOp -> STORE and a String the rightOp ?
						if( stmt.getLeftOp() instanceof ArrayRef && stmt.getRightOp() instanceof StringConstant ){
							ArrayRef cell = (ArrayRef) stmt.getLeftOp();
							List<Unit> toInsert = new ArrayList<Unit>();
							System.out.println("StringConstantBoxingBodyTransformer FOUDN STRING CONST assigned to ARRAY element  " + cell.getBase() + "["+cell.getIndex()+"]"); 
							Local l = UtilInstrumenter.generateFreshLocal(body, RefType.v("java.lang.String"));
							// Assign the constant to the new local:
							toInsert.add(Jimple.v().newAssignStmt(l, stmt.getRightOp()));
							
							units.insertBefore(toInsert, currentStatement);
							
							// Now replace the right elements in the original assignment
							stmt.setRightOp( l );
						}
					}
				}
			
			
				/**
				 * This gets invoked for those calls which do not return anything (void), such as <init>
				 * and calls whose return type is not assigned
				 */
				@Override
				public void caseInvokeStmt(InvokeStmt stmt) {
					System.out.println("caseInvokeStmt >> " + stmt );
					
					InvokeExpr invokeExpr = stmt.getInvokeExpr();

					int[] positionsOfStringConstants = new int[invokeExpr.getArgs().size()];
					Map<Integer, Local> extractedStrings = new HashMap<>();
					List<Unit> toInsert = new ArrayList<Unit>();

					for (int i = 0; i < positionsOfStringConstants.length; i++) {
						if (invokeExpr.getArg(i) instanceof StringConstant) {
							System.out.println("StringConstantBoxingBodyTransformer FOUND STRING CONST in position "
									+ i + " as PARAMETER FOR " + stmt + " inside " + containerMethod);
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
					
					super.caseInvokeStmt(stmt);
				}
			});

		}
	}

}
