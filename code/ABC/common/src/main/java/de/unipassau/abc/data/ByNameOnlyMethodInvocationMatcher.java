package de.unipassau.abc.data;

import java.util.regex.Pattern;

public class ByNameOnlyMethodInvocationMatcher extends MethodInvocationMatcher {
	final private Pattern methodNameRegex;

	public ByNameOnlyMethodInvocationMatcher(String methodNameRegex) {
		this.methodNameRegex = Pattern.compile(methodNameRegex);
	}

	@Override
	public boolean matches(MethodInvocation methodInvocation) {
		return methodNameRegex.matcher(JimpleUtils.getMethodName(methodInvocation.getMethodSignature())).matches();
	}
}
