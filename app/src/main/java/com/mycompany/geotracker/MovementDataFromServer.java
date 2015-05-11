/*
 * Copyright (c) 2015. Keyboard Warriors (Alex Hong, Daniel Khieuson, David Kim, Viet Nguyen).
 * This file is part of GeoTracker.
 * GeoTracker cannot be copied and/or distributed without the express permission
 * of Keyboard Warriors.
 */

package com.mycompany.geotracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mycompany.geotracker.data.MyData;

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
 * Created by David on 5/11/2015.
 */
public class MovementDataFromServer extends AsyncTask<String, Void, String> {

    private ProgressDialog pDialog;
    private Context context;
    private String uid;
    private String start;
    private String end;

    public MovementDataFromServer(Context context) {
        this.context = context;
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        pDialog = new ProgressDialog(context);
//        pDialog.setMessage("Downloading..");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(true);
//        pDialog.show();
//    }

    @Override
    protected String doInBackground(String... params) {
        try {
            uid = params[0];
            start = params[1];
            end = params[2];

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

    @Override
    protected void onPostExecute(String result) {
        // dismiss the dialog once done
//        pDialog.dismiss();

        if (result != null) {
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getString("result").equals("success")) {
                    try {
                        Log.i("Movement", "success");
//                        uid = obj.getString("userid");
//                        MyData myData = new MyData(MovementDataFromServer.this.context);
//                        myData.deleteAll();
//                        myData.insertUser(uid, email, password, "", "");
//                        myData.close();
                    } catch (Exception e) {
                        Toast.makeText(MovementDataFromServer.this.context, e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
         //           context.startActivity(new Intent(LoginToServer.this.context,
           //                 MyAccountActivity.class));
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
