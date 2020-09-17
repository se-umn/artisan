package de.unipassau.abc.data;

public class PlaceholderDataNode implements DataNode {

	private DataNode originalDataNode;

	public PlaceholderDataNode(DataNode originalDataNode) {
		this.originalDataNode = originalDataNode;
	}

	@Override
	public DataNode clone() {
		return new PlaceholderDataNode(originalDataNode.clone());
	}

	@Override
	public String getType() {
		// TODO This is fishy...
		return "PLACEHOLDER";
	}

	public Object getOriginalDataNode() {
		return originalDataNode;
	}

}
