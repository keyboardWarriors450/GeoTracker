/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David on April 2015
 */
public class MyData
{

    private static final String DATABASE_NAME = "my.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_LOCAL = "local";

    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement insertStmt;
    private static final String INSERT = "INSERT INTO " + TABLE_NAME_LOCAL
            + "(user_name, password) VALUES (?, ?)";

    public MyData(Context context)
    {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }

    /** Inserts the user_name and password into local
     If successful, returns the rowid otherwise -1.
     */
    public long insert(String user_name, String password) throws Exception
    {
        this.insertStmt.bindString(1, user_name);
        this.insertStmt.bindString(2, password);

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
        this.db.delete(TABLE_NAME_LOCAL, null, null);
    }


    /**
     * Return an array list of Local objects from the
     * data returned from select query on example table.
     * @return
     */
    public ArrayList<Local> selectAll()
    {
        ArrayList<Local> list = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME_LOCAL, new String[]
                { "user_name", "password" }, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {

                Local e = new Local(cursor.getString(0), cursor.getString(1));
                list.add(e);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return list;
    }

    /**
     * Return the password when user_name is passed.
     * null if no record found.
     * @param user_name
     * @return
     */
    public String selectByID(String user_name)
    {
        Cursor cursor = this.db.query(TABLE_NAME_LOCAL, new String[]
                        { "password" }, "user_name=?",
                new String[]
                        { user_name }, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                return cursor.getString(0);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
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
            db.execSQL("CREATE TABLE " + TABLE_NAME_LOCAL
                    + " (user_name TEXT PRIMARY KEY, password TEXT, created_at DATETIME DEFAULT " +
                    "CURRENT_TIMESTAMP)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w("Local",
                    "Upgrading database, this will drop tables and recreate.");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCAL);
            onCreate(db);
        }
    }
}