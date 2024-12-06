package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up the Toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Info");

        TextView profileName = findViewById(R.id.profileName);
        TextView profileEmail = findViewById(R.id.profileEmail);
        TextView profilePhone = findViewById(R.id.profilePhone);
        Button changePasswordButton = findViewById(R.id.changePasswordButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");

        Log.d("ProfileActivity", "Name: " + name);
        Log.d("ProfileActivity", "Email: " + email);
        Log.d("ProfileActivity", "Phone: " + phone);

        profileName.setText(name);
        profileEmail.setText(email);
        profilePhone.setText(phone);

        databaseHelper = new DatabaseHelper(this);

        // Log all users to the console
        databaseHelper.logAllUsers();

        changePasswordButton.setOnClickListener(v -> {
            Intent changePasswordIntent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            changePasswordIntent.putExtra("email", email);
            startActivity(changePasswordIntent);
        });
    }
}
