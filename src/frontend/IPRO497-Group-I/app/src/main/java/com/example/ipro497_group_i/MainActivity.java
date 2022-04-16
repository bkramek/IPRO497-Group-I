package com.example.ipro497_group_i;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ipro497_group_i.ui.OnSwipeTouchListener;
import com.example.ipro497_group_i.ui.checkinout.CheckInOutFragment;
import com.example.ipro497_group_i.ui.gallery.GalleryFragment;
import com.example.ipro497_group_i.ui.home.HomeFragment;
import com.example.ipro497_group_i.ui.slideshow.SlideshowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private static int slide;
    boolean admin = false;
    FirebaseDatabase db;
    BottomNavigationView bottomNavigationView;
    CheckInOutFragment qrFrag = new CheckInOutFragment();
    HomeFragment homeFrag = new HomeFragment();
    GalleryFragment adminFrag = new GalleryFragment();
    SlideshowFragment reserveFrag = new SlideshowFragment();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_bar);
        admin = false;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        /*slide = 2;

        ConstraintLayout relativeLayout = (ConstraintLayout) findViewById(R.id.container);
        if(admin){
            relativeLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            /*public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();

                    if(admin){

                    }else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reserveFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.reserve);
                        slide = 3;
                    }

            }
                @Override
                public void onSwipeRight() {
                    //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                    if(slide == 2){
                        //getSupportFragmentManager().beginTransaction().setCustomAnimations( R.anim.slide_out_left, R.anim.slide_out_right).replace(R.id.container, qrFrag).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, qrFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.scan);
                        slide = 1;
                    }
                    if(slide == 1){

                    }
                    if(slide == 3){
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        slide = 2;
                    }
                }
                @Override
                public void onSwipeLeft() {
                    //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                    if(slide == 2) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reserveFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.reserve);
                        slide = 3;
                    }
                    if(slide == 3){

                    }
                    if(slide == 1){
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        slide = 2;
                    }
                }

            });
        }if(!admin){
            relativeLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
                @Override
                public void onSwipeRight() {
                    //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                    if(slide == 2){
                        //getSupportFragmentManager().beginTransaction().setCustomAnimations( R.anim.slide_out_left, R.anim.slide_out_right).replace(R.id.container, qrFrag).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, qrFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.scan);
                        slide = 1;
                    }
                    if(slide == 1){

                    }
                    if(slide == 3){
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        slide = 2;
                    }
                }
                @Override
                public void onSwipeLeft() {
                    //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                    if(slide == 2) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reserveFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.reserve);
                        slide = 3;
                    }
                    if(slide == 3){

                    }
                    if(slide == 1){
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFrag).commit();
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        slide = 2;
                    }
                }

            });*/
        }

        /*
        Data data = new Data();
        db = FirebaseDatabase.getInstance();
        DatabaseReference bruh = db.getReference("Data");

        age = findViewById(R.id.age);
        time = findViewById(R.id.date);

        @Override
        public void onClick(View view) {

            int old = Integer.parseInt(age.getText().toString().trim());
            int peepNum = Integer.parseInt(time.getText().toString().trim());

            //data.setName(text_home.getText());
            data.setAge(old);
            data.setNum(peepNum);

            bruh.setValue(data);
            Snackbar.make(view, "Data Sent To DataBase", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

                bruh.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, qrFrag).commit();
                return true;

            case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFrag).commit();
                    return true;

            case R.id.reserve:
                if(admin){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, adminFrag).commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, reserveFrag).commit();
                }
                return true;
        }
        return false;
    }

    public static void setSlide(int num){
        slide = num;
    }
}


