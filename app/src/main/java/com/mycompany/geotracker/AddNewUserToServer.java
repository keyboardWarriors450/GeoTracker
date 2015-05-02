package com.mycompany.geotracker;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by David May 2015
 */
public class AddNewUserToServer extends AsyncTask<String,Void,String>{

    private ProgressDialog pDialog;
    private Context context;

    public AddNewUserToServer(Context context) {
        this.context = context;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Registering New User..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String email = arg0[0];
            String password = arg0[1];
            String question = arg0[2];
            String answer = arg0[3];

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
            return "Exception: " + e.getMessage();
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once done
        pDialog.dismiss();

        AlertDialog alertDialog = new AlertDialog.Builder(AddNewUserToServer.this.context).create();
        alertDialog.setTitle("Validate Email...");
        alertDialog.setMessage("Validate your email from your inbox to finish registration.");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(AddNewUserToServer.this.context, HomeScreen.class));
            }
        });
        alertDialog.show();
    }

}  