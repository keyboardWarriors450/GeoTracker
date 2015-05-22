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
public class TestLocation extends TestCase {

    private Location mLocation;

    public void setUp() {
        mLocation = new Location("uid", "lat", "lon", "speed", "heading", 100);
    }

    public void testLocation() {
        Location location = new Location("uid", "lat", "lon", "speed", "heading", 100);
        assertNotNull(location);
    }

    public void testGetLat() {
        assertEquals("15", mLocation.getLat());
    }

    public void testSetLat() {
        mLocation.setLat("15");
        assertEquals("15", mLocation.getLat());
    }

    public void testGetLon() {
        assertEquals("16", mLocation.getLon());
    }

    public void testSetLon() {
        mLocation.setLon("16");
        assertEquals("16", mLocation.getLon());
    }

    public void testGetSpeed() {
        assertEquals("20", mLocation.getSpeed());
    }

    public void testSetSpeed() {
        mLocation.setSpeed("20");
        assertEquals("20", mLocation.getSpeed());
    }

    public void testGetHeading() {
        assertEquals("21", mLocation.getHeading());
    }

    public void testSetHeading() {
        mLocation.setHeading("21");
        assertEquals("21", mLocation.getHeading());
    }

    public void testGetTimestamp() {
        assertEquals(30, mLocation.getTimestamp());
    }

    public void testSetTimestamp() {
        mLocation.setTimestamp(30);
        assertEquals(30, mLocation.getTimestamp());
    }

    public void testGetUid() {
        assertEquals("23", mLocation.getUid());
    }

    public void testSetUid() {
        mLocation.setUid("23");
        assertEquals("23", mLocation.getUid());
    }

}
