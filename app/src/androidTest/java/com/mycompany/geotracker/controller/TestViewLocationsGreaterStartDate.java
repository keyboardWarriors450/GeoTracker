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
 * Created by danielkhieuson on 6/4/15.
 */
public class TestViewLocationsGreaterStartDate extends ActivityInstrumentationTestCase2<ViewLocations> {

    private Solo solo;

    public TestViewLocationsGreaterStartDate() {
        super(ViewLocations.class);
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

    /**
     * Tests if the user selects a start date that is greater than the end date.
     */
    public void testSelectGreaterStartDate() {
        solo.clickOnText("Start Date");
        solo.clickOnButton("Done");
        solo.clickOnText("End Date");
        solo.setDatePicker(0, 2014, 5, 4);
        solo.clickOnButton("Done");
        boolean textFound = solo.searchText("End Date must greater than Start Date");
        assertTrue("End date is less than start date", textFound);
    }
}
