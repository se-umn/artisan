package de.unipassau.abc.instrumentation;

import static de.unipassau.abc.instrumentation.ABCInstrumenter.addTraceObject;
import static de.unipassau.abc.instrumentation.ABCInstrumenter.addTraceStart;
import static de.unipassau.abc.instrumentation.ABCInstrumenter.addTraceStop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.NullType;
import soot.PatchingChain;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.StringConstant;

/**
 * Trace fields assignment, array assignments, string initialization, and the
 * other "corner case" operations into trace actions like:
 * 
 * 
 * Array.SET(Position, Value); <b>We assume that parameters are Locals, that is,
 * we assume that the other runners have already executed.</b>
 * 
 * @author gambi
 *
 */
public class ABCInstrumentArtificialInvocations extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(ABCInstrumentArtificialInvocations.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {
		
		final SootMethod containerMethod = body.getMethod();

		System.out.println("ABCInstrumentArtificialInvocations.internalTransform() STARTING " + containerMethod);

		logger.debug("STRATING " + containerMethod );
		
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

			final Unit currentUnit = iter.next();

			if (currentUnit.hasTag(ABCTag.TAG_NAME)) {
				logger.info("DO NOT TRACE OUR OWN CODE " + currentUnit);
			}
			
			currentUnit.apply(new AbstractStmtSwitch() {

				public void caseAssignStmt(AssignStmt stmt) {
					
					
					if( stmt.containsInvokeExpr() ){
						InvokeExpr invokeExpr = stmt.getInvokeExpr();
						if (InstrumentTracer.doNotTraceCallsTo(invokeExpr.getMethod())) {
							logger.debug("Do not trace calls to " + currentUnit);
							return;
						}
					}

					if (stmt.getLeftOp() instanceof Local) {
						if (stmt.getLeftOp().getType().equals(RefType.v("java.lang.String"))
								&& stmt.getRightOp() instanceof StringConstant) {
							///
							instrumentStringAssignmentExpression(body, currentUnit, (Local) stmt.getLeftOp(),
									(StringConstant) stmt.getRightOp());
						}
					} else if (stmt.getLeftOp() instanceof ArrayRef) {
						ArrayRef arrayRefExp = (ArrayRef) stmt.getLeftOp();

						instrumentArrayElementAssignmentExpression(body, currentUnit,
								//
								arrayRefExp.getBase(), //
								arrayRefExp.getIndex(), //
								stmt.getRightOp());

					} else if (stmt.getLeftOp() instanceof FieldRef) {
						// if ( ! ((FieldRef)
						// stmt.getLeftOp()).getField().isPrivate() ){
						instrumentFieldAssignmentExpression(body, currentUnit, ((FieldRef) stmt.getLeftOp()).getField(),
								stmt.getRightOp());
						// }
					}
					// This one might not be mutually exclusive with the one
					// above...
					if (stmt.getRightOp() instanceof NewArrayExpr) {
						NewArrayExpr right = (NewArrayExpr) stmt.getRightOp();
						instrumentArrayInitExpression(body, currentUnit,
								//
								stmt.getLeftOp(), //
								right.getSize());
					} else if (stmt.getLeftOp() instanceof Local && //
					stmt.getRightOp() instanceof StaticFieldRef) {
						instrumentAccessToStaticFieldExpression(body, currentUnit,
								//
								(Local) stmt.getLeftOp(), (StaticFieldRef) stmt.getRightOp());
					} else if (stmt.getRightOp() instanceof StaticFieldRef) {
						logger.debug("stmt.getRightOp() instanceof StaticFieldRef >> Operation not supported " + stmt);
					} else if (stmt.getRightOp() instanceof ArrayRef) {
						instrumentArrayAccessExpression(body, currentUnit,
								//
								stmt.getLeftOp(), 
								((ArrayRef) stmt.getRightOp()).getBase(),
								((ArrayRef) stmt.getRightOp()).getIndex());
					} else if ( stmt.getRightOp() instanceof ClassConstant ){
						instrumentAccessToClassConstant(body, currentUnit, 
								//
								stmt.getLeftOp(), 
								((ClassConstant) stmt.getRightOp()));
					} else {
						// ClassConstant left and right ...
						logger.debug("Uknown combination. Operation not supported " + stmt.getLeftOp() + " " + stmt.getRightOp());
					}
				}

			});
		}
	}
	
	private void instrumentAccessToClassConstant(Body body, Unit currentUnit, Value leftOp,
			ClassConstant classConstant) {

		String invokeType = "ClassOperation";
		// "Static" - This corresponds to <Instance>.class - Maybe embed the actual class in the Action?
		String fakeMethodSignature = "<abc.Class: java.lang.Class get()>";
		//
		List<Value> parameterList = new ArrayList<>();
//		parameterList.add(StringConstant.v(classConstant.value));
		//
		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();
		//
		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));
		/*
		 * This avoid duplicating the access to the static field
		 */
		// Static method have null - TODO This will break it ?
		generatedAfter.addAll(addTraceStop(fakeMethodSignature, NullConstant.v(), classConstant, body));
		//
		// // TAG OUR CODE ?
		PatchingChain<Unit> units = body.getUnits();
		units.insertBefore(generatedBefore, currentUnit);
		units.insertAfter(generatedAfter, currentUnit);
		
	}
	// TODO How DO I KNOW THIS IS SYSTEM.OUT, SYSTEM.ERR, or SYSTEM.IN ?! ?
	private void instrumentAccessToStaticFieldExpression(Body body, Unit currentUnit, Local staticFieldLocal,
			StaticFieldRef staticField) {
		String invokeType = "StaticFieldOperation";
		// "Static"
		String fakeMethodSignature = "<abc.StaticField: " + staticField.getType() + " get(java.lang.String)>";
		//
		List<Value> parameterList = new ArrayList<>();
		parameterList.add(StringConstant
				.v(staticField.getField().getDeclaringClass().getName() + "." + staticField.getField().getName()));
		//
		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();
		//
		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));
		/*
		 * This avoid duplicating the access to the static field
		 */
		generatedAfter.addAll(addTraceStop(fakeMethodSignature, NullConstant.v(), staticFieldLocal, body));
		//
		// // TAG OUR CODE ?
		PatchingChain<Unit> units = body.getUnits();
		units.insertBefore(generatedBefore, currentUnit);
		units.insertAfter(generatedAfter, currentUnit);
	}

	private void instrumentFieldAssignmentExpression(Body body, Unit currentUnit, //
			SootField field, Value value) {
		// TODO Note that we do not even need to know which field is...
		String invokeType = "FieldOperation";
		
		Type fieldType = null;
		if( value == null || value instanceof NullConstant || value.getType() instanceof NullType){
			fieldType = field.getType();
		} else {
			fieldType = value.getType();
		}
		
		// "Static"
		String fakeMethodSignature = "<abc.Field: void set(" + fieldType + ")>";

		List<Value> parameterList = new ArrayList<>();
		parameterList.add(value);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));

		// FIXME Not sure how to handle this.
		generatedAfter.addAll(addTraceStop(fakeMethodSignature, NullConstant.v(), value, body));

		PatchingChain<Unit> units = body.getUnits();
		units.insertBefore(generatedBefore, currentUnit);
		units.insertAfter(generatedAfter, currentUnit);

	}

	/**
	 * Arrays do not have an INIT function, they are not objects, so we fake one
	 * the moment we see newarray
	 * 
	 * @param body
	 * @param units
	 * @param currentUnit
	 * @param array
	 * @param arraySize
	 */
	private void instrumentArrayInitExpression(Body body, Unit currentUnit,
			//
			Value array, Value arraySize) {

		String invokeType = "ArrayOperation";
		String fakeMethodSignature = "<" + array.getType() + ": void <init>(int)>";

		List<Value> parameterList = new ArrayList<>();
		parameterList.add(arraySize);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));
		// This assume that we observed a call to "new"
		generatedAfter.addAll(addTraceObject(array, fakeMethodSignature));

		generatedAfter.addAll(addTraceStop(fakeMethodSignature, array, NullConstant.v(), body));

		injectTracingCode(body, currentUnit, generatedBefore, generatedAfter);

	}

	/**
	 * Fake a call to a generic method STORE of the array
	 * 
	 * @param body
	 * @param units
	 * @param currentUnit
	 * @param array
	 * @param arrayIndex
	 * @param rightOp
	 */
	private void instrumentArrayElementAssignmentExpression(Body body, Unit currentUnit,
			//
			Value array, Value arrayIndex, //
			Value rightOp) {

		String invokeType = "ArrayOperation";
		String fakeMethodSignature = "<" + array.getType() + ": void store(int,"
				+ array.getType().toString().replace("[]", "") + ")>";

		List<Value> parameterList = new ArrayList<>();
		parameterList.add(arrayIndex);
		parameterList.add(rightOp);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));

		generatedAfter.addAll(addTraceObject(array, fakeMethodSignature));

		generatedAfter.addAll(addTraceStop(fakeMethodSignature, array, NullConstant.v(), body));

		injectTracingCode(body, currentUnit, generatedBefore, generatedAfter);
	}

	/**
	 * Fake a call to a generic method STORE of the array
	 * 
	 * @param body
	 * @param units
	 * @param currentUnit
	 * @param array
	 * @param arrayIndex
	 * @param rightOp
	 */
	private void instrumentArrayAccessExpression(Body body, Unit currentUnit,
			//
			Value leftOp, //
			Value array, Value arrayIndex) {

		String invokeType = "ArrayOperation";
		String fakeMethodSignature = "<" +  array.getType() + ": "+ array.getType().toString().replace("[]", "") + " get(int)>";

		List<Value> parameterList = new ArrayList<>();
		parameterList.add(arrayIndex);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));

		generatedAfter.addAll(addTraceObject(array, fakeMethodSignature));

		generatedAfter.addAll(addTraceStop(fakeMethodSignature, array, leftOp, body));

		
		System.out.println("ABCInstrumentArtificialInvocations.instrumentArrayAccessExpression() GENERATED CODE: ");
		for( Unit u : generatedBefore ){
			System.out.println(">>" + u );
		}
		
		
		injectTracingCode(body, currentUnit, generatedBefore, generatedAfter);
	}

	/**
	 * Trace the assignment of String to StringValue. This is needed because
	 * strings are NEVER initialized, that is, usually we do not observe a call
	 * to "new String()"
	 * 
	 * @param body
	 * @param currentUnit
	 * @param variable
	 * @param value
	 */
	private void instrumentStringAssignmentExpression(Body body, Unit currentUnit, Local variable,
			StringConstant value) {

		String invokeType = "StringOperation";
		// Generate a String from nowhere ;)
		String fakeMethodSignature = "<abc.String: java.lang.String set()>";

		List<Value> parameterList = new ArrayList<>();
		// parameterList.add(value);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));

		// Since "abc.String" is static we do not trace its owner

		generatedAfter.addAll(addTraceStop(fakeMethodSignature, NullConstant.v(), value, body));

		injectTracingCode(body, currentUnit, generatedBefore, generatedAfter);

	}

	/*
	 * Ensure to TAG own on code !
	 */
	private void injectTracingCode(Body body, Unit currentUnit, List<Unit> generatedBefore, List<Unit> generatedAfter) {
		PatchingChain<Unit> units = body.getUnits();
		for (Unit unit : generatedBefore) {
			System.out.println("ABCInstrumentArtificialInvocations.injectTracingCode() TAG " + unit );
			unit.addTag(ABCTag.TAG);
		}

		for (Unit unit : generatedAfter) {
			System.out.println("ABCInstrumentArtificialInvocations.injectTracingCode() TAG " + unit );
			unit.addTag(ABCTag.TAG);
		}
		// Be sure to tage the units we generate !
		units.insertBefore(generatedBefore, currentUnit);
		units.insertAfter(generatedAfter, currentUnit);

	}

}
