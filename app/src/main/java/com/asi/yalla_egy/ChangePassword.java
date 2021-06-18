package com.asi.yalla_egy;


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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mehdi.sakout.fancybuttons.FancyButton;
public class ChangePassword extends AppCompatActivity {


    EditText oldPass,newPass;
    FancyButton change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ini();
    }

    private void ini() {
        oldPass= (EditText) findViewById(R.id.et_old_password);
        newPass= (EditText) findViewById(R.id.et_password);
        change= (FancyButton) findViewById(R.id.btn_change_pass);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changePassRequest();
            }
        });
    }


    private void changePassRequest() {

        final String TAG="ASI";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("UserId",Constants.getUserId(ChangePassword.this));
        postParam.put("OldPassword",Constants.convertPassMd5(oldPass.getText().toString().trim()));
        postParam.put("NewPassword",Constants.convertPassMd5(newPass.getText().toString()));
        postParam.put("OrginalNewPassword",newPass.getText().toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"DriverChangePassword", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {

                            String Flag=response.getString("Flag");
                            if (Flag.equals(Constants.PASSWORD_CHANGED))
                            {
                                new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.Message))
                                        .setContentText(getResources().getString(R.string.passchange))
                                        .setConfirmText(getResources().getString(R.string.done))
                                        .show();
                            }else if (Flag.equals(Constants.OLD_PASS_INCORRECT))
                            {
                                new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(getResources().getString(R.string.Message))
                                        .setContentText(getResources().getString(R.string.oldpassnotcorrect))
                                        .setConfirmText(getResources().getString(R.string.done))
                                        .show();
                            }else if(Flag.equals(Constants.INVALID_REQUEST))
                            {
                                Toast.makeText(ChangePassword.this,getResources().getString(R.string.thereisanerror),Toast.LENGTH_LONG).show();
                            }else if(Flag.equals(Constants.INVALID_USER))
                            {
                                Toast.makeText(ChangePassword.this,getResources().getString(R.string.thereisanerror),Toast.LENGTH_LONG).show();
                            }
                            Log.e("FLAG-->",Flag);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
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
