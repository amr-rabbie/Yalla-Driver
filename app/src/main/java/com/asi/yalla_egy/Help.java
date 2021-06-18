package com.asi.yalla_egy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Help extends AppCompatActivity
{

    LinearLayout guidDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        guidDriver= (LinearLayout) findViewById(R.id.ll_guid_driver);
        guidDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Help.this,Guid_Driver_Activity.class));
            }
        });
    }
}
