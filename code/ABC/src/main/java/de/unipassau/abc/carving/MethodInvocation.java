package de.unipassau.abc.carving;

public class MethodInvocation implements GraphNode {

	private String invocationType;
	private String jimpleMethod;
	// Null if static
	private ObjectInstance owner;

	private int invocationCount;
	// This is to include comments in the final
	private int distanceFromMain;

	// Todo TAG this method invocation as being part of external libraries
	// instead of applications
	private boolean belongToExternalLibrary;

	public MethodInvocation(String jimpleMethod, int invocationCount) {
		this.jimpleMethod = jimpleMethod;
		this.invocationCount = invocationCount;
	}

	public void setInvocationType(String invocationType) {
		this.invocationType = invocationType;
	}

	public String getInvocationType() {
		return invocationType;
	}

	public String getJimpleMethod() {
		return jimpleMethod;
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
		result = prime * result + ((jimpleMethod == null) ? 0 : jimpleMethod.hashCode());
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
		if (jimpleMethod == null) {
			if (other.jimpleMethod != null)
				return false;
		} else if (!jimpleMethod.equals(other.jimpleMethod))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return jimpleMethod + "_" + invocationCount;
	}

	public boolean isStatic() {
		return "StaticInvokeExpr".equals(invocationType);
	}

	// TODO not sure this will cause no harm...
	public ObjectInstance getOwner() {
		return owner;
	}

	public void setOwner(ObjectInstance objectInstance) {
		this.owner = objectInstance;
	}

}
