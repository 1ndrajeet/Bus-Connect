package com.indrajeet.buspass;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BusPass.db";
    private static final int DATABASE_VERSION = 2; // Increment version for upgrades

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UID = "uid"; // New column for UID
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_LOGGED_IN = "logged_in";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_UID + " TEXT, " + // Add UID column
            COLUMN_NAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_NUMBER + " TEXT, " +
            COLUMN_PASSWORD + " TEXT, " +
            COLUMN_LOGGED_IN + " INTEGER DEFAULT 0);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void insertUser(String uid, String name, String email, String number, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UID, uid);

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_LOGGED_IN, 1);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean isUserLoggedIn() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_LOGGED_IN + " = 1", null, null, null, null);
        boolean loggedIn = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return loggedIn;
    }

    public void clearUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGGED_IN, 0);
        db.update(TABLE_USERS, values, null, null);
        db.close();
    }

    public String getUserId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_UID}, COLUMN_LOGGED_IN + " = 1", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String uid = cursor.getString(cursor.getColumnIndex(COLUMN_UID));
            cursor.close();
            return uid;
        }
        return null; // Return null if no user is logged in
    }

    // Method to get user by UID
    public User getUserById(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_UID + " = ?", new String[]{uid}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            String uuid = cursor.getString(cursor.getColumnIndex(COLUMN_UID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

            cursor.close();
            return new User(uuid, name, email, number, password); // Return user object
        }
        return null;
    }
}
