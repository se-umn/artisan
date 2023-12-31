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

    public static final int INVOCATION_TRACE_ID_NA_CONSTANT = -90000;

    private final static Logger logger = LoggerFactory.getLogger(MethodInvocation.class);

    // Makes easy to cache result but requires explicity resetting of the state !!!
    // Storing this here creates all sort of problems during carving...
//	public boolean alreadyCarved = false;

    // invocation id from the trace for debugging
    protected int invocationTraceId;
    // Unique id of the method invocation
    protected int invocationId;
    // Signature of the method
    protected String methodSignature;
    // Method owner (null if static)
    protected ObjectInstance owner;
    // List of the actual parameters
    protected List<DataNode> actualParameterInstances;
    // return value if any
    protected DataNode returnValue;

    /// The following might not be used ... TODO Check what we need !
    @Deprecated
    protected String[] actualParameters;

    @Deprecated // TODO WHAT'S THIS ?
    protected String invocationType;
    @Deprecated
    protected String xmlFileForOwner; // This stores the owner value status AFTER

    @Deprecated // calling this method
    protected String xmlFileForReturn; // This stores the return value status
                                       // AFTER calling this method

    // This is to include comments in the final
    @Deprecated
    protected int distanceFromMain;

    // Todo TAG this method invocation as being part of external libraries
    // instead of applications
    @Deprecated
    protected boolean belongToExternalInterface;

    protected boolean isPrivate = false;

    // why we need this if we have owner?

    protected boolean isStatic;

    protected boolean hasGenericReturnType;

    @Deprecated
    protected boolean isTestSetupCall;

    protected boolean isLibraryCall;

    protected boolean isConstructor;

    public MethodInvocation clone() {
        MethodInvocation cloned = null;
        try {
            cloned = (MethodInvocation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        cloned.invocationTraceId = invocationTraceId;
        cloned.invocationId = invocationId;
        cloned.methodSignature = methodSignature;
        // MethodInvocation cloned = new
        // MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT,
        // invocationCount,
        // methodSignature);

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

//		cloned.alreadyCarved = alreadyCarved;
        cloned.belongToExternalInterface = belongToExternalInterface;
        cloned.distanceFromMain = distanceFromMain;
        cloned.invocationTraceId = invocationTraceId;
        cloned.invocationId = invocationId;
        cloned.invocationType = invocationType;
        cloned.isConstructor = isConstructor;
        cloned.isExceptional = isExceptional;
        cloned.isLibraryCall = isLibraryCall;
        cloned.isNecessary = isNecessary;
        cloned.isPrivate = isPrivate;
        cloned.isProtected = isProtected;
        cloned.isPublic = isPublic;
        cloned.isSynthetic = isSynthetic;
        cloned.isAbstract = isAbstract;
        cloned.isTestSetupCall = isTestSetupCall;
        cloned.methodSignature = methodSignature;
        cloned.hasGenericReturnType = hasGenericReturnType;

        if (owner != null) {
            cloned.owner = owner.clone();
        }
        if (raisedException != null) {
            cloned.raisedException = raisedException.clone();
        }
        if (returnValue != null) {
            cloned.returnValue = returnValue.clone();
        }

        cloned.isStatic = isStatic;
        cloned.xmlFileForOwner = xmlFileForOwner;
        cloned.xmlFileForReturn = xmlFileForReturn;

        return cloned;
    }

    // The parameters and the owners are set later from the Graph Objects?
    // Basically explicitly set all the properties ?
    public MethodInvocation(int invocationTraceId, int invocationCount, String methodSignature) {
        this.invocationTraceId = invocationTraceId;
        this.methodSignature = methodSignature;
        this.invocationId = invocationCount;
        this.isConstructor = JimpleUtils.isConstructor(methodSignature);
        this.actualParameterInstances = new ArrayList<>();
        // Void ?
        this.returnValue = null;
        // Owenr == null vs Owner == NullInstnace ?
    }

    @Deprecated
    public MethodInvocation(int invocationTraceId, int invocationCount, String methodSignature,
            String[] actualParameters) {
        this.invocationTraceId = invocationTraceId;
        this.invocationId = invocationCount;
        this.methodSignature = methodSignature;
        this.actualParameters = actualParameters;
        this.isConstructor = JimpleUtils.isConstructor(methodSignature);
    }

    @Deprecated // Replace the occurrences of this with the other one !
    public MethodInvocation(int invocationTraceId, String jimpleMethod, int invocationCount) {
        this.methodSignature = jimpleMethod;
        this.invocationId = invocationCount;
    }

    public void setInvocationTraceId(int invocationTraceId) {
        this.invocationTraceId = invocationTraceId;
    }

    public void setInvocationCount(int invocationCount) {
        this.invocationId = invocationCount;
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
        return invocationId;
    }

    public int getInvocationTraceId() {
        return invocationTraceId;
    }

    // Note that invocationType for the moment does not count for equals and
    // hashCode
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + invocationId;
//        result = prime * result + invocationTraceId;
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
        if (invocationId != other.invocationId)
            return false;
//        if (invocationTraceId != other.invocationTraceId)
//            return false;
        if (methodSignature == null) {
            if (other.methodSignature != null)
                return false;
        } else if (!methodSignature.equals(other.methodSignature))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return methodSignature + "_" + invocationId + "_" + invocationTraceId;
    }

    public String toFullString() {
        final StringBuffer parameterList = new StringBuffer("\t Actual Parameters: ");
        actualParameterInstances.forEach(dn -> parameterList.append(dn + "(" + dn.getType() + ")"));
        parameterList.append("\n");

        return methodSignature + "_" + invocationId + "_" + invocationTraceId + "\n" + //
                "\t Owner: " + owner + "\n" + //
                parameterList.toString() + //
                "\t Return Value: " + returnValue;
    }

    public void setStatic(boolean staticCall) {
        this.isStatic = staticCall;
    }

    public boolean isStatic() {
        return this.isStatic;
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
        this.xmlFileForOwner = xmlFile;
    }

    public String getXmlDumpForOwner() {
        return xmlFileForOwner;
    }

    public void setXmlDumpForReturn(String xmlFile) {
        this.xmlFileForReturn = xmlFile;
    }

    public String getXmlDumpForReturn() {
        return xmlFileForReturn;
    }

    public boolean hasGenericReturnType() {
        return hasGenericReturnType;
    }

    public void setHasGenericReturnType(boolean hasGenericReturnType) {
        this.hasGenericReturnType = hasGenericReturnType;
    }

    public static MethodInvocation fromTag(Tag tag) {
        String[] tokens = tag.getName().replaceAll("carving:", "").split("_");
        return new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, tokens[0],
                Integer.parseInt(tokens[1]));
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
        // Use the Unique ID also to understand whether one call was generated, i.e., is
        // more recent and takes precedence?

        return (getInvocationTraceId() - o.getInvocationTraceId()) == 0
                ? (o.getInvocationCount() - getInvocationCount())
                : (getInvocationTraceId() - o.getInvocationTraceId());
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

    protected boolean isSynthetic = false;

    protected boolean isPublic;

    protected boolean isProtected;

    protected boolean isExceptional;

    // Must be an object... cannot be a primitive type
    protected ObjectInstance raisedException;

    private boolean isNecessary;

    private boolean isAbstract;

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

    public void incrementInvocationCountBy(int delta) {
        this.invocationId = this.invocationId + delta;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

}
