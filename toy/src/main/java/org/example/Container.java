package org.example;

public class Container {
    public int id, storageID, width, height;
    public Container(int id, int storageID){
        this.id = id;
        this.storageID = storageID;
        width = 2;
        height = 4;
        System.out.println("Carrier " + id + " has been created " + id + " " + storageID );

    }

}

