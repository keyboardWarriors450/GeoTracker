/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.model;

import junit.framework.TestCase;

/**
 * Created by danielkhieuson on 5/20/15.
 */
public class TestUser extends TestCase {

    private User mUser;

    public void setUp() {
        mUser = new User("userId", "email", "password", "question", "answer");
    }

    public void testUser() {
        User user = new User("userId", "email", "password", "question", "answer");
        assertNotNull(user);
    }

    public void testGetUserID() {
        assertEquals("danielk6", mUser.getUserID());
    }

    public void testGetEmail() {
        assertEquals("danielk6@uw.edu", mUser.getEmail());
    }

    public void testGetPassword() {
        assertEquals("password", mUser.getPassword());
    }

    public void testGetQuestion() {
        assertEquals("What is your favorite pet's name?", mUser.getQuestion());
    }

    public void testGetAnswer() {
        assertEquals("leo", mUser.getAnswer());
    }

}
