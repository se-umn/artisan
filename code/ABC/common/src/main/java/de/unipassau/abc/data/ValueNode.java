package de.unipassau.abc.data;

import soot.Value;

public interface ValueNode extends DataNode, Cloneable {

	public Value getData();
}
