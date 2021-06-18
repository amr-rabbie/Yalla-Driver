package com.asi.yalla_egy;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import LoingSession.SessionManager;
import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;

public class LoginSignup extends AppCompatActivity {
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        if (getIntent().getStringExtra("Flag").equals("0")) {
            permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
            if (new SessionManager(LoginSignup.this).isLoggedIn()) {
                Intent intent = new Intent(LoginSignup.this, MainActivity.class);
                startActivity(intent);
            } else {
                ini();
            }
            runpermission();
        } else if (getIntent().getStringExtra("Flag").equals("1")) {
            final SweetAlertDialog askUserForLogout = new SweetAlertDialog(LoginSignup.this, SweetAlertDialog.WARNING_TYPE);

            askUserForLogout.setTitleText(getResources().getString(R.string.message));
            askUserForLogout.setContentText(getResources().getString(R.string.youareloggedout));
            askUserForLogout.setConfirmText(getResources().getString(R.string.done));
            askUserForLogout.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    askUserForLogout.cancel();
                }
            });
            askUserForLogout.show();
            if (new SessionManager(LoginSignup.this).isLoggedIn()) {
                Intent intent = new Intent(LoginSignup.this, MainActivity.class);
                startActivity(intent);
            } else {
                ini();
            }
        } else {
            permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
            if (new SessionManager(LoginSignup.this).isLoggedIn()) {
                Intent intent = new Intent(LoginSignup.this, MainActivity.class);
                startActivity(intent);
            } else {
                ini();
            }
            runpermission();
        }


    }

    private void ini() {
        final FancyButton login = (FancyButton) findViewById(R.id.loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSignup.this, Login.class));
            }
        });

    }


    public void runpermission() {
        if (ActivityCompat.checkSelfPermission(LoginSignup.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginSignup.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginSignup.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(LoginSignup.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[3])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginSignup.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginSignup.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginSignup.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs  Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                       // Toast.makeText(getBaseContext(), "Go to Permissions to Grant   Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(LoginSignup.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginSignup.this, permissionsRequired[4])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginSignup.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Calls and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginSignup.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
               // Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(LoginSignup.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        // txtPermissions.setText("We've got all permissions");
      //  Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(LoginSignup.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}
