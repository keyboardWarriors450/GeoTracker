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
        solo.clickOnButton("OK");
        solo.enterText(0, "");
        solo.clickOnButton("Continue");
        boolean textFound = solo.searchText("Cannot leave it blank");
        assertTrue("Required field validation failed", textFound);
    }

    /*public void testOrientation() {
        solo.clickOnButton("OK");
        solo.enterText(0, "danielk6@uw.edu");
        solo.enterText(1, "password1");
        solo.enterText(2, "password1");
        solo.pressSpinnerItem(0, 0);
        solo.enterText(3, "leo");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Continue");

        solo.setActivityOrientation(Solo.LANDSCAPE);
        boolean textFound = solo.searchText("danielk6@uw.edu");
        assertTrue("Password change failed", textFound);

        solo.setActivityOrientation(Solo.PORTRAIT);
        textFound = solo.searchText("danielk6@uw.edu");
        assertTrue("Password change failed", textFound);
    }*/

    public void testSubmitButton() {
        String email = "bl665@uujhjjyy.com";
        String password = "password1";
        String password2 = "password1";
        String question = "What is your favorite pet's name?";
        String answer = "leo";

        solo.clickOnButton("OK");
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
