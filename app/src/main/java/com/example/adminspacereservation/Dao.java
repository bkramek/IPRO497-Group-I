package com.example.adminspacereservation;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@androidx.room.Dao
public interface Dao {


    // add data to database.
    @Insert
    void insert(Space model);


    // the data in our database.
    @Update
    void update(Space model);


    // specific course in our database.
    @Delete
    void delete(Space model);


    // delete all courses from our database.
    @Query("DELETE FROM room_table")
    void deleteAllRooms();

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM room_table ORDER BY roomName ASC")
    LiveData<List<Space>> getAllRooms();
}
