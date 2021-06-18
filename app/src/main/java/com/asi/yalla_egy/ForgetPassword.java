package com.asi.yalla_egy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener
{



    FancyButton done;
    EditText etMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ini();
    }

    private void ini()
    {

        done = (FancyButton) findViewById(R.id.btn_done);
        done.setOnClickListener(this);
        etMobile= (EditText) findViewById(R.id.input_mobile);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btn_done:
                changePass(etMobile.getText().toString().trim());
                break;

            default:
                break;
        }
    }


    public void changePass(String mobileNum)
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(ForgetPassword.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getApplicationContext().getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("AreaCode","966");
        postParam.put("MobileNumber",mobileNum);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverForgetPassword", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {


                    @Override
                    public void onResponse(JSONObject response)
                    {

                        progressDialog.dismiss();
                        try
                        {

                            String Flag = response.getString("Flag");
                            if (Flag.equals(Constants.MESSAGE_SEND_WITH_New_PASS))
                            {
                                Toasty.success(ForgetPassword.this,getResources().getString(R.string.PasswordsendbySMS), Toast.LENGTH_LONG,true).show();
                                startActivity(new Intent(ForgetPassword.this,Login.class));
                                finish();
                            }
                            else if (Flag.equals(Constants.INVALID_REQUEST))
                            {
                                // invalid request
                                Toasty.error(ForgetPassword.this,getResources().getString(R.string.thereisanerror), Toast.LENGTH_LONG,true).show();

                            }
                            else if (Flag.equals(Constants.INVALID_USER))
                            {
                                // invalid user
                                Toasty.error(ForgetPassword.this,getResources().getString(R.string.thereisanerror), Toast.LENGTH_LONG,true).show();

                            }else if (Flag.equals(Constants.ACCOUNT_DEACTIVTE))
                            {
                                // account_deactivte
                                Toasty.error(ForgetPassword.this,getResources().getString(R.string.thereisanerror), Toast.LENGTH_LONG,true).show();

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d(TAG, "Error" + error.getMessage());
                progressDialog.dismiss();
            }
        })
        {


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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "TAG");
    }
}
