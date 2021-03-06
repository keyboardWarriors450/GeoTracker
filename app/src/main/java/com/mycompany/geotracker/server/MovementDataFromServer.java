/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.geotracker.controller.ShowMovementDataActivity;
import com.mycompany.geotracker.controller.ViewMapActivity;
import com.mycompany.geotracker.data.MyData;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by David on 5/11/2015.
 */
public class MovementDataFromServer extends AsyncTask<String, Void, String> {

    private ProgressDialog pDialog;
    private Context context;
    private String uid;
    private String lat;
    private String lon;
    private String speed;
    private String heading;
    private long timestamp;
    private String start;
    private String end;
    private String name;

    public MovementDataFromServer(Context context) {
        this.context = context;
    }

    /**
     * Pop up that shows when the movement data is being retrieved from the server.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Retrieves the movement data from the server.
     * @param params the parameters
     * @return the message that is displayed when the user tries to get the movement data
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            uid = params[0];
            start = params[1];
            end = params[2];
            name = params[3];

            String link = Uri.parse("http://450.atwebpages.com/view.php").buildUpon()
                    .appendQueryParameter("uid", uid)
                    .appendQueryParameter("start", start)
                    .appendQueryParameter("end", end)
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
     * After completing background task, dismiss the progress dialog
     * @param result the results of the retrieval
     */
    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        if (result != null) {
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getString("result").equals("success")) {
                    try {
                        MyData myData = new MyData(MovementDataFromServer.this.context);
                        myData.deleteAllLocations();

                        JSONArray jArray = obj.getJSONArray("points");

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(i);
                            lat = jObject.getString("lat");
                            lon = jObject.getString("lon");
                            speed = jObject.getString("speed");
                            heading = jObject.getString("heading");
                            timestamp = jObject.getLong("time");

                            myData.insertLocation(uid, lat, lon, speed, heading, timestamp);
                        }

                        Log.i("Movement", "success");
                        myData.close();

                    } catch (Exception e) {
                        Toast.makeText(MovementDataFromServer.this.context, e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!name.equals("map")) {
                        context.startActivity(new Intent(MovementDataFromServer.this.context,
                                ShowMovementDataActivity.class));
                    } else {
                        context.startActivity(new Intent(MovementDataFromServer.this.context,
                                ViewMapActivity.class));
                    }
                }
                else {
                    Toast.makeText(MovementDataFromServer.this.context, "Download failed",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception");
            }
        }

    }

}
