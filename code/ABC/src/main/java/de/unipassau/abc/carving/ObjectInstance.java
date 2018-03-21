package de.unipassau.abc.carving;

public class ObjectInstance implements DataNode {

	private String objectId;

	public ObjectInstance(String objectId) {
		if (objectId == null)
			throw new IllegalArgumentException("ObjectInstance cannot have null objectId");
		this.objectId = objectId;
	}

	public String getObjectId() {
		return objectId;
	}
	
	// This is somehow wrong...
	public static ObjectInstance SystemIn() {
		// Those are static instances... not sure how to handle them !
		return new ObjectInstance("java.lang.System.in@0");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
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
		ObjectInstance other = (ObjectInstance) obj;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		return true;
	}

	public String getType() {
		return objectId.split("@")[0];
	}

	@Override
	public String toString() {
		return objectId;
	}

}
