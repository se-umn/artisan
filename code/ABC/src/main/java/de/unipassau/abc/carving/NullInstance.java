package de.unipassau.abc.carving;

import soot.Value;
import soot.jimple.NullConstant;

public class NullInstance implements ValueNode {

	private int id;
	private String type;
	
	public NullInstance(int id, String type) {
		this.id = id;
		this.type = type;
	}

	@Override
	public Value getData() {
		return NullConstant.v();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		NullInstance other = (NullInstance) obj;
		if (id != other.id)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
	
}
