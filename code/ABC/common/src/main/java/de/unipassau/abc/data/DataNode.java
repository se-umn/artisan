package de.unipassau.abc.data;

public interface DataNode extends GraphNode, Cloneable {

	public DataNode clone();

	public String getType();

}
