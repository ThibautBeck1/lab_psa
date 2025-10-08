package org.example;

import java.util.ArrayList;

public class Crane {
    private int craneSectionID, width, height, topRightX,topRightY , bottomLeftX,bottomLeftY ;
    private ArrayList<DispatchSection> dispatchSections;
    public Crane(int ID, int bottom_left_x , int bottom_left_y , int top_right_x , int top_right_y , int amount_of_dispatch_sections, ArrayList<DispatchSection> dispatchSections) {
        width = 4;
        height = 2;
        this.dispatchSections = dispatchSections;
        this.craneSectionID = ID;
        this.bottomLeftX = bottom_left_x;
        this.bottomLeftY = bottom_left_y;
        this.topRightX = top_right_x;
        this.topRightY = top_right_y;
        System.out.println("Crane created");

    }

}


