package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPasswordEditText, newPasswordEditText, retypePasswordEditText;
    Button changePasswordSubmitButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPasswordEditText = findViewById(R.id.changePasswordCurrent);
        newPasswordEditText = findViewById(R.id.changePasswordNew);
        retypePasswordEditText = findViewById(R.id.changePasswordRetype);
        changePasswordSubmitButton = findViewById(R.id.changePasswordSubmit);

        databaseHelper = new DatabaseHelper(this);

        changePasswordSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String retypePassword = retypePasswordEditText.getText().toString();

                // Ensure all fields are filled
                if (currentPassword.isEmpty() || newPassword.isEmpty() || retypePassword.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ensure new password matches retyped password
                if (!newPassword.equals(retypePassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the logged-in user's email from the intent
                String email = getIntent().getStringExtra("email");

                // Check if the current password is correct
                if (!databaseHelper.checkEmailPassword(email, currentPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Change the password
                boolean isPasswordChanged = databaseHelper.changePassword(email, newPassword);
                if (isPasswordChanged) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Error changing password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
