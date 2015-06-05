/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by danielkhieuson on 5/20/15.
 */
public class TestShowMovementDataActivity extends ActivityInstrumentationTestCase2<ShowMovementDataActivity> {

    private Solo solo;

    public TestShowMovementDataActivity() {
        super(ShowMovementDataActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    /*public void testMovementDataShowsUp() {
        solo.unlockScreen();
        solo.clickOnText("Start Date");
        solo.clickOnButton("Done");
        solo.clickOnText("End Date");
        solo.setDatePicker(0, 2016, 5, 4);
        solo.clickOnButton("Done");
        solo.clickOnButton("Show Movement Data");
        boolean textFound1 = solo.searchText("Downloading..");
        assertTrue("Movement Data retrieved", textFound1);
        //boolean textFound2 = solo.searchText(" Timestamp       Latitude        Longitude    Heading\n\n");
        //assertTrue("Movement Data retrieved", textFound2);
    }*/

    public void testMovementDataShowsUp() {
        solo.unlockScreen();
        boolean textFound = solo.searchText(" Timestamp       Latitude        Longitude    Heading\n\n");
        assertTrue("Movement Data retrieved", textFound);
    }

}
