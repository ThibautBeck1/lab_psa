package org.example;

import java.util.ArrayList;

public class Crane {
    private int craneSectionID, width, height;
    private ArrayList<dispatchSection> dispatchSections;
    public Crane(int craneSectionID, ArrayList<dispatchSection> dispatchSections) {
        width = 4;
        height = 2;
        this.dispatchSections = dispatchSections;
        this.craneSectionID = craneSectionID;
    }

}


