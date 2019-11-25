package com.hfad.alarmapplicaion.model;

public class GroupMember {
    public int imageUrl;
    public String name;
    public String stateMessage;
    public boolean isWake =false;

    public GroupMember(int imageUrl, String name, String stateMessage, boolean isWake){
        this.imageUrl = imageUrl;
        this.name = name;
        this.stateMessage = stateMessage;
        this.isWake = isWake;
    }

}
