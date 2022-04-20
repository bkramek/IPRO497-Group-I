package com.example.ipro497_group_i;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.ipro497_group_i.databinding.ActivityMainBinding;
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
    String TAG = "MainActivity";

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

        Intent intent = getIntent();
        Long username = intent.getLongExtra("userId", 0);
        Log.d(TAG, ""+username);

        admin = false;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        }

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


