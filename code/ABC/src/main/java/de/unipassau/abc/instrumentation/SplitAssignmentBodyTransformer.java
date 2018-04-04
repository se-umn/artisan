package de.unipassau.abc.instrumentation;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.NullType;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;

public class SplitAssignmentBodyTransformer extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(SplitAssignmentBodyTransformer.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {

		final SootMethod containerMethod = body.getMethod();

		System.out.println("SplitAssignmentBodyTransformer.internalTransform() >>> STARTING " + containerMethod);

		// Filter methods that we do not want to instrument
		// TODO Make this parameteric
		// FOR EXAMPLE external Library
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

			final Unit u = iter.next();

			u.apply(new AbstractStmtSwitch() {

				/**
				 * Split the assignment in two parts such that the left side,
				 * which receives the result of the invoke is always a LOCAL,
				 * and the original receiver is always assigned to that local
				 * 
				 * A first part which ALWAYS results in assigning a value to a
				 * LOCAL
				 * 
				 * An invokation -> local
				 * 
				 * A second part which ALWAYS results in STORING a value from a
				 * LOCAL
				 * 
				 * An assignemnt <- local
				 * 
				 * 
				 * 
				 */
				public void caseAssignStmt(AssignStmt stmt) {

					logger.trace("Processing " + stmt);
					/*
					 * Introduce the artificial local unless the left side is
					 * already a LOCAL
					 */
					Value leftOp = stmt.getLeftOp();

					if (leftOp instanceof Local) {
						return;
					} else {
						// This code does not work if leftOp is a local and the
						// rightOp is NULL and we are inside the <init>, which
						// basically corresponds to not initialized a field in a
						// class.

						if (leftOp instanceof FieldRef && stmt.getRightOp() instanceof NullConstant && //
						(containerMethod.getSignature().contains("void <init>")
								|| containerMethod.getSignature().contains("void <clinit>"))) {
							logger.info("Skip Split for " + stmt + " in " + containerMethod.getSignature());
							return;
						}

						Type localType = null;
						if(stmt.getRightOp().getType() instanceof NullType ){
							localType = stmt.getLeftOp().getType();
						}  else {
							localType = stmt.getRightOp().getType();
						}
						
						
						// Introduce the new Local and update the assignment
						Local split = UtilInstrumenter.generateFreshLocal(body, localType);
						stmt.setLeftOp(split);
						logger.info("Swap " + leftOp + " with " + split);

						// Now we handle the assignment to the original leftOp
						AssignStmt assignFromSplit = Jimple.v().newAssignStmt(leftOp, split);
						units.insertAfter(assignFromSplit, u);
						logger.info("Insert new assignFromSplit " + assignFromSplit + " after " + u);
					}

				}
			});
		}

	}
}
