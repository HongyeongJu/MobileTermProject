package com.hfad.alarmapplicaion.model;

import java.io.Serializable;

public class Shop implements Serializable {

    public String itemName;
    public String price;
    public String url;

    public Shop(String itemName, String price, String url){
        this.itemName = itemName;
        this.price = price;
        this.url = url;
    }

    public Shop(){
        this.itemName = null;
        this.price = null;
        this.url = null;
    }
}
