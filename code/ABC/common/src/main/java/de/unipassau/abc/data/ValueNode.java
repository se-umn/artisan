package de.unipassau.abc.data;

import soot.Value;

// Remove soot dependency here !
public interface ValueNode extends DataNode, Cloneable {

	public Value getData();
}
