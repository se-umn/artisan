package de.unipassau.abc.carving;

import java.util.concurrent.atomic.AtomicInteger;

// Why is this a ValueNode instead of an Object inStance? Becasue ObjectInstances create deps ? 
public class NullInstance extends ObjectInstance{ 
//implements ValueNode {
    
    private final static AtomicInteger uniqueIdGenerator = new AtomicInteger(0);
    
	private int uniqueId;
	private String type;
	
	public NullInstance(String type) {
	    super(type+"@"+0);
		this.uniqueId = uniqueIdGenerator.incrementAndGet();
		this.type = type;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + uniqueId;
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
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (uniqueId != other.uniqueId)
            return false;
        return true;
    }

}
