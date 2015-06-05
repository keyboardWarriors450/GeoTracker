/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.controller;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

import com.mycompany.geotracker.R;
import com.robotium.solo.Solo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

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
        String email = "danielk6@uw.edu";
        String password = "password2";
        solo.unlockScreen();
        solo.enterText(0, email);
        solo.enterText(1, password);
        //solo.clickOnMenuItem("Login");
        //solo.clickOnButton("Login");
        solo.clickOnActionBarItem(R.id.action_login);

        try {


            String link = Uri.parse("http://450.atwebpages.com/login.php").buildUpon()
                    .appendQueryParameter("email", email)
                    .appendQueryParameter("password", password)
                    .build().toString();

            link = link.replaceFirst("%40", "@");

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));

            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));

            StringBuilder sb = new StringBuilder("");
            String line="";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            System.out.println(sb.toString());

        } catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
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
