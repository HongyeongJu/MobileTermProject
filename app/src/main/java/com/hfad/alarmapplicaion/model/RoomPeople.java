package com.hfad.alarmapplicaion.model;

import java.io.Serializable;

public class RoomPeople implements Serializable {
    public boolean gender;
    public boolean isTurnOff;
    public String id;

    public RoomPeople(){
        isTurnOff = false;
        id = null;
        gender = false;
    }

    public RoomPeople(String id, boolean isTurnOff, boolean gender){
        this.isTurnOff = isTurnOff;
        this.id = id;
        this.gender = gender;
    }
}
