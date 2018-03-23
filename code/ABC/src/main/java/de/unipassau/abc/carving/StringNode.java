package de.unipassau.abc.carving;

import soot.Value;
import soot.jimple.StringConstant;

// This could be easily a PrimitiveValue node
public class StringNode extends ObjectInstance implements ValueNode {
	
	public StringNode(String objectId) {
		super(objectId);
	}
	
	public StringNode(int id, String value) {
		this("java.lang.String@"+id);
		this.value = value;
	}

	private String value;
	
	@Override
	public Value getData() {
		return StringConstant.v(value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		StringNode other = (StringNode) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	

}
