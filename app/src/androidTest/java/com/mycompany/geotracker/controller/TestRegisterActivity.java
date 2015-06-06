/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;

/**
 * Created by danielkhieuson on 5/20/15.
 */
public class TestRegisterActivity extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private Solo solo;

    /**
     * The constructor.
     */
    public TestRegisterActivity() {
        super(RegisterActivity.class);
    }

    /**
     * Sets up the test.
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * Stops the app after the test is finished.
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    /**
     * Tests if the user can register while leaving fields blank.
     */
    public void testRequiredFields() {
        solo.unlockScreen();
        solo.clickOnButton("I Accept");
        solo.enterText(0, "");
        solo.clickOnButton("Continue");
        boolean textFound = solo.searchText("Cannot leave it blank");
        assertTrue("Required field validation failed", textFound);
    }

    /**
     * Tests registering a user.
     * To perform this test, you will need to change the email String.
     * If you plan to test this multiple times, you will need to clear the app's
     * settings in your emulator before you run this test.
     * Otherwise, the first time you run this test, it will pass but
     * subsequent times will fail.
     */
    public void testSubmitButton() {
        String email = "blah6656@uujhjjyy.com";
        String password = "password1";
        String password2 = "password1";
        String question = "What is your favorite pet's name?";
        String answer = "leo";

        solo.clickOnButton("I Accept");
        solo.enterText(0, email);
        solo.enterText(1, password);
        solo.enterText(2, password2);
        solo.pressSpinnerItem(0, 0);
        solo.enterText(3, answer);
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Continue");
        try {

            String link = Uri.parse("http://450.atwebpages.com/adduser.php").buildUpon()
                    .appendQueryParameter("email", email)
                    .appendQueryParameter("password", password)
                    .appendQueryParameter("question", question)
                    .appendQueryParameter("answer", answer)
                    .build().toString();

            link = link.replaceFirst("%40", "@");

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);

        } catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        boolean textFound = solo.searchText("Validate your email from your inbox to finish registration.");
        assertTrue("Failed to send email", textFound);
    }
}
