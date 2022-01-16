package de.unipassau.abc.data;

public class EnumConstant extends ObjectInstance {

    private final String name;

    public EnumConstant(String objectId, String name) {
        super(objectId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFullyQualifiedName() {
        return getType().replaceAll("\\$", ".") + "." + name;
    }

}
