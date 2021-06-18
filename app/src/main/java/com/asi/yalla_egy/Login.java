package com.asi.yalla_egy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import LoingSession.SQLiteHandler;
import LoingSession.SessionManager;
import Models.responseModels.LoginResponse;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;

public class Login extends AppCompatActivity implements View.OnClickListener {

    FancyButton login;


    EditText mobile, pass;
    TextView forgetPass;

    ImageView iv_infoApp;

    private SQLiteHandler db;
    private SessionManager session;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ini();
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        deviceId = telephonyManager.getDeviceId();
            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());
            // Session manager
            session = new SessionManager(getApplicationContext());
        iv_infoApp= (ImageView) findViewById(R.id.iv_infoApp);
        iv_infoApp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Login.this,About.class));
            }
        });



    }
    private void ini()
    {
        login= findViewById(R.id.btn_login);
        mobile= (EditText) findViewById(R.id.input_email);
        pass= (EditText) findViewById(R.id.et_password);
        forgetPass= (TextView) findViewById(R.id.tv_forget_pass);
        login.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
    }



    public void test(View view) {
        startActivity(new Intent(Login.this,NotificationAct.class));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btn_login:
                if(mobile.getText().toString().isEmpty()&&pass.getText().toString().isEmpty())
                {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getResources().getString(R.string.Message))
                            .setContentText(getResources().getString(R.string.EnterYourPassAndNum))
                            .setConfirmText(getResources().getString(R.string.done))
                            .show();
                }else {
                    loginRequest();
                }
                break;
            case R.id.tv_forget_pass:
                startActivity(new Intent(Login.this,ForgetPassword.class));
                break;
            default:
                break;
        }
    }





    private void loginRequest() {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getApplicationContext().getResources().getString(R.string.SiningIN));
        progressDialog.setCancelable(false);
        progressDialog.show();

         final String TAG="ASI";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("Phone",mobile.getText().toString().trim());
        postParam.put("Password",pass.getText().toString().trim());
        postParam.put("DeviceId",deviceId);
        postParam.put("DeviceType","1"); // 1- Mean android device
        postParam.put("DeviceToken","0");
        postParam.put("BuildVersion","5");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"DriverLogin", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d(TAG+"USER DATA", response.toString());

                        try {
                            LoginResponse userData = new Gson().fromJson(response.toString(), LoginResponse.class);
                            String Flag=response.getString("Flag");
                            Log.e("FLAG-->",Flag);
                            if(Flag.equals(Constants.SUCCSESS))
                            {
                                String UserId= userData.getUserId();
                                String Name=userData.getName();
                                String Email=  userData.getEmail();
                                String Phone=  userData.getPhone();
                                String Address = userData.getAddress();
                                String ProfilePicture=  userData.getProfilePicture();
                                String DriverLicenceId=  userData.getDriverLicenceId();
                                String LastName=  userData.getLastName();
                                String ModelName=  userData.getModelName();
                                String TaxiNo=  userData.getTaxiNo();
                                String RefCode=  userData.getRefCode();
                                String ShiftId=userData.getShiftId();
                                String CompanyId=userData.getCompanyId();
                                // save user data into database
                                db.addUser(Name,Email,UserId,ShiftId,CompanyId,TaxiNo,RefCode,ModelName,ProfilePicture);
                                session.setLogin(true);
                                Log.e("USER DATA OBJECT----==-",userData.toString());
                                Toasty.success(Login.this,getResources().getString(R.string.loginsucceeded),Toast.LENGTH_LONG,true).show();
                                finish();
                                startActivity(new Intent(Login.this,MainActivity.class));
                            }else if(Flag.equals(Constants.PHONE_NOT_EXISTS))
                            {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.Message))
                                        .setContentText(getResources().getString(R.string.phonenotexist))
                                        .setConfirmText(getResources().getString(R.string.done))
                                        .show();
                            }else if(Flag.equals(Constants.PASSWORD_FAILED))
                            {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.Message))
                                        .setContentText(getResources().getString(R.string.passwordwrong))
                                        .setConfirmText(getResources().getString(R.string.done))
                                        .show();
                            }else if(Flag.equals(Constants.ACCOUNT_DEACTIVTE))
                            {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.Message))
                                        .setContentText(getResources().getString(R.string.accountdeactivated))
                                        .setConfirmText(getResources().getString(R.string.done))
                                        .show();
                            }
                            else if(Flag.equals(Constants.ALREADY_LOGIN))
                            {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.Message))
                                        .setContentText(getResources().getString(R.string.userLoginal))
                                        .setConfirmText(getResources().getString(R.string.done))
                                        .show();
                            }else if (Flag.equals(Constants.TAXI_NOT_ASSIGNED))
                            {
                                Toast.makeText(Login.this,getResources().getString(R.string.driverhasnotaxi),Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
               // hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //Toasty.error(Login.this,"TimeoutError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof AuthFailureError) {
                    //Toasty.error(Login.this,"AuthFailureError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof ServerError) {
                    //Toasty.error(Login.this,"ServerError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof NetworkError) {
                    //Toasty.error(Login.this,"NetworkError",Toast.LENGTH_LONG,true).show();
                } else if (error instanceof ParseError) {
                    //Toasty.error(Login.this,"ParseError",Toast.LENGTH_LONG,true).show();
                }
            }
        }) {



            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }



        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"TAG");
    }

}
