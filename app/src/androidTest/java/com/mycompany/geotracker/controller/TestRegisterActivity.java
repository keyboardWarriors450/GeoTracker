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
public class TestRegisterActivity extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private Solo solo;

    public TestRegisterActivity() {
        super(RegisterActivity.class);
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

    public void testRequiredFields() {
        solo.unlockScreen();

        solo.enterText(0, "");
        solo.clickOnButton("Submit");
        boolean textFound = solo.searchText("Please enter your email");
        assertTrue("Required field validation failed", textFound);
    }

    public void testOrientation() {
        solo.enterText(0, "danielk6@uw.edu");

        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("danielk6@uw.edu");
        assertTrue("Password change failed", textFound);

        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("danielk6@uw.edu");
        assertTrue("Password change failed", textFound);
    }

    public void testCourseAddButton() {
        solo.enterText(0, "danielk6@uw.edu");
        solo.clickOnButton("Submit");
        boolean textFound = solo.searchText("Email has been sent");
        assertTrue("Failed to send email", textFound);
    }
}
