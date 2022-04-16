package com.example.ipro497_group_i.ui.slideshow;

public class ReserveData {

    String time, date, building, room;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public ReserveData(String time, String date, String building, String room) {
        this.time = time;
        this.date = date;
        this.building = building;
        this.room = room;
    }

}
