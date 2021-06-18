package Fragmant;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.About;
import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;
import com.asi.yalla_egy.Documents;
import com.asi.yalla_egy.EditeProfile;
import com.asi.yalla_egy.Help;
import com.asi.yalla_egy.LoginSignup;
import com.asi.yalla_egy.R;
import com.asi.yalla_egy.Settings;
import com.asi.yalla_egy.TaxiProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import LoingSession.SQLiteHandler;
import LoingSession.SessionManager;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragmant extends Fragment
{
    private View view;
    TextView ViewProfile;
    LinearLayout payLayout, settingLayout, doclayout, aboutlayout, helplayout;
    ImageView profilePic, carPic;
    TextView name,viewTaxiprofile,carName;
    CardView logout;
    private SweetAlertDialog askUserForLogout;

    public ProfileFragmant()
    {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.fragment_profile_fragmant, container, false);
        ini(view);
       // GlobalBus.getBus().register(this);
        return view;
    }

    public void ini(View view)
    {
        ViewProfile = (TextView) view.findViewById(R.id.tvViewProfile);
        ViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), EditeProfile.class));
            }
        });

//        payLayout = (LinearLayout) view.findViewById(R.id.payLayout);
//        payLayout.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                startActivity(new Intent(getActivity(), PayStatement.class));
//            }
//        });

        settingLayout = (LinearLayout) view.findViewById(R.id.settingsLayout);
        settingLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), Settings.class));
            }
        });

        doclayout = (LinearLayout) view.findViewById(R.id.docLayout);
        doclayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Documents.class);
                startActivity(intent);
            }
        });

        aboutlayout = (LinearLayout) view.findViewById(R.id.aboutlayout);
        aboutlayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), About.class);
                startActivity(intent);
            }
        });
        helplayout = (LinearLayout) view.findViewById(R.id.helplayout);
        helplayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), Help.class);
                startActivity(intent);
            }
        });

        logout = (CardView) view.findViewById(R.id.cardLogout);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                askUserForLogout=new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getActivity().getResources().getString(R.string.areyousure))
                        .setContentText(getActivity().getResources().getString(R.string.needtologout))
                        .setConfirmText(getActivity().getResources().getString(R.string.yeslogmeout))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                    {
                        @Override
                        public void onClick(SweetAlertDialog sDialog)
                        {
                            logoutRequest();
                        }
                    });
                askUserForLogout.show();

            }
        });
        profilePic = (ImageView) view.findViewById(R.id.profile_image);
        //Log.e("DRIVER PIC --->",new SQLiteHandler(getActivity()).getUserDetails().get("pro_pic"));
//        Glide.with(getActivity()).load(new SQLiteHandler(getActivity()).getUserDetails().get("pro_pic"))
//                .crossFade()
//                .placeholder(R.drawable.user)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(profilePic);
        Picasso.with(getActivity()).load(new SQLiteHandler(getActivity()).getUserDetails().get("pro_pic"))
                .placeholder(R.drawable.user)
                .error(R.drawable.warning)
                .into(profilePic);

//        if (profilePic==null)
//        {
//            profilePic.setImageResource(R.drawable.user);
//        }
        carPic = (ImageView) view.findViewById(R.id.car_image);
        carName= (TextView) view.findViewById(R.id.tvCarName);
        carName.setText(new SQLiteHandler(getActivity()).getUserDetails().get("car_name"));
        name = (TextView) view.findViewById(R.id.tv_name);
        name.setText(new SQLiteHandler(getActivity()).getUserDetails().get("name"));

        viewTaxiprofile= (TextView) view.findViewById(R.id.tvChangeCar);
        viewTaxiprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TaxiProfile.class));
            }
        });






    }
//    @Subscribe
//    public void getMessage(Events.UpdateTextOfDistance updateTextOfDistance) {
//
//        Log.e("DIS FROM PROFILE --->",updateTextOfDistance.getDis());
//        name.setText(updateTextOfDistance.getDis());
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        // Unregister the registered event.
//        GlobalBus.getBus().unregister(this);
//    }

    private void logoutRequest()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getActivity().getResources().getString(R.string.logout));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String TAG = "ASI";

        Log.e("DriverId", Constants.getUserId(getActivity()));
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("DriverId",Constants.getUserId(getActivity()));
        postParam.put("ShiftId", new SQLiteHandler(getActivity()).getUserDetails().get("shift_id"));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverLogout", new JSONObject(postParam),
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
                            Log.e("FLAG-->", Flag);
                            if (Flag.equals("14"))
                            {
                                new SessionManager(getActivity()).setLogin(false);
                                new SQLiteHandler(getActivity()).deleteUsers();
                                getActivity().finish();
                                getActivity().startActivity(new Intent(getActivity(), LoginSignup.class).putExtra("Flag","2"));
                            }else if(Flag.equals("13"))
                            {
                                askUserForLogout.dismiss();
                                Toast.makeText(getActivity(),getResources().getString(R.string.youareintripnow),Toast.LENGTH_LONG).show();
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
