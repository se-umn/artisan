package de.unipassau.abc.carving;

import de.unipassau.abc.utils.JimpleUtils;

public class ObjectInstance implements DataNode {

    static class StaticObjectInstance extends ObjectInstance {

        private String type;

        public StaticObjectInstance(String objectId, String type) {
            super(objectId);
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

    }

    public final static ObjectInstance systemErr = new StaticObjectInstance("java.lang.System.err@0",
            System.err.getClass().getName());

    // Special Instances are handled via subclasses
    public final static ObjectInstance systemIn = new StaticObjectInstance("java.lang.System.in@0",
            // System.in.getClass().getName()
            "java.io.InputStream");
    public final static ObjectInstance systemOut = new StaticObjectInstance("java.lang.System.out@0",
            System.out.getClass().getName());

    // TODO How to flag this ? -> Call chain to init of android or type
    // inference with soot or something...
    // DuckTyping
    private boolean isAndroidActivity = false;

    private String objectId;

    private String type;
    
    // This is for boxed primitives...
    private String stringValue;

    private boolean isAndroidFragment;

    // Maybe a subclass for handling boxed primitives and classes ?
    public ObjectInstance(String objectId) {
        if (objectId == null)
            throw new IllegalArgumentException("ObjectInstance cannot have null objectId");

        if (objectId.contains(":")) {
            this.stringValue = objectId.split(":")[1];
            this.objectId = objectId.split(":")[0];
        } else {
            this.objectId = objectId;
        }
        this.type = objectId.split("@")[0];
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObjectInstance other = (ObjectInstance) obj;
        if (objectId == null) {
            if (other.objectId != null)
                return false;
        } else if (!objectId.equals(other.objectId))
            return false;
        if (stringValue == null) {
            if (other.stringValue != null)
                return false;
        } else if (!stringValue.equals(other.stringValue))
            return false;
        return true;
    }

    public String getObjectId() {
        return objectId;
    }
    // Only for boxed primitives... Maybe we cou
    public String getStringValue() {
        return stringValue;
    }
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
        result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
        return result;
    }

    public boolean isAndroidActivity() {
        return isAndroidActivity;
    }

    public void setAndroidActivity(boolean isAndroidActivity) {
        this.isAndroidActivity = isAndroidActivity;
    }
    
    public boolean isAndroidFragment() {
        return isAndroidFragment;
    }

    public void setAndroidFragment(boolean isAndroidFragment) {
        this.isAndroidFragment = isAndroidFragment;
    }

    @Override
    public String toString() {
        return objectId;
    }
    
    public boolean isBoxedPrimitive() {
        return JimpleUtils.isBoxedPrimitive(getType());
    }
    
    public static void retype(ObjectInstance objectInstance, String newType){
        objectInstance.type = newType;
    }

}
