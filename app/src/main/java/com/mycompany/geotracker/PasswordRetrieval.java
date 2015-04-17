package com.mycompany.geotracker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Daniel on 4/13/2015.
 */
public class PasswordRetrieval extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_retrieval);

        final Button btn_cancel = (Button)findViewById(R.id.button2);

        btn_cancel.setOnClickListener(new Button.OnClickListener() {
            public void onClickView(View v) {
                finish();
            }
        });

        /*final Button btn_cancel = (Button)findViewById(R.id.reg_cancel);

        btn_cancel.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                finish();

        });*/
    }


}
