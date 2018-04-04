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
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;

/**
 * Ensures that for each invokeExpr its parameters are plain LOCAL. TODO not
 * sure about arrays tho !
 * 
 * TODO Try to see if "Boxing" primitives works already here !!!
 * 
 * @author gambi
 *
 */
public class PrepareInvocationParameters extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(PrepareInvocationParameters.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {

		final SootMethod containerMethod = body.getMethod();

		System.out.println("PrepareInvocationParameters.internalTransform() STARTING " + containerMethod);

		if (InstrumentTracer.filterMethod(containerMethod)) {
			logger.debug("Skip instrumentation of: " + containerMethod);
			return;
		}

		if (InstrumentTracer.isExternalLibrary(containerMethod)) {
			// Probably we can do something about it
			logger.debug("Skip instrumentation of: " + containerMethod);
			return;
		}

		final PatchingChain<Unit> units = body.getUnits();
		for (final Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {

			final Unit unit = iter.next();

			unit.apply(new AbstractStmtSwitch() {

				public void caseAssignStmt(AssignStmt stmt) {
					if (stmt.containsInvokeExpr()) {
						processInvokeExpr(body, unit, stmt.getInvokeExpr());
					}
				}

				@Override
				public void caseInvokeStmt(InvokeStmt stmt) {
					processInvokeExpr(body, unit, stmt.getInvokeExpr());
				}

			});
		}
	}

	private void processInvokeExpr(Body body, Unit currentPosition, InvokeExpr invokeExpr) {
		int count = invokeExpr.getArgCount();
		logger.trace("Process " + invokeExpr + " with " + count + " parameters");
		if (count == 0) {
			return;
		} else {
			// We need to process them in order so we can replace them in the
			// right way
			for (int position = 0; position < count; position++) {
				if (invokeExpr.getArg(position) instanceof Local) {
					continue;
				} else {
					Value originalParameter = invokeExpr.getArg(position);
					Type parameterType = null;
					if (originalParameter == null || originalParameter instanceof NullConstant) {
						parameterType = invokeExpr.getMethod().getParameterType(position);
					} else {
						parameterType = originalParameter.getType();
					}
					// Some arguments are null !
					Local parameterLocal = UtilInstrumenter.generateFreshLocal(body, parameterType);
					// Note that THIS might not work for ArrayRef, FieldRef and
					// such... in that case add code to extract those !
					AssignStmt assignParameters = Jimple.v().newAssignStmt(parameterLocal, originalParameter);
					body.getUnits().insertBefore(assignParameters, currentPosition);
					// Replace the parameter with the local
					invokeExpr.setArg(position, parameterLocal);
					logger.info("Replaced argument " + originalParameter + " with " + parameterLocal + " in position "
							+ position + " for " + currentPosition);
				}
			}
		}

	}
}
