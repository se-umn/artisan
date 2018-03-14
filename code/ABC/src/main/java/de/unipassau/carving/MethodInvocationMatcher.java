package de.unipassau.carving;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.utils.JimpleUtils;

public class MethodInvocationMatcher {

	static class NoMatchMethodInvocationMatcher extends MethodInvocationMatcher {

		protected NoMatchMethodInvocationMatcher() {
			super();
		}

		@Override
		public boolean match(MethodInvocation methodInvocation) {
			return false;
		}
	}

	private final static Logger logger = LoggerFactory.getLogger(MethodInvocationMatcher.class);
	// Jimple methods: <org.employee.Validation: int
	// numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
	private final Pattern jimpleMethodInvocationPattern = Pattern.compile("<" // Opening
																				// Tag
			+ "[a-zA_Z_][\\.\\w]*" //
			+ ":\\s" //
			+ "[a-zA_Z_][\\.\\w]*" //
			+ "\\s"//
			+ "([\\.\\w][\\.\\w]*|<init>|<clinit>)" // Method name or
													// constructor or static
													// initializer
			+ "\\(([a-zA_Z_][\\.\\w]*)?(,[a-zA_Z_][\\.\\w]*)*\\)" // Parameter
																	// list
			+ ">"); // Closing Tag

	// Package and class name, package class and name for return type,
	// methodName, ( list of (package class and name for parameter)>
	// <[*.*] [*.*] [*]

	private final Pattern classPattern;
	private final Pattern returnPattern;
	private final Pattern methodPattern;
	private final Pattern[] parameterPatterns;
	//
	private int invocationID;
	//
	private final Pattern instancePattern;

	public static MethodInvocationMatcher byPackage(String string) {
		return byClass(string + ".*");
	}

	public static MethodInvocationMatcher byClass(String string) {
		return new MethodInvocationMatcher(string, null, null, null, -1, null);
	}

	public static MethodInvocationMatcher byReturnType(String string) {
		return new MethodInvocationMatcher(".*", string, null, null, -1, null);
	}

	public static MethodInvocationMatcher byMethod(String string) {
		// TODO Validate this is a well formed jimple method
		return fromJimpleMethod(string);
	}

	public static MethodInvocationMatcher byInstance(ObjectInstance objectInstance) {
		return new MethodInvocationMatcher(objectInstance);
	}

	// Testing mostly
	public static MethodInvocationMatcher fromJimpleMethod(String jimpleMethod) {
		return new MethodInvocationMatcher(//
				JimpleUtils.getClassNameForMethod(jimpleMethod), //
				JimpleUtils.getReturnType(jimpleMethod), //
				JimpleUtils.getMethodName(jimpleMethod), //
				JimpleUtils.getParameterList(jimpleMethod));
	}

	public static MethodInvocationMatcher fromMethodInvocation(MethodInvocation methodInvocation) {
		String jimpleMethod = methodInvocation.getJimpleMethod();
		return new MethodInvocationMatcher(//
				JimpleUtils.getClassNameForMethod(jimpleMethod), //
				JimpleUtils.getReturnType(jimpleMethod), //
				JimpleUtils.getMethodName(jimpleMethod), //
				JimpleUtils.getParameterList(jimpleMethod), methodInvocation.getInvocationCount(), null);
	}

	// This is only for the SubClasses
	protected MethodInvocationMatcher() {
		classPattern = null;
		returnPattern = null;
		methodPattern = null;
		parameterPatterns = null;
		invocationID = -1;
		instancePattern = null;
	}

	// Avoid this to be called from outside
	protected MethodInvocationMatcher(String classRegEx, String returnRegEx, String methodNameRegEx,
			String[] parameterRegExs, int invocationID, String objectId) {
		classPattern = Pattern.compile(classRegEx);
		returnPattern = returnRegEx != null ? Pattern.compile(returnRegEx) : null;
		methodPattern = methodNameRegEx != null ? Pattern.compile(methodNameRegEx) : null;
		//
		if (parameterRegExs != null && parameterRegExs.length > 0) {
			List<Pattern> _parameterPatterns = new ArrayList<>();
			for (String parameterRegEx : parameterRegExs) {
				_parameterPatterns.add(Pattern.compile(parameterRegEx));
			}
			parameterPatterns = _parameterPatterns.toArray(new Pattern[] {});
		} else {
			parameterPatterns = new Pattern[] {};
		}
		this.invocationID = invocationID;
		this.instancePattern = objectId != null ? Pattern.compile(Pattern.quote(objectId)) : null;
	}

