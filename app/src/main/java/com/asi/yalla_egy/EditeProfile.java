package com.asi.yalla_egy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import LoingSession.SQLiteHandler;
import Models.responseModels.UserProfileDataResponse;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditeProfile extends AppCompatActivity  implements View.OnClickListener{

    ImageView ivEdite,ivProfileImage;
    private FrameLayout frameLayout;
    Toolbar toolbar;
    EditText fName,lName,userMail,mobile,addressone,etAccountType;
    NestedScrollView nestedParentLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_profile);
        ini();
       // setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        frameLayout.setFocusable(true);
    }

    public void ini()
    {
        //toolbar= (Toolbar) findViewById(R.id.toolbar);

       // ivEdite.setOnClickListener(this);
        frameLayout= (FrameLayout) findViewById(R.id.frame);
        fName=(EditText) findViewById(R.id.etFname);
        lName=(EditText) findViewById(R.id.edLname);
        userMail=(EditText) findViewById(R.id.etEmail);
        mobile=(EditText) findViewById(R.id.etmobile);
        addressone= (EditText) findViewById(R.id.edaddressone);
        etAccountType= (EditText) findViewById(R.id.etAccountType);
        nestedParentLayout= (NestedScrollView) findViewById(R.id.parentlayout);
        ivProfileImage= (ImageView) findViewById(R.id.profile_image);
        getDriverProfile();
    }

    @Override
    public void onClick(View view)
    {
//        switch (view.getId()) {
//            case R.id.ivedite:
//                nestedParentLayout.setBackgroundColor(Color.TRANSPARENT);
//                makeThingsActive(fName);
//                makeThingsActive(lName);
//                makeThingsActive(userMail);
//                makeThingsActive(mobile);
//                makeThingsActive(addresstwo);
//                makeThingsActive(addressone);
//                makeThingsActive(city);
//                makeThingsActive(state);
//                makeThingsActive(postalcode);
//                break;
//
//            default:
//                break;
//        }
    }


    /**
     * this method used to switch edit text from #setFocusable --> false to -->true
     * */
    public void makeThingsActive(EditText view)
    {
        view.setCursorVisible(true);
        view.setFocusableInTouchMode(false);
        view.setFocusable(false);
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
    }

    public  void getDriverProfile()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(EditeProfile.this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String TAG = "ASI";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId",Constants.getUserId(EditeProfile.this));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "GetdDriverProfile", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {



                        progressDialog.cancel();
                        Log.d(TAG, response.toString());
                        try
                        {
                            String Flag = response.getString("Flag");

                            if (Flag.equals("1"))
                            {

                                //invalid_request
                            }else if(Flag.equals("2"))
                            {

                                //invalid_user
                            }
                            else if(Flag.equals("7"))
                            {
                                //account_deactivte

                            }else if(Flag.equals("0"))
                            {

                                // parse response
                                UserProfileDataResponse object=new Gson().fromJson(response.toString(),UserProfileDataResponse.class);
                                //Success
                                fName.setText(object.getName());
                                lName.setText(object.getLastName());
                                userMail.setText(object.getEmail());
                                addressone.setText(object.getAddress());
                                etAccountType.setText(object.getAccountType());
                                mobile.setText(object.getPhone());
//                                Glide.with(EditeProfile.this).load(object.getProfilePicture())
//                                        .crossFade()
//                                        .placeholder(R.drawable.user)
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .into(ivProfileImage);
                                Picasso.with(EditeProfile.this).load(new SQLiteHandler(EditeProfile.this).getUserDetails().get("pro_pic"))
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.warning)
                                        .into(ivProfileImage);

//                                if (ivProfileImage==null)
//                                {
//                                    ivProfileImage.setImageResource(R.drawable.user);
//                                }
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
                Log.d(TAG, "Error: " + error.getMessage());

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
