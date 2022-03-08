package com.example.newloginregisterpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        findViewById(R.id.button1).setOnClickListener(
                v-> {
                   startActivity(new Intent(MainActivity.this, Login_Activity.class));
                }

        );
        findViewById(R.id.button).setOnClickListener(
                v-> {
                    startActivity(new Intent(MainActivity.this, Sign_up_Activity.class));
                }
        );
    }
}