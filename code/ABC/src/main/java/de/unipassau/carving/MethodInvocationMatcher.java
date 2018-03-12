package de.unipassau.carving;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.utils.JimpleUtils;

public class MethodInvocationMatcher {

	private final static Logger logger = LoggerFactory.getLogger( MethodInvocationMatcher.class);
	// Jimple methods: <org.employee.Validation: int
	// numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
	private final Pattern jimpleMethodInvocationPattern = Pattern.compile(
			"<" // Opening Tag
			+ "[a-zA_Z_][\\.\\w]*" //
			+ ":\\s" //
			+ "[a-zA_Z_][\\.\\w]*" //
			+ "\\s"//
			+ "([\\.\\w][\\.\\w]*|<init>|<clinit>)" // Method name or constructor or static initializer
			+ "\\(([a-zA_Z_][\\.\\w]*)?(,[a-zA_Z_][\\.\\w]*)*\\)" // Parameter list
			+ ">"); // Closing Tag 

	// Package and class name, package class and name for return type,
	// methodName, ( list of (package class and name for parameter)>
	// <[*.*] [*.*] [*]

	private final Pattern classPattern;
	private final Pattern returnPattern;
	private final Pattern methodPattern;
	private final Pattern[] parameterPatterns;

	public MethodInvocationMatcher(String classRegEx, String returnRegEx, String methodNameRegEx,
			String... parameterRegExs) {
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
			parameterPatterns = new Pattern[]{};
		}
	}

	
	// Probably better to use a Builder pattern here...
	public MethodInvocationMatcher(String classRegEx) {
		this(classRegEx, null, null);
	}

	// TODO This might be simplified by merging together the varoius regex
	// instead
	public boolean match(MethodInvocation methodInvocation) {
		// Basic chain of command pattern
		String jimpleMethod = methodInvocation.getJimpleMethod();
		final Matcher jimpleMatcher = jimpleMethodInvocationPattern.matcher(jimpleMethod);

		if (!jimpleMatcher.find()) {
			logger.trace( methodInvocation + " does not match jimpleMatcher");
			return false;
		}

		final Matcher classMatcher = classPattern.matcher(JimpleUtils.getClassNameForMethod(jimpleMethod));
		if (!classMatcher.find()) {
			logger.trace( methodInvocation + " does not match classPatternMatcher " + classPattern);
			return false;
		} else if (returnPattern == null) {
			return true;
		}

		// From this point on, we consider matching exaclty !
		
		final Matcher returnMatcher = returnPattern.matcher(JimpleUtils.getReturnType(jimpleMethod));
		if (!returnMatcher.find()) {
			logger.trace( methodInvocation + " does not match returnPatternMatcher " + returnPattern );
			return false;
		}

		final Matcher methodMatcher = methodPattern.matcher(JimpleUtils.getMethodName(jimpleMethod));
		if (!methodMatcher.find()) {
			logger.trace( methodInvocation + " does not match methodPatternMatcher " + methodPattern);
			return false;
		}
		// Matching parametes positionally
		String[] formalParams = JimpleUtils.getParameterList(jimpleMethod);
		if (formalParams.length != parameterPatterns.length) {
			logger.trace( methodInvocation + " does not match methodParameterCountMatcher");
			return false;
		}

		for (int pos = 0; pos < formalParams.length; pos++) {
			final Matcher parameterMatcher = parameterPatterns[pos].matcher(formalParams[pos]);
			if (!parameterMatcher.find()) {
				logger.trace( methodInvocation + " does not match parameterCountMatcher for " + formalParams[pos]);
				return false;
			}
		}

		return true;
	}

	public static MethodInvocationMatcher fromJimpleMethod(String jimpleMethod) {
		return new MethodInvocationMatcher(//
				JimpleUtils.getClassNameForMethod(jimpleMethod), //
				JimpleUtils.getReturnType(jimpleMethod), //
				JimpleUtils.getMethodName(jimpleMethod), //
				JimpleUtils.getParameterList(jimpleMethod));
	}
}
