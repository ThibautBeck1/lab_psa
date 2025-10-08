package org.example;
// UnloadOperation class
public class UnloadOperation extends Operation {
    private int containerId;
    private int storageId;

    public UnloadOperation(int dischargeId, int containerId, int storageId) {
        super(dischargeId);
        this.containerId = containerId;
        this.storageId = storageId;
    }

    public int getContainerId() {
        return containerId;
    }

    public int getStorageId() {
        return storageId;
    }

    @Override
    public String getType() {
        return "UNLOAD";
    }

    @Override
    public String toString() {
        return "UnloadOperation{discharge=" + dischargeId +
                ", container=" + containerId + ", storage=" + storageId + "}";
    }
}
