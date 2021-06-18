package com.asi.yalla_egy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
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

import LoingSession.SQLiteHandler;
import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;

public class NotificationAct extends AppCompatActivity implements View.OnClickListener {
    TextView secText;
    FancyButton btn_accept, btn_decline;
    private ToneGenerator toneGen1;
    private CountDownTimer countDownTimer;
    TextView passName,PickUpPlace,DropPlace;
    String TripId; //TripId here is come from the checknewRequest.java on services directory
    String DriverId; // get driver id from sqlite data base\
    String passengerId;
    AlertDialog alertDialog1;
    CharSequence[] values = {String.valueOf(R.string.carissue),String.valueOf(R.string.Nondeductionsonthesite),String.valueOf(R.string.Emergencycauses)};
    String  PickupLongitude,PickupLatitude,DropLongitude,DropLatitude;
    RatingBar passangetRate;
    private Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);
        ini();
    }
    private void ini() {
        secText = (TextView) findViewById(R.id.secTxt);
        btn_accept= (FancyButton) findViewById(R.id.butt_accept);
        btn_accept.setOnClickListener(this);
        btn_decline= (FancyButton) findViewById(R.id.butt_decline);
        btn_decline.setOnClickListener(this);
        passName= (TextView) findViewById(R.id.pass_name);
        passName.setText(AppController.getPassengerName());
        PickUpPlace= (TextView) findViewById(R.id.tvpickupplace);
        PickUpPlace.setText(AppController.getPickUpPlace());
        DropPlace= (TextView) findViewById(R.id.tvdropplace);
        DropPlace.setText(AppController.getDropPlace());
        passangetRate= (RatingBar) findViewById(R.id.review_ratingBar);
        passangetRate.setRating(Float.parseFloat(AppController.getPassengerRate()));
        /**
         * to run count down timer
         * */
        final Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        countDownTimer=new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Toast.makeText(NotificationAct.this,"seconds remaining: " + millisUntilFinished / 1000,Toast.LENGTH_LONG).show();
                //here you can have your logic to set text to edittext
                r.play();
                secText.setText(String.valueOf(millisUntilFinished / 1000)+ "  sec");
//
//                try {
//                    if (toneGen1 == null) {
//
//                        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
//                        toneGen1.startTone(ToneGenerator.TONE_DTMF_5, 200);
//                    }
//                    toneGen1.startTone(ToneGenerator.TONE_DTMF_5, 200);
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (toneGen1 != null) {
//
//                                toneGen1.release();
//                                toneGen1 = null;
//                            }
//                        }
//
//                    }, 200);
//                } catch (Exception e) {
//
//                }
            }
            public void onFinish() {
                // Toast.makeText(NotificationAct.this, "Done", Toast.LENGTH_LONG).show();
                r.stop();
                cancelTrip("0","4");

            }
        }.start();
        DriverId=Constants.getUserId(NotificationAct.this);
        TripId=AppController.getTripId();
        passengerId=AppController.getPassengerId();
        PickupLatitude=AppController.getPickupLatitude();
        PickupLongitude=AppController.getPickupLongitude();
        DropLatitude=AppController.getDropLatitude();
        DropLongitude=AppController.getDropLatitude();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.butt_accept:

                /**
                 * stop the the timer then move ti
                 * */
//                toneGen1.stopTone();
                countDownTimer.cancel();
                r.stop();

                acceptTrip();
                break;
            case R.id.butt_decline:
