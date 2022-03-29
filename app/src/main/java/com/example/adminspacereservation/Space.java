package com.example.adminspacereservation;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// below line is for setting table name.
@Entity(tableName = "room_table")
public class Space {

    @PrimaryKey(autoGenerate = true)

    private int id;


    private String roomName;


    private String roomDescription;


    private String roomDuration;


    public Space(String roomName, String roomDescription, String roomDuration) {
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.roomDuration = roomDuration;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomDuration() {
        return roomDuration;
    }

    public void setRoomDuration(String roomDuration) {
        this.roomDuration = roomDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


