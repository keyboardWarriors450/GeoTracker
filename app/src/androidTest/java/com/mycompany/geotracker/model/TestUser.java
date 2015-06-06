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

    /**
     * Sets up the test with example user info.
     */
    public void setUp() {
        mUser = new User("danielk6", "danielk6@uw.edu", "password1", "What is your favorite pet's name?", "leo");
    }

    /**
     * Tests an example user.
     */
    public void testUser() {
        User user = new User("danielk6", "danielk6@uw.edu", "password1", "What is your favorite pet's name?", "leo");
        assertNotNull(user);
    }

    /**
     * Tests the example user id.
     */
    public void testGetUserID() {
        assertEquals("danielk6", mUser.getUserID());
    }

    /**
     * Tests the example email.
     */
    public void testGetEmail() {
        assertEquals("danielk6@uw.edu", mUser.getEmail());
    }

    /**
     * Tests the example password.
     */
    public void testGetPassword() {
        assertEquals("password1", mUser.getPassword());
    }

    /**
     * Tests the example security question.
     */
    public void testGetQuestion() {
        assertEquals("What is your favorite pet's name?", mUser.getQuestion());
    }

    /**
     * Tests the example security answer.
     */
    public void testGetAnswer() {
        assertEquals("leo", mUser.getAnswer());
    }

}