//                toneGen1.stopTone();
                countDownTimer.cancel();
                // if user cancel the trip
                r.stop();
               CreateAlertDialogWithRadioButtonGroup();
                break;
            default:
                break;
        }
    }
    /**
     * if user click on cancel the trip this function will used @rejectType as params
     * @param rejectType,reason
     * */
    public void cancelTrip(String rejectType,String reason)
    {

        final String TAG="ASI";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("Driverid",DriverId);
        postParam.put("Tripid",TripId);
        postParam.put("Reason",reason);
        postParam.put("Companyid",new SQLiteHandler(NotificationAct.this).getUserDetails().get("company_id"));
        postParam.put("Rejecttype",rejectType);// 1=rejected by driver, 0=request timeout

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"RejectTrip", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG+"-->Reject", response.toString());
                        try {
                            String Flag=response.getString("Flag");
                            if (Flag.equals(Constants.REQUEST_REJECTED_BY_DRIVER))
                            {
                                //user reject the trip

                                /**
                                 *  when user cancel the trip  the value will change to be F--->Free so he can receive new trips
                                 *
                                 * */
                                Toasty.info(NotificationAct.this,getResources().getString(R.string.Youcancelthetrip),Toast.LENGTH_LONG,true).show();
                            }else if (Flag.equals(Constants.DRIVER_REPLY_TIMEOUT))
                            {
                                // time out
                                /**
                                 *  when time out the trip  the value will change to be F--->Free so he can receive new trips
                                 *
                                 * */
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE,NotificationAct.this);
                                Toasty.info(NotificationAct.this,getResources().getString(R.string.timeout),Toast.LENGTH_LONG,true).show();

                                finish();
                            }else if (Flag.equals(Constants.INVALID_TRIP))
                            {
                                // invalid trip
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE,NotificationAct.this);
                                Toasty.info(NotificationAct.this,getResources().getString(R.string.invalidtrip),Toast.LENGTH_LONG,true).show();

                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: from cancel" + error.getMessage());
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
    /**
     * if user click on the accept btn he will redirect to the map
     * */
    public void acceptTrip()
    {
        final String TAG="ASI";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("Driverid",DriverId);
        postParam.put("Tripid",TripId);
        postParam.put("Taxiid",new SQLiteHandler(NotificationAct.this).getUserDetails().get("taxi_no"));
        postParam.put("Companyid",new SQLiteHandler(NotificationAct.this).getUserDetails().get("company_id"));
        postParam.put("driverreply","A");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL+"AcceptTrip", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG+"-->Accept ", response.toString());
                        try {

                            String Flag=response.getString("Flag");
                            if (Flag.equals(Constants.DRIVER_ACCEPTED_THE_TRIP_REQUEST))
                            {
                                //user accept the trip  the trip
                                Intent intent=new Intent(NotificationAct.this,ShowRoute.class);
                                intent.putExtra("TripId",TripId);
                                intent.putExtra("status","0");
                                new AppController().setName("0");
                                startActivity(intent);
                                /**
                                 * Here when user accept the trip he will save the value as A ---> mean he in active trip now
                                 * */
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_GET_New_REQUEST,NotificationAct.this);
                                Toasty.success(NotificationAct.this,getResources().getString(R.string.tripAccepted), Toast.LENGTH_SHORT, true).show();

                                finish();
                            }else if (Flag.equals(Constants.TRIP_ALREADY_CONFIRMED))
                            {
                                // trip already confermid
                                Toasty.info(NotificationAct.this,getResources().getString(R.string.tripconfrimed),Toast.LENGTH_LONG,true).show();
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE,NotificationAct.this);
                            }else if(Flag.equals("1"))
                            {
                                Constants.SaveDriverStatus(Constants.DRIVER_STATUS_FREE,NotificationAct.this);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: from cancel" + error.getMessage());
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

    /**
     * if user click on cancel the trip this dialoge will used to ask him to choose one from three chooses
     * */
    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationAct.this);

        builder.setTitle(getResources().getString(R.string.SelectYourChoice));

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        //Toast.makeText(NotificationAct.this, "First Item Clicked", Toast.LENGTH_LONG).show();

                        cancelTrip("1","1");
                        Constants.SaveDriverStatus("F",NotificationAct.this);
                        finish();
                        break;
                    case 1:

                       // Toast.makeText(NotificationAct.this, "Second Item Clicked", Toast.LENGTH_LONG).show();

                        cancelTrip("1","2");
                        Constants.SaveDriverStatus("F",NotificationAct.this);
                        finish();
                        break;
                    case 2:


                        cancelTrip("1","3");
                        Constants.SaveDriverStatus("F",NotificationAct.this);
                        finish();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();

        alertDialog1.show();

    }
}


