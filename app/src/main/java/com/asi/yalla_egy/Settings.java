package com.asi.yalla_egy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class Settings extends AppCompatActivity {

    LinearLayout changeLang,changePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ini();
    }


    public void ini()
    {
        changeLang= (LinearLayout) findViewById(R.id.changeLangLayout);
        changePass= (LinearLayout) findViewById(R.id.changePassLayout);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Settings.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.langmenu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                       if (item.getItemId()==R.id.one)
                       {
                           Constants.ChangeLang( "ar", Settings.this);
                           Intent intent = new Intent(getApplicationContext(), SplachScreen.class);
                           //In API level 11 or greater, use FLAG_ACTIVITY_CLEAR_TASK and FLAG_ACTIVITY_NEW_TASK flag on Intent to clear all the activity stack.
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                           finish();
                       }else if (item.getItemId()==R.id.two)
                        {
                            Constants.ChangeLang( "en", Settings.this);
                            Intent intent = new Intent(getApplicationContext(), SplachScreen.class);
                            //In API level 11 or greater, use FLAG_ACTIVITY_CLEAR_TASK and FLAG_ACTIVITY_NEW_TASK flag on Intent to clear all the activity stack.
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
        }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,ChangePassword.class));
            }
        });
    }
}
