package Fragmant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;
import com.asi.yalla_egy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.notificationAdapter;
import LoingSession.SQLiteHandler;
import Models.notificationModel;

public class Notifications extends Fragment {

    RecyclerView rvNotification;
    notificationAdapter notificationAdapter;
    ArrayList<notificationModel> notificationModels = new ArrayList<>();
    private View view;
    LinearLayout linearLoading,linear_no_new_notifications;
    public Notifications() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_notifications, container, false);
        ini(view);
        return view;
    }


    public void ini(View view)
    {
        rvNotification = (RecyclerView) view.findViewById(R.id.RvNotification);
        linearLoading= (LinearLayout) view.findViewById(R.id.loadingLayout);
        linear_no_new_notifications= (LinearLayout) view.findViewById(R.id.layout_no);
        getNotifications();


    }



    /**
     * get number of the notification in order to show them if there are any new notifications
     * */
    public  void getNotifications()
    {
        linearLoading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.BASE_URL+"GetDriverNotification", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //here is the response of server
                linearLoading.setVisibility(View.GONE);

                Log.e("RES-------->",response);

                JSONArray array=null;
                try {
                    JSONObject object=new JSONObject(response);
                     array= object.getJSONArray("driver_notifications_msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(array==null)
                {
                   linear_no_new_notifications.setVisibility(View.VISIBLE);
                }else
                {
                    for (int n = 0; n < array.length(); n++)
                    {
                        try
                        {
                            JSONObject object = array.getJSONObject(n);
                            String msgId = object.getString("msgId");
                            String message_title = object.getString("message_title");
                            String message_details = object.getString("message_details");
                            String sent_date=object.getString("sent_date");
                            notificationModels.add(new notificationModel(msgId, message_title, message_details,sent_date));
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        // do some stuff....
                    }
                    notificationAdapter = new notificationAdapter(getActivity(), notificationModels);
                    rvNotification.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    rvNotification.setAdapter(notificationAdapter);
                    linearLoading = (LinearLayout) view.findViewById(R.id.loadingLayout);
                }
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                linearLoading.setVisibility(View.GONE);
                Log.d("ErroeVolley",error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("DriverId",new SQLiteHandler(getActivity()).getUserDetails().get("uid"));
                return params;
            }

        };
        // Adding request to request queue
        int socketTimeout = 50000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "tag");
    }
}
