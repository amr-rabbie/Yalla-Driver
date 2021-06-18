package com.asi.yalla_egy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import mehdi.sakout.fancybuttons.FancyButton;

public class NOInternetActivity extends AppCompatActivity
{

    FancyButton tryAginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nointernet);
        ActionBar myActionBar = getSupportActionBar();
        //For hiding android actionbar
        myActionBar.hide();
        tryAginBtn= (FancyButton) findViewById(R.id.btn_try_agin);
        tryAginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(NOInternetActivity.this,SplachScreen.class));
                finish();
            }
        });
    }
}
