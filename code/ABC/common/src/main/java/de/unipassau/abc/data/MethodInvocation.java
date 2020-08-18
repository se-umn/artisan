package de.unipassau.abc.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.tagkit.Tag;

/// FIXME Rebuild hashCode and Equals
// TODO Encapsulate the calls to JimpleUtils and expose proper methods, like getMethodName and the like...
public class MethodInvocation implements GraphNode, Comparable<MethodInvocation>, Cloneable {

	private final static Logger logger = LoggerFactory.getLogger(MethodInvocation.class);

	// Makes easy to cache result but requires explicity resetting of the state !!!
	public boolean alreadyCarved = false;

	// Unique id of the method invocation
	private int invocationCount;
	// Signature of the method
	private String methodSignature;
	// Method owner (null if static)
	private ObjectInstance owner;
	// List of the actual parameters
	private List<DataNode> actualParameterInstances;
	// return value if any
	private DataNode returnValue;

	/// The following might not be used ... TODO Check what we need !
	@Deprecated
	private String[] actualParameters;

	@Deprecated // TODO WHAT'S THIS ?
	private String invocationType;
	@Deprecated
	private String xmlFileForOwner; // This stores the owner value status AFTER

	@Deprecated // calling this method
	private String xmlFileForReturn; // This stores the return value status
										// AFTER calling this method

	// This is to include comments in the final
	@Deprecated
	private int distanceFromMain;

	// Todo TAG this method invocation as being part of external libraries
	// instead of applications
	@Deprecated
	private boolean belongToExternalInterface;

	private boolean isPrivate = false;

	// why we need this if we have owner?

	private boolean staticCall;

	@Deprecated
	private boolean isTestSetupCall;

	private boolean isLibraryCall;

	private boolean isConstructor;

	@Deprecated
	private boolean isAndroidActivityCallback;

	@Deprecated
	private boolean isAndroidFragmentCallback;

	public MethodInvocation clone() {
		MethodInvocation cloned = new MethodInvocation(invocationCount, methodSignature);

		if (actualParameterInstances != null) {
			cloned.actualParameterInstances = actualParameterInstances.stream().map(new Function<DataNode, DataNode>() {

				@Override
				public DataNode apply(DataNode t) {
					return t.clone();
				}
			}).collect(Collectors.toList());
		}

		if (actualParameters != null) {
			cloned.actualParameters = Arrays.copyOf(actualParameters, actualParameters.length);
		}

		cloned.alreadyCarved = alreadyCarved;
		cloned.belongToExternalInterface = belongToExternalInterface;
		cloned.distanceFromMain = distanceFromMain;
		cloned.invocationCount = invocationCount;
		cloned.invocationType = invocationType;
		cloned.isConstructor = isConstructor;
		cloned.isExceptional = isExceptional;
		cloned.isLibraryCall = isLibraryCall;
		cloned.isNecessary = isNecessary;
		cloned.isPrivate = isPrivate;
		cloned.isProtected = isProtected;
		cloned.isPublic = isPublic;
		cloned.isSynthetic = isSynthetic;
		cloned.isTestSetupCall = isTestSetupCall;
		cloned.methodSignature = methodSignature;

		if (owner != null) {
			cloned.owner = owner.clone();
		}
		if (raisedException != null) {
			cloned.raisedException = raisedException.clone();
		}
		if (returnValue != null) {
			cloned.returnValue = returnValue.clone();
		}

		cloned.staticCall = staticCall;
		cloned.xmlFileForOwner = xmlFileForOwner;
		cloned.xmlFileForReturn = xmlFileForReturn;

		return cloned;
	}

	// The parameters and the owners are set later from the Graph Objects?
	// Basically explicitly set all the properties ?
	public MethodInvocation(int invocationCount, String methodSignature) {
		this.methodSignature = methodSignature;
		this.invocationCount = invocationCount;
		this.isConstructor = JimpleUtils.isConstructor(methodSignature);
		this.actualParameterInstances = new ArrayList<>();
		// Void ?
		this.returnValue = null;
		// Owenr == null vs Owner == NullInstnace ?
	}

	@Deprecated
	public MethodInvocation(int invocationCount, String methodSignature, String[] actualParameters) {
		this.invocationCount = invocationCount;
		this.methodSignature = methodSignature;
		this.actualParameters = actualParameters;
		this.isConstructor = JimpleUtils.isConstructor(methodSignature);
	}

