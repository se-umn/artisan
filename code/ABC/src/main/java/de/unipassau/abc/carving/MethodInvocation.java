package de.unipassau.abc.carving;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.tagkit.Tag;

/// FIXME Rebuild hashCode and Equals
public class MethodInvocation implements GraphNode, Comparable<MethodInvocation> {

	private final static Logger logger = LoggerFactory.getLogger(MethodInvocation.class);

	private String methodSignature;
	private String[] actualParameters;
	
	
	//
	private String invocationType;
	private String xmlFileForOwner; // This stores the owner value status AFTER
									// calling this method
	private String xmlFileForReturn; // This stores the return value status
										// AFTER calling this method

	// Null if static
	private ObjectInstance owner;

	private int invocationCount;
	// This is to include comments in the final
	private int distanceFromMain;

	// Todo TAG this method invocation as being part of external libraries
	// instead of applications
	private boolean belongToExternalInterface;

	private boolean isPrivate = false;

	// why we need this if we have owner? 
	private boolean staticCall;

	private boolean isTestSetupCall;

    private boolean isLibraryCall;

//	private boolean belongsToAbstractClass;

    public MethodInvocation(int invocationCount, String methodSignature, String[] actualParameters) {
        this.invocationCount = invocationCount;
        this.methodSignature = methodSignature;
        this.actualParameters = actualParameters;
    }
    
    @Deprecated
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
		// System.out.println( toString() + " isPrivate ? " + isPrivate );
		this.isPrivate = isPrivate;
	}

	public boolean isPrivate() {
		return isPrivate;
	}
	
	// This define the order as in the execution graph
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
		return compareTo( next ) > 0;
	}
	
	public boolean isBefore(MethodInvocation next) {
		return compareTo( next ) < 0;
	}

    /* Identifies System and 3rd party library method calls */
	public void setLibraryCall(boolean b) {
        this.isLibraryCall = b;
    }
    
    public boolean isLibraryCall() {
        return isLibraryCall;
    }

    public String[] getActualParameters() {
        return actualParameters;
    }

    public void setActualParameters(String[] actualParameters) {
        this.actualParameters = actualParameters;
    }

    public boolean isPure() {
        // TODO Auto-generated method stub
        return false;
    }

}
