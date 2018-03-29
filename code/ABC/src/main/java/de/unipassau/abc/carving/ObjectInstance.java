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

	// Special Instances are handled via subclasses
	public final static ObjectInstance systemIn = new StaticObjectInstance("java.lang.System.in@0",
			System.in.getClass().getName());
	public final static ObjectInstance systemOut = new StaticObjectInstance("java.lang.System.out@0",
			System.out.getClass().getName());
	public final static ObjectInstance systemErr = new StaticObjectInstance("java.lang.System.err@0",
			System.err.getClass().getName());

	static class StaticObjectInstance extends ObjectInstance {

		private String type;

		public StaticObjectInstance(String objectId, String type) {
			super(objectId);
			this.type = type;
		}

		public String getType() {
			return this.type;
		}

	}

}
