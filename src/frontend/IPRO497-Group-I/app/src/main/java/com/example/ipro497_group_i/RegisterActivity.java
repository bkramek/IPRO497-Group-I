package com.example.ipro497_group_i;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import android.content.Intent;

import com.google.common.hash.Hashing;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";

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

    public void attemptRegistration(String email, String username, String password) {

        // Verify email is not taken
        db.collection("users").whereEqualTo("email_address", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.v(TAG, "Email task result size: " + String.valueOf(task.getResult().size()));
                            if (task.getResult().size() != 0) {
                                Log.v(TAG, "Email address " + email + " already exists.");
                                errorPopup("Email taken!", "This email address has already been registered.");
                            } else {
                                // Verify username is not taken
                                db.collection("users").whereEqualTo("username", username).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Log.v(TAG, "Username task result size: " + String.valueOf(task.getResult().size()));
                                                    if (task.getResult().size() != 0) {
                                                        Log.v(TAG, "Username " + username + " already exists.");
                                                        errorPopup("Username taken!", "This username has already been registered.");
                                                    } else {
                                                        Log.v(TAG, "Registering");

                                                        // Get user count for ID
                                                        final long[] userCount = new long[1];
                                                        db.collection("data").document("globaldata").get()
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot gdDoc = task.getResult();
                                                                            userCount[0] = gdDoc.getLong("user_count");
                                                                            db.collection("data").document("globaldata").update("user_count", userCount[0] + 1);
                                                                            // Hash password
                                                                            String passwordHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

                                                                            Map<String, Object> userData = new HashMap<>();
                                                                            userData.put("username", username);
                                                                            userData.put("email_address", email);
                                                                            userData.put("password_hash", passwordHash);
                                                                            userData.put("user_id", userCount[0] + 1);
                                                                            userData.put("permission", 1);

                                                                            // Create user document
                                                                            db.collection("users").document(String.valueOf(userCount[0] + 1)).set(userData);
                                                                            errorPopup("Registration successful!", "You may now log in.");
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //attemptRegistration("bkramek@hawk.iit.edu", "bkramek", "Hell0World!");

        EditText username = (EditText) findViewById(R.id.username);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText repassword = (EditText) findViewById(R.id.repassword);

        MaterialButton regbtn = (MaterialButton) findViewById(R.id.signupbtn);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String username1 = username.getText().toString();
                Toast.makeText(RegisterActivity.this,"Username is"+username1,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);*/
                String usernameText = username.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                // Verify email is valid
                if (!Pattern.matches("^*?.\\S{1,64}@^*?.+$", emailText)) {
                    errorPopup("Invalid email!", "This email address doesn't look right.");
                    Log.v(TAG, "Invalid email format");
                    return;
                }

                // Verify password is valid
                if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", passwordText)) {
                    errorPopup("Invalid password!", "Passwords must be at least 8 characters, with one uppercase, one lowercase, one number, and one special character.");
                    Log.v(TAG, "Invalid password format");
                    return;
                }

                // Verify password entered twice
                if (!(passwordText.equals(repassword.getText().toString()))) {
                    errorPopup("Passwords don't match!", "Please re-enter your password exactly.");
                    Log.v(TAG, "Passwords don't match");
                    return;
                }

                attemptRegistration(emailText, usernameText, passwordText);

            }
        });

        TextView backButton = (TextView) findViewById(R.id.registerback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}