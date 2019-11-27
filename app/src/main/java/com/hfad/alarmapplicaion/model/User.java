package com.hfad.alarmapplicaion.model;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String id;
    public String password;
    public int totalPoint;
    public int point;
    public boolean gender = false;

    public User(String name, String id, String passward, int totalPoint, int point, boolean gender){
        this.name = name;
        this.id = id;
        this.password = passward;
        this.totalPoint = totalPoint;
        this.point = point;
        this.gender = gender;
    }

    public User(){
        name = null;
        id = null;
        password = null;
        totalPoint = 0;
        point = 0;
        gender = false;
    }


}
