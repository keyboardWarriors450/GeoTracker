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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Alex on 5/13/2015.
 */
public class AddRegisterSheet extends AsyncTask<String, Void, String> {

    private Context context;

    public AddRegisterSheet(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        try {
//            String link = Uri.parse("http://450.atwebpages.com/agreement.php").buildUpon()
//                    .build().toString();
            URI link;
            URL url = new URL("http://450.atwebpages.com/agreement.php");
            link = url.toURI();

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(link);
            HttpResponse response = client.execute(request);

            String result = EntityUtils.toString(response.getEntity());

            return result;


        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }


    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject jsonResult = new JSONObject(result);

                String registration = jsonResult.getString("agreement");
                StringBuilder sb = new StringBuilder("");
                for(int i = 0; i < registration.length(); i++) {
                    char c = registration.charAt(i);

                    if(c == '<') {
                        i += 1;
                        char compared = registration.charAt(i);
                        while (compared != '>') {
                            i += 1;
                            compared = registration.charAt(i);
                            c = registration.charAt(i);
                        }
                        if (compared == '>') {
                            if ((i+1) != registration.length()) {
                                i+=1;
                                c = registration.charAt(i);
                            } else {
                                c = ' ';
                            }
                        }
                    }


                    sb.append(c);

                }

                String printedRegistration = sb.toString();

                final AlertDialog alert = new AlertDialog.Builder(AddRegisterSheet.this.context).create();
                alert.setMessage(printedRegistration);
                alert.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
//                        context.startActivity(new Intent(AddRegisterSheet.this.context, RegisterActivity.class));
                        alert.cancel();
                    }
                });
                alert.show();
            } catch (JSONException e) {

            }
        }
    }
}
