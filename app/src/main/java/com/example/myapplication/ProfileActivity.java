package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Assuming you have TextViews in your activity_profile.xml to display the user's information
        TextView profileName = findViewById(R.id.profileName);
        TextView profileEmail = findViewById(R.id.profileEmail);
        TextView profilePhone = findViewById(R.id.profilePhone);

        // You can pass the user's information through the intent or fetch from the database again
        // For now, we will assume the intent has the user's info
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");

        profileName.setText(name);
        profileEmail.setText(email);
        profilePhone.setText(phone);
    }
}
