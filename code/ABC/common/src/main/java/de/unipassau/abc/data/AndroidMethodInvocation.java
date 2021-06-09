package de.unipassau.abc.data;

public class AndroidMethodInvocation extends MethodInvocation {

	private boolean isAndroidActivityCallback;

	private boolean isAndroidFragmentCallback;

	public AndroidMethodInvocation(int invocationTraceId, int invocationCount, String methodSignature) {
		super(invocationTraceId, invocationCount, methodSignature);
	}

	// Create a more specific android related copy out of a general method
	// invocation
	public AndroidMethodInvocation(MethodInvocation methodInvocation) {
		super(methodInvocation.getInvocationTraceId(), methodInvocation.getInvocationCount(), methodInvocation.getMethodSignature());

		setOwner(methodInvocation.getOwner());
		setActualParameters(methodInvocation.getActualParameters());
		setActualParameterInstances(methodInvocation.getActualParameterInstances());
		setReturnValue(methodInvocation.getReturnValue());
		setInvocationType(methodInvocation.getInvocationType());
		setXmlDumpForOwner(methodInvocation.getXmlDumpForOwner());
		setXmlDumpForReturn(methodInvocation.getXmlDumpForReturn());
		setBelongsToExternalInterface(methodInvocation.belongsToExternalInterface());
		setPrivate(methodInvocation.isPrivate());
		setStatic(methodInvocation.isStatic());
		setTestSetupCall(methodInvocation.isTestSetupCall());
		setLibraryCall(methodInvocation.isLibraryCall());
		setConstructor(methodInvocation.isConstructor());
		setSyntheticMethod(methodInvocation.isSynthetic());
		setPublic(methodInvocation.isPublic());
		setProtected(methodInvocation.isProtected());
		setExceptional(methodInvocation.isExceptional());
		setRaisedException(methodInvocation.getRaisedException());
		setNecessary(methodInvocation.isNecessary());

		// android.util.Log is static, so it does not have any owner, but it is also a framework method
		if (owner != null) {
			isAndroidFragmentCallback = owner.isAndroidFragment() && JimpleUtils.isFragmentLifecycle(methodSignature);
			isAndroidActivityCallback = owner.isAndroidActivity() && JimpleUtils.isActivityLifecycle(methodSignature);
		}
	}

	/**
	 * https://developer.android.com/guide/components/activities/activity-lifecycle
	 * <p>
	 * NOTE: Not all the methods might be implemented...
	 * <p>
	 * onCreate(): In the onCreate() method, you perform basic application startup
	 * logic that should happen only once for the entire life of the activity. This
	 * method receives the parameter savedInstanceState, which is a Bundle object
	 * containing the activity's previously saved state. If the activity has never
	 * existed before, the value of the Bundle object is null.
	 * <p>
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
	 * <p>
	 * >> This means we can chain onStart and onResume basically always...
	 *
	 * </br>
	 * onResume(): . This is the state in which the app interacts with the user. The
	 * app stays in this state until something happens to take focus away from the
	 * app. Such an event might be, for instance, receiving a phone call, the user’s
	 * navigating to another activity, or the device screen’s turning off.
	 * <p>
	 * Visible and active in the foreground
	 * <p>
	 * If the activity returns to the Resumed state from the Paused state, the
	 * system once again calls onResume() method.
	 * <p>
	 * >> Regardless of which build-up event you choose to perform an initialization
	 * operation in, make sure to use the corresponding lifecycle event to release
	 * the resource.
	 * <p>
	 * >> Assertions specific to Activity lifecycle?
	 *
	 * </br>
	 * <p>
	 * onPause(): When an interruptive event occurs, the activity enters the Paused
	 * state, and the system invokes the onPause() callback. user is leaving your
	 * activity (though it does not always mean the activity is being destroyed); it
	 * indicates that the activity is no longer in the foreground (though it may
	 * still be visible if the user is in multi-window mode).
	 * <p>
	 * <p>
	 * onPause() execution is very brief, and does not necessarily afford enough
	 * time to perform save operations. For this reason, you should not use
	 * onPause() to save application or user data, make network calls, or execute
	 * database transactions; such work may not complete before the method
	 * completes. Instead, you should perform heavy-load shutdown operations during
	 * onStop().
	 * <p>
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
	 * <p>
	 * onRestart()</br>
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

	@Override
	public MethodInvocation clone() {
		// This works as long as super class also class super.clone() within its clone
		// method
		AndroidMethodInvocation cloned = (AndroidMethodInvocation) super.clone();
		cloned.isAndroidActivityCallback = isAndroidActivityCallback;
		cloned.isAndroidFragmentCallback = isAndroidFragmentCallback;
		return cloned;
	}
}
