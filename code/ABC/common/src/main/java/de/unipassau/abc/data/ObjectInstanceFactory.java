package de.unipassau.abc.data;

public class ObjectInstanceFactory {

    public static ObjectInstance get(String objectInstanceId) {
        return new ObjectInstance(objectInstanceId);
    }

    private final static ObjectInstance SYSTEM_INTENT = new ObjectInstance("abc.android.SystemIntent@0");
    
    public static ObjectInstance getSystemIntent() {
        return SYSTEM_INTENT;
    }

}
