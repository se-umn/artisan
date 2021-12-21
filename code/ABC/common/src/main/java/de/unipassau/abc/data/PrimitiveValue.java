package de.unipassau.abc.data;

import org.robolectric.util.Logger;

import soot.Value;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;

public class PrimitiveValue implements ValueNode, Cloneable {

    private int id;
    private String type;
    private String stringValue;

    @Override
    public PrimitiveValue clone() {
        PrimitiveValue cloned = null;
        try {
            cloned = (PrimitiveValue) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        cloned.id = id;
        cloned.type = type;
        cloned.stringValue = stringValue;
        // It does not seem to make sense to clone a primitive, but I am no sure we even
        // handle them as singletons. It does not seem to be the case, tho
//		PrimitiveValue cloned = new PrimitiveValue(id, type, stringValue);
        return cloned;
    }

    // Patch for strings. TODO Refactor with proper subclass of primitive or
    // implementation of ValueNode.
    private String refid;

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getRefid() {
        return refid;
    }
    /////

    public PrimitiveValue(int id, String type, String stringValue) {
        this.id = id;
        this.type = type;
        this.stringValue = stringValue;
    }

    // FOR ESTABLISHING THE EQUALITY OF PRIMITIVE VALUES WE DO NOT USE id !
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        PrimitiveValue other = (PrimitiveValue) obj;
        if (stringValue == null) {
            if (other.stringValue != null)
                return false;
        } else if (!stringValue.equals(other.stringValue))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    public String getType() {
        return type;
    }

    @Override
    public Value getData() {
        switch (type) {
        case "char":
            return IntConstant.v((int) stringValue.charAt(0));
        case "int":
        case "short":
        case "byte":
            return IntConstant.v(Integer.parseInt(stringValue));
        case "double":
            return DoubleConstant.v(Double.parseDouble(stringValue));
        case "float":
            return FloatConstant.v(Float.parseFloat(stringValue));
        case "long":
            return LongConstant.v(Long.parseLong(stringValue));
        case "boolean":
            // For whatever reason, in some cases we got true/false and in
            // others 1/0 !?
            try {
                return IntConstant.v(Integer.parseInt(stringValue));
            } catch (NumberFormatException e) {
                return Boolean.parseBoolean(stringValue) ? IntConstant.v(1) : IntConstant.v(0);
            }
        case "java.lang.String":
            if (stringValue == null) {
                return NullConstant.v();
            } else {
                System.out.println("PrimitiveValue.getData() Creating String using value " + stringValue );
                return StringConstant.v(stringValue);
            }
        default:
            throw new RuntimeException("Unknonw primitive type " + type);
        }
    }

    @Override
    public String toString() {
        if (JimpleUtils.isString(getType())) {
            // TODO Apparently we rely on this to check for equality !
            return JimpleUtils.generateStringContent(stringValue);
        } else {
            return stringValue;
        }
    }

    //
    public String getStringValue() {
        return stringValue;
    }
}
