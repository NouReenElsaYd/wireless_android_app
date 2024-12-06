package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SignLog.db";
    public static final String TABLE_NAME = "users";
    private SQLiteDatabase MyDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        MyDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(email TEXT PRIMARY KEY, password TEXT, firstName TEXT, lastName TEXT, phone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert data with SHA-256 hashed password
    public boolean insertData(String email, String password, String firstName, String lastName, String phone) {
        String hashedPassword = hashPassword(password);  // Hash the password here
        if (hashedPassword == null) {
            Log.e("DatabaseHelper", "Password hashing failed");
            return false;  // Exit early if hashing fails
        }
        Log.d("DatabaseHelper", "Hashed password: " + hashedPassword);  // Verify the hash
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", hashedPassword);  // Store the hashed password
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("phone", phone);
        long result = MyDatabase.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Hash the password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');  // Ensure 2 digits
                hexString.append(hex);
            }
            return hexString.toString();  // Return the hashed password as a hex string
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Check if email exists
    public boolean checkEmail(String email) {
        boolean exists = false;
        Cursor cursor = null;
        try {
            cursor = MyDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email = ?", new String[]{email});
            exists = cursor.getCount() > 0;
            Log.d("DatabaseHelper", "Email check for " + email + ": " + exists);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error checking email: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return exists;
    }

    // Verify email and password using SHA-256
    public boolean checkEmailPassword(String email, String password) {
        Cursor cursor = MyDatabase.rawQuery("SELECT password FROM " + TABLE_NAME + " WHERE email = ?", new String[]{email});
        boolean valid = false;
        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            Log.d("DatabaseHelper", "Stored hash: " + storedHash);
            String inputHash = hashPassword(password);  // Hash the entered password
            Log.d("DatabaseHelper", "Input hash: " + inputHash);
            valid = inputHash != null && inputHash.equals(storedHash);  // Compare hashes
        }
        cursor.close();
        return valid;
    }

    public String getUserName(String email) {
        Cursor cursor = MyDatabase.rawQuery("SELECT firstName, lastName FROM " + TABLE_NAME + " WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"));
            cursor.close();
            return firstName + " " + lastName;
        }
        cursor.close();
        return null;
    }

    public String getUserPhone(String email) {
        Cursor cursor = MyDatabase.rawQuery("SELECT phone FROM " + TABLE_NAME + " WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            cursor.close();
            return phone;
        }
        cursor.close();
        return null;
    }

    // Method to change the password
    public boolean changePassword(String email, String newPassword) {
        String hashedPassword = hashPassword(newPassword);  // Hash the new password
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", hashedPassword);
        int result = MyDatabase.update(TABLE_NAME, contentValues, "email = ?", new String[]{email});
        return result > 0;
    }

    // Method to get all users and log them
    public void logAllUsers() {
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                Log.d("DatabaseHelper", "User: " + email + ", Password Hash: " + password);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
