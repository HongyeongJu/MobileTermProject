package com.hfad.alarmapplicaion.model;

public class User {
    public String name;
    public String id;
    public String password;
    public int totalPoint;
    public int point;

    public User(String name, String id, String passward, int totalPoint, int point){
        this.name = name;
        this.id = id;
        this.password = passward;
        this.totalPoint = totalPoint;
        this.point = point;
    }

    public User(){
        name = null;
        id = null;
        password = null;
        totalPoint = 0;
        point = 0;
    }


}
