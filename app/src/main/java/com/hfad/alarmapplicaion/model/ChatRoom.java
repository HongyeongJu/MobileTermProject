package com.hfad.alarmapplicaion.model;

import java.io.Serializable;
import java.util.List;

// 알람룸인데. 오타뜸.
public class ChatRoom implements Serializable {

    public long number = 0;
    public String roomTitle;       // 방제목
    public int hour;           // 시
    public int minute;         // 분
    public String owner;       // 주인
    public List<RoomPeople> peoples ;
    public boolean monday;
    public boolean tuesday;
    public boolean wednesday;
    public boolean thursday;
    public boolean friday;
    public boolean saturday;
    public boolean sunday;

    public ChatRoom(){
        roomTitle = null;
        hour = 0;
        minute = 0;
        owner = null;
        peoples = null;
        monday= false;
        tuesday = false;
        wednesday = false;
        thursday = false;
        friday = false;
        saturday = false;
        sunday = false;
    }
    public ChatRoom(String roomTitle, int hour, int minute, String owner, List<RoomPeople> peoples , boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun){
        this.roomTitle = roomTitle;
        this.hour = hour;
        this.minute = minute;
        this.owner = owner;
        this.peoples = peoples;
        this.monday = mon;
        this.tuesday = tue;
        this.wednesday = wed;
        this.thursday = thu;
        this.friday = fri;
        this.saturday = sat;
        this.sunday = sun;
    }

    public void setNumber(long number){
        this.number = number;
    }
}
