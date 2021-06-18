package com.asi.yalla_egy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import LoingSession.SQLiteHandler;

public class Invites extends AppCompatActivity
{

    LinearLayout shareCode;
    TextView tvRefCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);
        ini();
    }

    private void ini() {

        tvRefCode= (TextView) findViewById(R.id.tvRecCode);
        tvRefCode.setText(new SQLiteHandler(getApplicationContext()).getUserDetails().get("ref_code"));
        shareCode= (LinearLayout) findViewById(R.id.llShareRefCode);
        shareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,new SQLiteHandler(getApplicationContext()).getUserDetails().get("ref_code"));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
}
