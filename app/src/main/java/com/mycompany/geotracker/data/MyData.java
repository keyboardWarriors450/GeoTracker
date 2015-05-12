/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mycompany.geotracker.model.Location;
import com.mycompany.geotracker.model.User;

import java.util.ArrayList;

/**
 * Created by David on April 2015
 */
public class MyData
{

    private static final String DATABASE_NAME = "my.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_LOC = "location";

    private SQLiteDatabase db;

    private SQLiteStatement insertStmt1;
    private SQLiteStatement insertStmt2;
    private static final String INSERT_USER = "INSERT INTO " + TABLE_NAME_USER
            + "(uid, email, password, secretQuestion, secretAnswer) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_LOC = "INSERT INTO " + TABLE_NAME_LOC
            + "(uid, lat, lon, speed, heading, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

    public MyData(Context context)
    {
        OpenHelper openHelper = new OpenHelper(context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt1 = this.db.compileStatement(INSERT_USER);
        this.insertStmt2 = this.db.compileStatement(INSERT_LOC);
    }

    /** Inserts the uid, email, password, secretQuestion, secretAnswer into User
     If successful, returns the rowid otherwise -1.
     */
    public long insertUser(String uid, String email, String password, String question, String answer)
            throws Exception
    {
        this.insertStmt1.bindString(1, uid);
        this.insertStmt1.bindString(2, email);
        this.insertStmt1.bindString(3, password);
        this.insertStmt1.bindString(4, question);
        this.insertStmt1.bindString(5, answer);

        long rowID = this.insertStmt1.executeInsert();
        if (rowID == -1) {
            throw new Exception("Unable to insert");
        }
        return rowID;
    }

    /** Inserts the uid, latitude, longitude, speed, heading, and timestamp into Location
     If successful, returns the rowid otherwise -1.
     */
    public long insertLocation(String uid, String lat, String lon, String speed, String heading,
                               long timestamp) throws Exception
    {
        this.insertStmt2.bindString(1, uid);
        this.insertStmt2.bindString(2, lat);
        this.insertStmt2.bindString(3, lon);
        this.insertStmt2.bindString(4, speed);
        this.insertStmt2.bindString(5, heading);
        this.insertStmt2.bindLong(6, timestamp);

        long rowID = this.insertStmt2.executeInsert();
        if (rowID == -1) {
            throw new Exception("Unable to insert");
        }
        return rowID;
    }

    /**
     * Delete everything from example
     */
    public void deleteAll()
    {
        this.db.delete(TABLE_NAME_USER, null, null);
        this.db.delete(TABLE_NAME_LOC, null, null);
    }


    /**
     * Return an array list of User objects from the
     * data returned from select query on example table.
     * @return list
     */
    public ArrayList<User> selectAllUsers()
    {
        ArrayList<User> list = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_USER, new String[]
                { "uid", "email", "password", "secretQuestion", "secretAnswer" },
                null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                User e = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4));
                list.add(e);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }

    /**
     * Return an array list of Location objects from the
     * data returned from select query on example table.
     * @return list
     */
    public ArrayList<Location> selectAllLocations()
    {
        ArrayList<Location> list = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_LOC, new String[]
                        { "uid", "lat", "lon", "speed", "heading", "timestamp" },
                null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Location e = new Location(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getLong(5));
                list.add(e);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }

//    /**
//     * Return the password when email is passed.
//     * null if no record found.
//     * @param uid userID
//     * @return null
//     */
//    public String selectByID(String uid)
//    {
//        Cursor cursor = this.db.query(TABLE_NAME_USER, new String[]
//                { "password" }, "uid=?", new String[] { uid }, null, null, null);
//        if (cursor.moveToFirst())
//        {
//            do
//            {
//                return cursor.getString(0);
//            } while (cursor.moveToNext());
//        }
//        if (!cursor.isClosed())
//        {
//            cursor.close();
//        }
//        return null;
//    }

    /**
     * Close the connection
     */
    public void close()
    {
        db.close();
    }

    private static class OpenHelper extends SQLiteOpenHelper
    {

        OpenHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + TABLE_NAME_USER
               + " (uid TEXT PRIMARY KEY, email TEXT, password TEXT, secretQuestion TEXT, " +
                    "secretAnswer TEXT)");

            db.execSQL("CREATE TABLE " + TABLE_NAME_LOC
                    + " (uid TEXT PRIMARY KEY, lat TEXT, lon TEXT, speed TEXT, " +
                    "heading TEXT, timestamp INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.i("User",
                    "Upgrading database, this will drop tables and recreate.");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOC);
            onCreate(db);
        }
    }
}