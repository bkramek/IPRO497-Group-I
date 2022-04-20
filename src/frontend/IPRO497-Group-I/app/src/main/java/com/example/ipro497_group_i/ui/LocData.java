package com.example.ipro497_group_i.ui;

import com.example.ipro497_group_i.ui.home.HomeFragment;

import java.util.ArrayList;

public class LocData {

    String building, room, desc;



    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public LocData(String building, String room, String desc) {
        this.building = building;
        this.room = room;
        this.desc = desc;
    }




}
