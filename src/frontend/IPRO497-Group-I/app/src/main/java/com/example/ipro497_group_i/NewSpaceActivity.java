package com.example.ipro497_group_i;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewSpaceActivity extends AppCompatActivity {

    // creating a variables for our button and edittext.
    private EditText roomNameEdt, roomDescEdt, roomDurationEdt;
    private Button roomBtn;

    // creating a constant string variable for our
    // course name, description and duration.
    public static final String EXTRA_ID = "com.gtappdevelopers.gfgroomdatabase.EXTRA_ID";
    public static final String EXTRA_ROOM_NAME = "com.gtappdevelopers.gfgroomdatabase.EXTRA_COURSE_NAME";
    public static final String EXTRA_DESCRIPTION = "com.gtappdevelopers.gfgroomdatabase.EXTRA_COURSE_DESCRIPTION";
    public static final String EXTRA_DURATION = "com.gtappdevelopers.gfgroomdatabase.EXTRA_COURSE_DURATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_space);

        // initializing our variables for each view.
        roomNameEdt = findViewById(R.id.idEdtRoomName);
        roomDescEdt = findViewById(R.id.idEdtRoomDescription);
        roomDurationEdt = findViewById(R.id.idEdtRoomDuration);
        roomBtn = findViewById(R.id.idBtnSaveRoom);

        // below line is to get intent as we
        // are getting data via an intent.
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // if we get id for our data then we are
            // setting values to our edit text fields.
            roomNameEdt.setText(intent.getStringExtra(EXTRA_ROOM_NAME));
            roomDescEdt.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            roomDurationEdt.setText(intent.getStringExtra(EXTRA_DURATION));
        }
        // adding on click listener for our save button.
        roomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting text value from edittext and validating if
                // the text fields are empty or not.
                String roomName = roomNameEdt.getText().toString();
                String roomDesc = roomDescEdt.getText().toString();
                String roomDuration = roomDurationEdt.getText().toString();
                if (roomName.isEmpty() || roomDesc.isEmpty() || roomDuration.isEmpty()) {
                    Toast.makeText(NewSpaceActivity.this, "Please enter the valid space details.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to save our course.
                saveCourse(roomName, roomDesc, roomDuration);
            }
        });
    }

    private void saveCourse(String roomName, String roomDescription, String roomDuration) {
        // inside this method we are passing
        // all the data via an intent.
        Intent data = new Intent();

        // in below line we are passing all our course detail.
        data.putExtra(EXTRA_ROOM_NAME, roomName);
        data.putExtra(EXTRA_DESCRIPTION, roomDescription);
        data.putExtra(EXTRA_DURATION, roomDuration);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            // in below line we are passing our id.
            data.putExtra(EXTRA_ID, id);
        }

        // at last we are setting result as data.
        setResult(RESULT_OK, data);

        // displaying a toast message after adding the data
        Toast.makeText(this, "Space has been saved to Room Database. ", Toast.LENGTH_SHORT).show();
    }
}

