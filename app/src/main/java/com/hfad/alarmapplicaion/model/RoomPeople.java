package com.hfad.alarmapplicaion.model;

import java.io.Serializable;

// 알람방에 들어가 있는 사람들의 클래스
public class RoomPeople implements Serializable {
    public boolean gender;
    public boolean isTurnOff;
    public String id;
    public String phone;

    public RoomPeople(){
        isTurnOff = false;
        id = null;
        gender = false;
        phone = null;
    }

    public RoomPeople(String id, boolean isTurnOff, boolean gender, String phone){
        this.isTurnOff = isTurnOff;
        this.id = id;
        this.gender = gender;
        this.phone = phone;
    }
}
