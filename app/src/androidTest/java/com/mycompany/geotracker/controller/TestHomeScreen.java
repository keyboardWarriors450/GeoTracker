/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.test.ActivityInstrumentationTestCase2;

import com.mycompany.geotracker.R;
import com.robotium.solo.Solo;

/**
 * Created by danielkhieuson on 6/1/15.
 */
public class TestHomeScreen extends ActivityInstrumentationTestCase2<HomeScreen> {

    private Solo solo;

    public TestHomeScreen() {
        super(HomeScreen.class);
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
     * Tests if the user logs in with the wrong password.
     */
    public void testLogInWithWrongPassword() {
        solo.unlockScreen();
        solo.enterText(0, "danielk6@uw.edu");
        solo.enterText(1, "password2");
        //solo.clickOnMenuItem("Login");
        solo.clickOnButton("Login");
        //solo.clickOnActionBarItem(R.id.action_login);
        boolean textFound = solo.searchText("Incorrect email or password");
        assertTrue("Correct password", textFound);
    }

    /**
     * Tests if the user logs in with the wrong user name.
     */
    /*public void testLogInWithWrongUserName() {
        //solo.unlockScreen();
        solo.enterText(0, "danielk5@uw.edu");
        solo.enterText(1, "password1");
        //solo.clickOnButton(R.id.action_login);
        solo.clickOnButton("Login");
//        solo.clickOnActionBarItem(R.id.action_login);
        boolean textFound = solo.searchText("Incorrect email or password");
        assertTrue("Correct username", textFound);
    }*/
}
