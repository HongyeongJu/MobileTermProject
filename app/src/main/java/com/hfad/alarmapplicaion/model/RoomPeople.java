package com.hfad.alarmapplicaion.model;

import java.io.Serializable;

public class RoomPeople implements Serializable {
    public boolean isTurnOff;
    public String id;

    public RoomPeople(){
        isTurnOff = false;
        id = null;
    }

    public RoomPeople(String id, boolean isTurnOff){
        this.isTurnOff = isTurnOff;
        this.id = id;
    }
}
