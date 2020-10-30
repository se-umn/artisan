package de.unipassau.abc.data;

/**
 * 
 * @author gambitemp
 *
 */
public class NullInstance extends ObjectInstance {

//	 Why NullInstance declared a unique ID ?
//    private final static AtomicInteger uniqueIdGenerator = new AtomicInteger(0);

	private final int id = 0;
	private String type;

	public NullInstance(String type) {
		super(type + "@" + 0);
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
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
