package de.unipassau.abc.carving;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.tagkit.Tag;

public class MethodInvocation implements GraphNode {

	private final static Logger logger = LoggerFactory.getLogger( MethodInvocation.class);
	
	
	private String invocationType;
	private String jimpleMethod;
	//
	private String xmlFileForOwner; // This stores the owner value status AFTER calling this method
	private String xmlFileForReturn; // This stores the return value status AFTER calling this method
	
	// Null if static
	private ObjectInstance owner;

	private int invocationCount;
	// This is to include comments in the final
	private int distanceFromMain;

	// Todo TAG this method invocation as being part of external libraries
	// instead of applications
	private boolean belongToExternalInterface;

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
		String[] tokens = tag.getName().replaceAll("carving:","").split("_");
		return new MethodInvocation( tokens[0], Integer.parseInt(tokens[1]));
	}

}