	protected MethodInvocationMatcher(String classRegEx, String returnRegEx, String methodNameRegEx,
			String... parameterRegExs) {
		this(classRegEx, returnRegEx, methodNameRegEx, parameterRegExs, -1, null);
	}
	// Probably better to use a Builder pattern here...

	protected MethodInvocationMatcher(ObjectInstance objectInstance) {
		// Any value, since those will not be used
		this(".*", ".*", ".*", new String[] {}, -1, objectInstance.getObjectId());

	}

	public boolean match(MethodInvocation methodInvocation) {
		return instancePattern == null ? matchByRegEx(methodInvocation) : matchByInstanceId(methodInvocation);
	}

	private boolean matchByInstanceId(MethodInvocation methodInvocation) {
		if (methodInvocation.isStatic()) {
			return false;
		}
		String objectId = methodInvocation.getOwner().getObjectId();

		if (objectId == null) {

		}
		final Matcher instanceMatcher = instancePattern.matcher(objectId);
		if (!instanceMatcher.find()) {
			logger.trace(methodInvocation + " with owner " + objectId + " does not match instanceMatcher");
			return false;
		}

		return true;

	}

	private boolean matchByRegEx(MethodInvocation methodInvocation) {
		// Basic chain of command pattern
		String jimpleMethod = methodInvocation.getJimpleMethod();
		final Matcher jimpleMatcher = jimpleMethodInvocationPattern.matcher(jimpleMethod);

		if (!jimpleMatcher.find()) {
			logger.trace(methodInvocation + " does not match jimpleMatcher");
			return false;
		}

		final Matcher classMatcher = classPattern.matcher(JimpleUtils.getClassNameForMethod(jimpleMethod));
		if (!classMatcher.find()) {
			logger.trace(methodInvocation + " does not match classPatternMatcher " + classPattern);
			return false;
		} else if (returnPattern == null) {
			return true;
		}

		// From this point on, we consider matching exaclty !

		final Matcher returnMatcher = returnPattern.matcher(JimpleUtils.getReturnType(jimpleMethod));
		if (!returnMatcher.find()) {
			logger.trace(methodInvocation + " does not match returnPatternMatcher " + returnPattern);
			return false;
		} else if (methodPattern == null) {
			return true;
		}

		final Matcher methodMatcher = methodPattern.matcher(JimpleUtils.getMethodName(jimpleMethod));
		if (!methodMatcher.find()) {
			logger.trace(methodInvocation + " does not match methodPatternMatcher " + methodPattern);
			return false;
		}
		// Matching parametes positionally
		String[] formalParams = JimpleUtils.getParameterList(jimpleMethod);
		if (formalParams.length != parameterPatterns.length) {
			logger.trace(methodInvocation + " does not match methodParameterCountMatcher");
			return false;
		}

		for (int pos = 0; pos < formalParams.length; pos++) {
			final Matcher parameterMatcher = parameterPatterns[pos].matcher(formalParams[pos]);
			if (!parameterMatcher.find()) {
				logger.trace(methodInvocation + " does not match parameterCountMatcher for " + formalParams[pos]);
				return false;
			}
		}

		if (invocationID != -1) {
			if (methodInvocation.getInvocationCount() != this.invocationID) {
				logger.trace(methodInvocation + " does not match invocationCount for " + this.invocationID);
				return false;
			}
		}
		return true;
	}

	// Return a Matcher that do not match anything, this is mostly to avoid
	// having around null objects
	public static MethodInvocationMatcher noMatch() {
		return new NoMatchMethodInvocationMatcher();
	}

}
