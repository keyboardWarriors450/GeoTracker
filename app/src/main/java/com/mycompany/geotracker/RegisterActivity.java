package com.mycompany.geotracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RegisterActivity extends ActionBarActivity {

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
                toMyAccountActivity();
            }
        });
    }

    public void toMyAccountActivity() {
        startActivity(new Intent(this, MyAccountActivity.class));
    }
}
