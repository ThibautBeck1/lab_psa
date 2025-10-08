package org.example;


// LoadOperation class
public class LoadOperation extends Operation {
    private int containerId;

    public LoadOperation(int dischargeId, int containerId) {
        super(dischargeId);
        this.containerId = containerId;
    }

    public int getContainerId() {
        return containerId;
    }

    @Override
    public String getType() {
        return "LOAD";
    }

    @Override
    public String toString() {
        return "LoadOperation{discharge=" + dischargeId +
                ", container=" + containerId + "}";
    }
}
