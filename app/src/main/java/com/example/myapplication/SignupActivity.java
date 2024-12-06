package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private EditText signupFirstName;
    private EditText signupLastName;
    private EditText signupPhone;
    private EditText signupEmail;
    private EditText signupPassword;
    private EditText signupConfirm;
    private Button signupButton;
    private TextView loginRedirectText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseHelper = new DatabaseHelper(this);

        signupFirstName = findViewById(R.id.signupFirstName);
        signupLastName = findViewById(R.id.signupLastName);
        signupPhone = findViewById(R.id.signupPhone);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupConfirm = findViewById(R.id.signupConfirm);
        signupButton = findViewById(R.id.signupButton);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = signupFirstName.getText().toString().trim();
                String lastName = signupLastName.getText().toString().trim();
                String phone = signupPhone.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String confirmPassword = signupConfirm.getText().toString().trim();

                Log.d("SignupActivity", "FirstName: " + firstName);
                Log.d("SignupActivity", "LastName: " + lastName);
                Log.d("SignupActivity", "Phone: " + phone);
                Log.d("SignupActivity", "Email: " + email);
                Log.d("SignupActivity", "Password: " + password);
                Log.d("SignupActivity", "Confirm Password: " + confirmPassword);

                if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);
                        Log.d("SignupActivity", "Check User Email: " + checkUserEmail);

                        if (!checkUserEmail) {
                            Boolean insert = databaseHelper.insertData(email, password, firstName, lastName, phone);
                            Log.d("SignupActivity", "Insert Data: " + insert);

                            if (insert) {
                                Toast.makeText(SignupActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                                Log.d("SignupActivity", "Signup Successful");

                                // Pass the user data to ProfileActivity
                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                intent.putExtra("name", firstName + " " + lastName);
                                intent.putExtra("email", email);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                                Log.d("SignupActivity", "Signup Failed");
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                            Log.d("SignupActivity", "User already exists");
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                        Log.d("SignupActivity", "Passwords do not match");
                    }
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
