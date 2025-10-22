package org.example;

import java.util.ArrayList;

public class Crane {
    public int craneSectionID, topRightX,topRightY , bottomLeftX,bottomLeftY ,amount_of_dispatch_sections;
    private ArrayList<DispatchSection> dispatchSections;
    public ArrayList<Carrier> availableCarriers = new ArrayList<>();
    @Override
    public String toString() {
        return "Crane{" +
                "craneSectionID=" + craneSectionID +
                ", topRightX=" + topRightX +
                ", topRightY=" + topRightY +
                ", bottomLeftX=" + bottomLeftX +
                ", bottomLeftY=" + bottomLeftY +
                ", amount_of_dispatch_sections=" + amount_of_dispatch_sections +
                ", dispatchSections=" + dispatchSections +
                '}';
    }

    public Crane(int ID, int bottom_left_x , int bottom_left_y , int top_right_x , int top_right_y , int amount_of_dispatch_sections, ArrayList<DispatchSection> dispatchSections) {
        this.amount_of_dispatch_sections =  amount_of_dispatch_sections;
        this.dispatchSections = dispatchSections;
        this.craneSectionID = ID;
        this.bottomLeftX = bottom_left_x;
        this.bottomLeftY = bottom_left_y;
        this.topRightX = top_right_x;
        this.topRightY = top_right_y;
    }

    public DispatchSection getDispatchSections(int id) {
        return dispatchSections.get(id);
    }
}


