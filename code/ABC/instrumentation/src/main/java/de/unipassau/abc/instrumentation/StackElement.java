package de.unipassau.abc.instrumentation;

class StackElement {
	public Object methodOwner;
	public String methodSignature;
	public String methodContext;
	public boolean isLibCall;
}