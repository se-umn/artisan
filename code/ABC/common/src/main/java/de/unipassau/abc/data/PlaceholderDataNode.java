package de.unipassau.abc.data;

public class PlaceholderDataNode implements DataNode {

    private DataNode originalDataNode;

    public PlaceholderDataNode(DataNode originalDataNode) {
        this.originalDataNode = originalDataNode;
    }

    @Override
    public DataNode clone() {
        PlaceholderDataNode cloned = null;
        try {
            cloned = (PlaceholderDataNode) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cloned.originalDataNode = originalDataNode.clone();
        return cloned;
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
