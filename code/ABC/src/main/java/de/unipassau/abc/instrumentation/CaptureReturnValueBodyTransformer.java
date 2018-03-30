package de.unipassau.abc.instrumentation;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.VoidType;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;

/**
 * In case of calls like "if( a == b.foo()){" soot is smart enough to extract
 * the locals and place them back in the if-conditions
 * 
 * @author gambi
 *
 */
public class CaptureReturnValueBodyTransformer extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(CaptureReturnValueBodyTransformer.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {

		final SootMethod containerMethod = body.getMethod();

		System.out.println("CaptureReturnValueBodyTransformer.internalTransform() STARTING " + containerMethod);
		

		if (InstrumentTracer.filterMethod(containerMethod)) {
			logger.debug("Skip instrumentation of: " + containerMethod);
			return;
		}

		if (InstrumentTracer.isExternalLibrary(containerMethod)) {
			logger.debug("Skip instrumentation of: " + containerMethod);
			return;
		}

		final PatchingChain<Unit> units = body.getUnits();
		for (final Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {

			final Unit u = iter.next();

			u.apply(new AbstractStmtSwitch() {

				/**
				 * Force the return value of this call to be assigned. Basically
				 * replace the InvokeStmt with an AssignStmt unless the invoke
				 * returns void
				 */
				@Override
				public void caseInvokeStmt(InvokeStmt stmt) {
					logger.trace("Processing " + stmt);
					InvokeExpr invokeExpr = stmt.getInvokeExpr();

					if (invokeExpr.getMethod().getReturnType() instanceof VoidType) {
						return;
					} else {
						Local returnValue = UtilInstrumenter.generateFreshLocal(body,
								invokeExpr.getMethod().getReturnType());
						Unit capturingAssignStmt = Jimple.v().newAssignStmt(returnValue, invokeExpr);
						units.insertBefore(capturingAssignStmt, stmt);
						units.remove(stmt);
						// Remove the original call !
						logger.info("Replace original Invoke " + stmt + " with an assignemnt " + capturingAssignStmt);
					}
				}
			});
		}

	}
}
