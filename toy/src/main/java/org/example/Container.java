package org.example;

import java.util.List;

public class Container {
    public int id, storageID;
    Storage storage ;
    public Container(int id, int storageID , List<Storage> storages){
        this.id = id;
        this.storageID = storageID;
        this.storage = storages.get(storageID); // imediately setting all the storages
        // withh en height niet voor alles opslaan , kan algemeen , mem efficiency

    }
    public Container(int id ) {
        this.id = id;
        this.storageID = -1;
        this.storage = null;
    }

    public int getX(){
        if (this.storage != null)
                return storage.x;
        return -1;
    }
    public int getY(){
        if (this.storage != null)
        return storage.y;
        return -1;
    }
    // mag denkik niet zo , je moet eest de conainer verzetten
    public void changeStorage(int storageID, Storage storage){
        if (this.storageID != storageID){
            System.out.println("you save the incorrect storage id with the incorrect storage ");

        }
        else {
            this.storageID = storage.id;
            this.storage = storage;
        }
    }
    public void changeStorage(int storageID){
        this.storageID = storageID;
        this.storage = Data.storage.get(storageID);
    }
    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", storageID=" + storageID +
                '}';
    }
}

