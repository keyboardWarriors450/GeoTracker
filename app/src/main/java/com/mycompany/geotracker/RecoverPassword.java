/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Alex on 5/8/2015.
 */
public class RecoverPassword extends AsyncTask<String, Void, String> {

    private String email;
    private Context context;

    public RecoverPassword(Context context) {
        this.context = context;
    }

    /**
     * Connects to the web service to send the user an email to reset their password.
     * @param args the arguments
     * @return the message that shows if the email is sent or not
     */
    @Override
    protected String doInBackground(String... args) {
        try {
            email = args[0];

            String link = Uri.parse("http://450.atwebpages.com/reset.php").buildUpon()
                    .appendQueryParameter("email", email)
                    .build().toString();

            link = link.replaceFirst("%40", "@");

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));

            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder sb = new StringBuilder("");
            String line = "";

            while((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();

        } catch(Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    /**
     * Pop up that shows when the user enters in an email address.
     * @param result the string that is displayed in the pop up
     */
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);
                if (jsonResult.getString("result").equals("success")) {
                    AlertDialog alert = new AlertDialog.Builder(RecoverPassword.this.context).create();
                    alert.setMessage("Please check your email for further instructions");
                    alert.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            context.startActivity(new Intent(RecoverPassword.this.context, HomeScreen.class));
                        }
                    });
                    alert.show();
                } else {
                    Toast.makeText(RecoverPassword.this.context, "Email does not exist",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception thrown.");
            }
        }
    }
}
