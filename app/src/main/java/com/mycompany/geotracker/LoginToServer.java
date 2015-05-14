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
 * Created by David May 2015
 */
public class LoginToServer extends AsyncTask<String,Void,String> {
    private ProgressDialog pDialog;
    private Context context;
    private String uid, email, password;

    public LoginToServer(Context context) {
        this.context = context;
    }

    /**
     * Before starting background thread, show the progress dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loggin in..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Logs onto the server.
     * @param arg0 the arguments
     * @return the message that is displayed when the user tries to log onto the server
     */
    @Override
    protected String doInBackground(String... arg0) {
        try {
            email = arg0[0];
            password = arg0[1];

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
            return sb.toString();

        } catch(Exception e){
            return "Exception: " + e.getMessage();
        }
    }

    /**
     * After completing background task, dismiss the progress dialog
     */
    @Override
    protected void onPostExecute(String result) {
        // dismiss the dialog once done
        pDialog.dismiss();

        if (result != null) {
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getString("result").equals("success")) {
                    try {
                        uid = obj.getString("userid");
                        Log.i("USER", uid);
                        MyData myData = new MyData(LoginToServer.this.context);
                        myData.deleteAllUsers();
                        myData.insertUser(uid, email, password, "", "");
                        myData.close();
                    } catch (Exception e) {
                        Toast.makeText(LoginToServer.this.context, e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    context.startActivity(new Intent(LoginToServer.this.context,
                            MyAccountActivity.class));
                }
                else {
                    Toast.makeText(LoginToServer.this.context, "Incorrect email or password",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                System.out.println("JSON Exception");
            }
        }

    }

}
