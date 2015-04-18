package com.mycompany.geotracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RegisterActivity extends ActionBarActivity {
    private String email, password, confirmed_password, question, answer;
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button btn_cancel = (Button)findViewById(R.id.reg_cancel);
        final Button btn_continue = (Button) findViewById(R.id.reg_continue);

        btn_cancel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btn_continue.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                preferenceSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preferenceEditor = preferenceSettings.edit();

                TextView reg_email = (TextView)findViewById(R.id.reg_email);
                email = reg_email.getText().toString();
                TextView reg_pass = (TextView)findViewById(R.id.reg_password);
                password = reg_pass.getText().toString();
                TextView conf_pass = (TextView)findViewById(R.id.conf_password);
                confirmed_password = conf_pass.getText().toString();
                TextView sec_question = (TextView)findViewById(R.id.sec_question);
                question = sec_question.getText().toString();
                TextView sec_answer = (TextView)findViewById(R.id.sec_answer);
                answer = sec_answer.getText().toString();

                /*
                Need this to test to see if the password is the same as the confirmed.
                Wasn't sure what you wanted to put here. For now I made it so that it just
                accepts it no matter what.
                 */
//                if(!password.equals(confirmed_password)) {
//                    //passwords do not match
//                } else {
//
//                }

                preferenceEditor.putString("email", email);
                preferenceEditor.putString("password", password);
                preferenceEditor.putString("question", question);
                preferenceEditor.putString("answer", answer);
                preferenceEditor.commit();

                toMyAccountActivity();
            }
        });
    }

    public void toMyAccountActivity() {
        startActivity(new Intent(this, MyAccountActivity.class));
    }
}
