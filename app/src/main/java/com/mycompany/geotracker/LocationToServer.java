/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by David on 5/11/15.
 */
public class LocationToServer extends AsyncTask<String, Void, String> {

    private Context context;
    private String uid;
    private String lat;
    private String lon;
    private String speed;
    private String heading;
    private String timestamp;

    public LocationToServer(Context context) {
    }

    @Override
    protected void onPreExecute() {
    }

    /**
     * Sends locations to the server.
     * @param params the parameters of the locations
     * @return message that displays when the locations are being sent to the server
     */
    @Override
    protected String doInBackground(String ...params) {
        try {
            uid = params[0];
            lat = params[1];
            lon = params[2];
            speed = params[3];
            heading = params[4];
            timestamp = params[5];

            String link = Uri.parse("http://450.atwebpages.com/logAdd.php").buildUpon()
                    .appendQueryParameter("lat", lat)
                    .appendQueryParameter("lon", lon)
                    .appendQueryParameter("speed", speed)
                    .appendQueryParameter("heading", heading)
                    .appendQueryParameter("timestamp", timestamp)
                    .appendQueryParameter("source", uid)
                    .build().toString();

       //     link = link.replaceFirst("%40", "@");

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
            return sb.toString();

        } catch(Exception e){
            return "Exception: " + e.getMessage();
        }
    }

    /**
     * The result of whether or not the location was sent to the server.
     * @param result the result
     */
    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getString("result").equals("success")) {
                    Log.i("sending location", "success");
                    Log.i("sending location", timestamp);
                }
                else {
                    Log.i("sending location", "failed");
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception");
            }
        }
    }

}
