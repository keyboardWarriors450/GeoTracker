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

    private SQLiteDatabase db;

    private SQLiteStatement insertStmt;
    private static final String INSERT = "INSERT INTO " + TABLE_NAME_USER
            + "(email, password, secretQuestion, secretAnswer) VALUES (?, ?, ?, ?)";

    public MyData(Context context)
    {
        OpenHelper openHelper = new OpenHelper(context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }

    /** Inserts the email, password, secretQuestion, secretAnswer into User
     If successful, returns the rowid otherwise -1.
     */
    public long insert(String email, String password, String question, String answer) throws Exception
    {
        this.insertStmt.bindString(1, email);
        this.insertStmt.bindString(2, password);
        this.insertStmt.bindString(3, question);
        this.insertStmt.bindString(4, answer);

        long rowID = this.insertStmt.executeInsert();
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
    }


    /**
     * Return an array list of User objects from the
     * data returned from select query on example table.
     * @return list
     */
    public ArrayList<User> selectAll()
    {
        ArrayList<User> list = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_USER, new String[]
                { "email", "password", "secretQuestion", "secretAnswer" }, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                User e = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3));
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
     * Return the password when email is passed.
     * null if no record found.
     * @param email user's email
     * @return null
     */
    public String selectByID(String email)
    {
        Cursor cursor = this.db.query(TABLE_NAME_USER, new String[]
                        { "password" }, "email=?",
                new String[]
                        { email }, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                return cursor.getString(0);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed())
        {
            cursor.close();
        }
        return null;
    }

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
               + " (email TEXT PRIMARY KEY, password TEXT, secretQuestion TEXT, secretAnswer TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w("User",
                    "Upgrading database, this will drop tables and recreate.");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
            onCreate(db);
        }
    }
}