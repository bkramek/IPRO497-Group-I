package com.example.ipro497_group_i;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipro497_group_i.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void errorPopup(String title, String msg) {
        AlertDialog.Builder aDBuilder = new AlertDialog.Builder(this);
        aDBuilder.setTitle(title);
        aDBuilder.setMessage(msg);
        aDBuilder.setIcon(R.drawable.ic_baseline_email_24);
        aDBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = aDBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                // Hash password
                String passwordHash = Hashing.sha256().hashString(passwordText, StandardCharsets.UTF_8).toString();

                // Check if user matches
                db.collection("users").whereEqualTo("username", usernameText)
                        .whereEqualTo("password_hash", passwordHash).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() < 1) {
                                        Log.v(TAG, "Login failed: username and password do not match.");
                                        errorPopup("Login failed!", "The username and password do not match.");
                                    } else {
                                        Intent intent = new Intent(getBaseContext(), HomeFragment.class);
                                        intent.putExtra("userId", task.getResult().getDocuments().get(0).getLong("user_id"));
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        });

        TextView registerButton = (TextView) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });



    }
}