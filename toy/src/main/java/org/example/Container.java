package org.example;

public class Container {
    public int id, storageID;
    public Container(int id, int storageID){
        this.id = id;
        this.storageID = storageID;
        // withh en height niet voor alles opslaan , kan algemeen , mem efficiency

    }

    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", storageID=" + storageID +
                '}';
    }
}

