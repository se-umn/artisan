package de.unipassau.abc.carving;

import soot.Value;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;

public class PrimitiveValue implements ValueNode {

	private int id;
	private String type;
	private String stringValue;

	public PrimitiveValue(int id, String type, String stringValue) {
		this.id = id;
		this.type = type;
		this.stringValue = stringValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (id != other.id)
			return false;
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

	@Override
	public Value getData() {
		switch (type) {
		case "int":
		case "short":
		case "char":
		case "byte":
			return IntConstant.v(Integer.parseInt(stringValue));
		case "double":
			return DoubleConstant.v(Double.parseDouble(stringValue));
		case "float":
			return FloatConstant.v(Float.parseFloat(stringValue));
		case "long":
			return LongConstant.v(Long.parseLong(stringValue));
		case "boolean":
			return Boolean.parseBoolean(stringValue) ? IntConstant.v(1) : IntConstant.v(0);
		default:
			throw new RuntimeException("Unknonw primitive type " + type);
		}
	}
}