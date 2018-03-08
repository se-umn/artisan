package de.unipassau.carving;

import soot.Value;
import soot.jimple.StringConstant;

// This could be easily a PrimitiveValue node
public class StringValue implements ValueNode {
	
	private int id;
	private String value;
	
	public StringValue(int id, String value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public Value getData() {
		return StringConstant.v(value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		StringValue other = (StringValue) obj;
		if (id != other.id)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	

}