	@Deprecated // Replace the occurrences of this with the other one !
	public MethodInvocation(String jimpleMethod, int invocationCount) {
		this.methodSignature = jimpleMethod;
		this.invocationCount = invocationCount;
	}

	public void setInvocationType(String invocationType) {
		this.invocationType = invocationType;
	}

	public String getInvocationType() {
		return invocationType;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public int getInvocationCount() {
		return invocationCount;
	}

	// Note that invocationType for the moment does not count for equals and
	// hashCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + invocationCount;
		result = prime * result + ((methodSignature == null) ? 0 : methodSignature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodInvocation other = (MethodInvocation) obj;
		if (invocationCount != other.invocationCount)
			return false;
		if (methodSignature == null) {
			if (other.methodSignature != null)
				return false;
		} else if (!methodSignature.equals(other.methodSignature))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return methodSignature + "_" + invocationCount;
	}

	public void setStatic(boolean staticCall) {
		this.staticCall = staticCall;
	}

	public boolean isStatic() {
		return this.staticCall;
	}

	// TODO not sure this will cause no harm...
	public ObjectInstance getOwner() {
		return owner;
	}

	public void setOwner(ObjectInstance objectInstance) {
		this.owner = objectInstance;
	}

	public void setBelongsToExternalInterface(boolean b) {
		this.belongToExternalInterface = b;
	}

	public boolean belongsToExternalInterface() {
		return belongToExternalInterface;
	}

	public void setXmlDumpForOwner(String xmlFile) {
		logger.trace("MethodInvocation.setXmlDumpForOwner() " + xmlFile + " for " + this);
		this.xmlFileForOwner = xmlFile;
	}

	public String getXmlDumpForOwner() {
		return xmlFileForOwner;
	}

	public void setXmlDumpForReturn(String xmlFile) {
		logger.trace("MethodInvocation.setXmlDumpForReturn() " + xmlFile + " for " + this);
		this.xmlFileForReturn = xmlFile;
	}

	public String getXmlDumpForReturn() {
		return xmlFileForReturn;
	}

	public static MethodInvocation fromTag(Tag tag) {
		String[] tokens = tag.getName().replaceAll("carving:", "").split("_");
		return new MethodInvocation(tokens[0], Integer.parseInt(tokens[1]));
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
		if (this.isPrivate) {
			this.isPublic = false;
			this.isProtected = false;
		}
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	/**
	 * Sort by ascending order of invocation
	 */
	@Override
	public int compareTo(MethodInvocation o) {
		return getInvocationCount() - o.getInvocationCount();
	}

	public void setTestSetupCall(boolean b) {
		this.isTestSetupCall = b;
	}

	public boolean isTestSetupCall() {
		return isTestSetupCall;
	}

	public boolean isAfter(MethodInvocation next) {
		return compareTo(next) > 0;
	}

	public boolean isBefore(MethodInvocation next) {
		return compareTo(next) < 0;
	}

	/* Identifies System and 3rd party library method calls */
	public void setLibraryCall(boolean b) {
		this.isLibraryCall = b;
	}

	public boolean isLibraryCall() {
		return isLibraryCall;
	}

	// Why this is deprecated? because we should lookup this dependency from the
	// DataDependencyGraph ?
	@Deprecated
	public String[] getActualParameters() {
		return actualParameters;
	}

	@Deprecated
	public void setActualParameters(String[] actualParameters) {
		this.actualParameters = actualParameters;
	}

	@Deprecated
	public List<DataNode> getActualParameterInstances() {
		return actualParameterInstances;
	}

	@Deprecated
	public void setActualParameterInstances(List<DataNode> actualParameterInstances) {
		this.actualParameterInstances = actualParameterInstances;
	}

	@Deprecated
	public boolean isPure() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isConstructor() {
		return this.isConstructor;
	}

	// Not sure this might be really needed
	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	public DataNode getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(DataNode returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * 
	 * https://developer.android.com/guide/components/activities/activity-lifecycle
	 * 
	 * NOTE: Not all the methods might be implemented...
	 * 
	 * onCreate(): In the onCreate() method, you perform basic application startup
	 * logic that should happen only once for the entire life of the activity. This
	 * method receives the parameter savedInstanceState, which is a Bundle object
	 * containing the activity's previously saved state. If the activity has never
	 * existed before, the value of the Bundle object is null.
	 *
	 * >> Relevant properties of the bundle are accessed by calling getXXX methods
	 * while putXXX methods are used to store the state. Hence, in theory, carving
	 * the bundle objects might be enough to "drive" the activity in a specific
	 * state.
	 * 
	 * </br>
	 * onStart(): The onStart() call makes the activity visible to the user, as the
	 * app prepares for the activity to enter the foreground and become interactive.
	 * he onStart() method completes very quickly and, as with the Created state,
	 * the activity does not stay resident in the Started state. Once this callback
	 * finishes, the activity enters the Resumed state, and the system invokes the
	 * onResume() method.
	 * 
	 * >> This means we can chain onStart and onResume basically always...
	 * 
	 * </br>
	 * onResume(): . This is the state in which the app interacts with the user. The
	 * app stays in this state until something happens to take focus away from the
	 * app. Such an event might be, for instance, receiving a phone call, the user’s
	 * navigating to another activity, or the device screen’s turning off.
	 * 
	 * Visible and active in the foreground
	 * 
	 * If the activity returns to the Resumed state from the Paused state, the
	 * system once again calls onResume() method.
	 * 
	 * >> Regardless of which build-up event you choose to perform an initialization
	 * operation in, make sure to use the corresponding lifecycle event to release
	 * the resource.
	 * 
	 * >> Assertions specific to Activity lifecycle?
	 * 
	 * </br>
	 * 
	 * onPause(): When an interruptive event occurs, the activity enters the Paused
	 * state, and the system invokes the onPause() callback. user is leaving your
	 * activity (though it does not always mean the activity is being destroyed); it
	 * indicates that the activity is no longer in the foreground (though it may
	 * still be visible if the user is in multi-window mode).
	 * 
	 * 
	 * onPause() execution is very brief, and does not necessarily afford enough
	 * time to perform save operations. For this reason, you should not use
	 * onPause() to save application or user data, make network calls, or execute
	 * database transactions; such work may not complete before the method
	 * completes. Instead, you should perform heavy-load shutdown operations during
	 * onStop().
	 *
	 * >> Will this generate flaky tests?
	 * 
	 * </br>
	 * onStop(): When your activity is no longer visible to the user, it has entered
	 * the Stopped state, and the system invokes the onStop() callback. This may
	 * occur, for example, when a newly launched activity covers the entire screen.
	 * The system may also call onStop() when the activity has finished running, and
	 * is about to be terminated.
	 * 
	 * 
	 * </br>
	 * onDestroy(): onDestroy() is called before the activity is destroyed.
	 * 
	 * </br>
	 * 
	 * onRestart()</br>
	 * 
	 * 
	 * @return
	 */
	public boolean isAndroidActivityCallback() {
		return this.isAndroidActivityCallback;
	}

	public void setAndroidActivityCallback(boolean isAndroidActivityCallback) {
		this.isAndroidActivityCallback = isAndroidActivityCallback;
	}

	public boolean isAndroidFragmentCallback() {
		return this.isAndroidFragmentCallback;
	}

	public void setAndroidFragmentCallback(boolean isAndroidFragmentCallback) {
		this.isAndroidFragmentCallback = isAndroidFragmentCallback;
	}

	private boolean isSynthetic = false;

	private boolean isPublic;

	private boolean isProtected;

	private boolean isExceptional;

	// Must be an object... cannot be a primitive type
	private ObjectInstance raisedException;

	private boolean isNecessary;

	public void setSyntheticMethod(boolean synthetic) {
		this.isSynthetic = synthetic;
	}

	public boolean isSynthetic() {
		return isSynthetic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
		if (this.isPublic) {
			this.isPrivate = false;
			this.isProtected = false;
		}
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
		if (this.isProtected) {
			this.isPrivate = false;
			this.isPublic = false;
		}
	}

	public boolean isProtected() {
		return isProtected;
	}

	public void setExceptional(boolean b) {
		this.isExceptional = b;
	}

	public boolean isExceptional() {
		return isExceptional;
	}

	public void setRaisedException(ObjectInstance exceptionValue) {
		this.raisedException = exceptionValue;
	}

	public ObjectInstance getRaisedException() {
		return raisedException;
	}

	public void setNecessary(boolean isNecessary) {
		this.isNecessary = isNecessary;
	}

	public boolean isNecessary() {
		return isNecessary;
	}
}
