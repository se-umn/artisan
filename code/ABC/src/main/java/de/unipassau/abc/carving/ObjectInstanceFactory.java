package de.unipassau.abc.carving;

public class ObjectInstanceFactory {

    public static ObjectInstance get(String objectInstanceId) {
        return new ObjectInstance(objectInstanceId);
    }

}
