package org.example;
public abstract class Operation {
    protected int dischargeId;

    public Operation(int dischargeId) {
        this.dischargeId = dischargeId;
    }

    public int getDischargeId() {
        return dischargeId;
    }

    public abstract String getType();

    @Override
    public abstract String toString();
}