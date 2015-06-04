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
public class TestViewLocations extends ActivityInstrumentationTestCase2<ViewLocations> {

    private Solo solo;

    public TestViewLocations() {
        super(ViewLocations.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        //currentActivity = getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    /**
     * Tests if the user selects the same dates for start and end date.
     */
    public void testSelectSameDates() {
        solo.unlockScreen();

        solo.clickOnText("Start Date");
        solo.clickOnButton("Done");
        solo.clickOnText("End Date");
        solo.clickOnButton("Done");

        boolean textFound = solo.searchText("End Date must greater than Start Date");
        assertTrue("Dates are the same", textFound);
    }

    /**
     * Tests if the user selects a start date that is greater than the end date.
     */
    public void testSelectGreaterStartDate() {
        solo.clickOnText("Start Date");
        solo.clickOnButton("Done");
        solo.clickOnText("End Date");
        //solo.pressSpinnerItem(0, -1);
        //DatePicker datePicker = (DatePicker)currentActivity.findViewById(R.id.datePicker1);
        //solo.setDatePicker(datePicker, 2015, 5, 4);
        solo.clickOnButton("Done");
        boolean textFound = solo.searchText("End Date must greater than Start Date");
        assertTrue("End date is less than start date", textFound);
    }
}
