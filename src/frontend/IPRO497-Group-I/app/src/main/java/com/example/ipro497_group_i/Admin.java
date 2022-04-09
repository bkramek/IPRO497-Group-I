package com.example.ipro497_group_i;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Admin extends AppCompatActivity {
    // creating a variables for our recycler view.
    private RecyclerView RoomRV;
    private static final int ADD_ROOM_REQUEST = 1;
    private static final int EDIT_ROOM_REQUEST = 2;
    private ViewModal viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        // initializing our variable for our recycler view and fab.
        RoomRV = findViewById(R.id.idRVrooms);
        FloatingActionButton fab = findViewById(R.id.idRoomAdd);

        // adding on click listener for floating action button.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starting a new activity for adding a new course
                // and passing a constant value in it.
                Intent intent = new Intent(Admin.this, NewSpaceActivity.class);

                startActivityForResult(intent, ADD_ROOM_REQUEST);
            }
        });

        // setting layout manager to our adapter class.
        RoomRV.setLayoutManager(new LinearLayoutManager(this));
        RoomRV.setHasFixedSize(true);

        // initializing adapter for recycler view.
        final RoomRVAdapter adapter = new RoomRVAdapter();

        // setting adapter class for recycler view.
        RoomRV.setAdapter(adapter);

        // passing a data from view modal.
        //viewmodal = ViewModelProviders.of(this).get(ViewModal.class);

        // below line is use to get all the courses from view modal.
        viewmodal.getAllRooms().observe(this, new Observer<List<Space>>() {
            @Override
            public void onChanged(List<Space> models) {
                // when the data is changed in our models we are
                // adding that list to our adapter class.
                adapter.submitList(models);
            }
        });
        // below method is use to add swipe to delete method for item of recycler view.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // on recycler view item swiped then we are deleting the item of our recycler view.
                viewmodal.delete(adapter.getCourseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(Admin.this, "Course deleted", Toast.LENGTH_SHORT).show();
            }
        }).
                // below line is use to attach this to recycler view.
                        attachToRecyclerView(RoomRV);
        // below line is use to set item click listener for our item of recycler view.
        adapter.setOnItemClickListener(new RoomRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Space model) {
                // after clicking on item of recycler view
                // we are opening a new activity and passing
                // a data to our activity.
                Intent intent = new Intent(Admin.this, NewSpaceActivity.class);
                intent.putExtra(NewSpaceActivity.EXTRA_ID, model.getId());
                intent.putExtra(NewSpaceActivity.EXTRA_ROOM_NAME, model.getRoomName());
                intent.putExtra(NewSpaceActivity.EXTRA_DESCRIPTION, model.getRoomDescription());
                intent.putExtra(NewSpaceActivity.EXTRA_DURATION, model.getRoomDuration());

                // below line is to start a new activity and
                // adding a edit course constant.
                startActivityForResult(intent, EDIT_ROOM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ROOM_REQUEST && resultCode == RESULT_OK) {
            String roomName = data.getStringExtra(NewSpaceActivity.EXTRA_ROOM_NAME);
            String roomDescription = data.getStringExtra(NewSpaceActivity.EXTRA_DESCRIPTION);
            String roomDuration = data.getStringExtra(NewSpaceActivity.EXTRA_DURATION);
            Space model = new Space(roomName, roomDescription, roomDuration);
            viewmodal.insert(model);
            Toast.makeText(this, "Space saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_ROOM_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(NewSpaceActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Course can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String roomName = data.getStringExtra(NewSpaceActivity.EXTRA_ROOM_NAME);
            String roomDesc = data.getStringExtra(NewSpaceActivity.EXTRA_DESCRIPTION);
            String roomDuration = data.getStringExtra(NewSpaceActivity.EXTRA_DURATION);
            Space model = new Space(roomName, roomDesc, roomDuration);
            model.setId(id);
            viewmodal.update(model);
            Toast.makeText(this, "Space updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Space not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
