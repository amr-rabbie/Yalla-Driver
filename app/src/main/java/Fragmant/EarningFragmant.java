package Fragmant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asi.yalla_egy.AppController;
import com.asi.yalla_egy.Constants;
import com.asi.yalla_egy.DriverPromoCodeHistory;
import com.asi.yalla_egy.Invites;
import com.asi.yalla_egy.LastSixMonthActivity;
import com.asi.yalla_egy.R;
import com.asi.yalla_egy.TripsHistory;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Models.moneyModel;
import Models.responseModels.DriverWeeklyEarning;
import chartview.Bar;
import chartview.BarGraph;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;


//public class EarningFragmant extends Fragment implements ViewPager.OnPageChangeListener {
public class EarningFragmant extends Fragment
{

    ArrayList<moneyModel> moneyOfDay;
    ArrayList<Bar> points;
    private View view;
    FancyButton invite;
    TextView tvTotalEarnning, tvAccptedTrips, tvTotalMoney, tvcurentdatetodate;
    private String currentdate;
    private String toDate;
    LinearLayout linear_Promotions,profitHisLayout;

    public EarningFragmant()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_earning_fragmant, container, false);
        ini(view);
        return view;
    }

    public void ini(View view)
    {
        getDriverWeeklyEarning();
        points = new ArrayList<>();
        moneyOfDay = new ArrayList<>();
        tvTotalMoney = (TextView) view.findViewById(R.id.tvTotalMoney);
        tvcurentdatetodate = (TextView) view.findViewById(R.id.tvcurentdatetodate);
        linear_Promotions = (LinearLayout) view.findViewById(R.id.linear_Promotions);
        linear_Promotions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), DriverPromoCodeHistory.class));
            }
        });


        profitHisLayout= (LinearLayout) view.findViewById(R.id.profitHisLayout);
        profitHisLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), LastSixMonthActivity.class));
            }
        });

        invite = (FancyButton) view.findViewById(R.id.btn_invite);
        invite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), Invites.class));
            }
        });

        LinearLayout tripHis = (LinearLayout) view.findViewById(R.id.tripHisLayout);
        tripHis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), TripsHistory.class));
            }
        });

        tvTotalEarnning = (TextView) view.findViewById(R.id.tvTotalEarning);
    }


    private void getDriverWeeklyEarning()
    {
        final SweetAlertDialog progressDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getActivity().getResources().getString(R.string.LOADING));
        progressDialog.setCancelable(false);
        progressDialog.show();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy");
        Calendar currentCal = Calendar.getInstance();
        currentdate = dateFormat.format(currentCal.getTime());
        Log.e("Current Date", currentdate);
        currentCal.add(Calendar.DATE, -7);
        toDate = dateFormat.format(currentCal.getTime());
        Log.e("Current Date", toDate);
        final String TAG = "ASI";
        Log.e("DATE=>", currentdate + "--->" + toDate);
        //tvcurentdatetodate.setText(currentdate+" "+toDate);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("startdate", toDate);
        postParam.put("enddate", currentdate);
        postParam.put("DriverId", Constants.getUserId(getActivity()));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.BASE_URL + "DriverWeeklyEarning", new JSONObject(postParam),
                new Response.Listener<JSONObject>()
                {


                    @Override
                    public void onResponse(JSONObject response)
                    {
                        progressDialog.dismiss();
                        DriverWeeklyEarning WeekData = new Gson().fromJson(response.toString(), DriverWeeklyEarning.class);
                        Log.d(TAG + "DriverWeeklyEarning", response.toString());
                        moneyOfDay.add(new moneyModel(WeekData.getMon(), WeekData.getWen(), WeekData.getTus(), WeekData.getThr(), WeekData.getFri(), WeekData.getSat(), WeekData.getSun()));
                        tvcurentdatetodate.setText(getResources().getString(R.string.weekprofit)+" "+getResources().getString(R.string.from)+" "+WeekData.getStartDt() + "- \n" +getResources().getString(R.string.to)+" "+WeekData.getEndDt());
                        tvTotalMoney.setText(WeekData.getTotal() + " " + "SAR");
                        if (    WeekData.getFri().equals("0") &&
                                WeekData.getSat().equals("0") &&
                                WeekData.getSun().equals("0") &&
                                WeekData.getMon().equals("0") &&
                                WeekData.getWen().equals("0") &&
                                WeekData.getTus().equals("0") &&
                                WeekData.getTus().equals("0")
                                )
                        {
                            Toasty.info(getActivity(),getResources().getString(R.string.noearningyet),Toast.LENGTH_LONG,true).show();
                        }else
                        {


                            Bar d7 = new Bar();
                            d7.setColor(Color.parseColor("#FFBB33"));
                            d7.setName("SU");
                            d7.setValue(Float.parseFloat(moneyOfDay.get(0).getSu()));
                            points.add(d7);
                            Bar mon = new Bar();
                            mon.setColor(Color.parseColor("#99CC00"));
                            mon.setName("M");
                            mon.setValue(Float.parseFloat(moneyOfDay.get(0).getM()));
                            points.add(mon);
                            Bar tu = new Bar();
                            tu.setColor(Color.parseColor("#FFaB33"));
                            tu.setName("TU");
                            tu.setValue(Float.parseFloat(moneyOfDay.get(0).getTu()));
                            points.add(tu);

                            Bar d3 = new Bar();
                            d3.setColor(Color.parseColor("#FF5B33"));
                            d3.setName("W");
                            d3.setValue(Float.parseFloat(moneyOfDay.get(0).getW()));

                            points.add(d3);

                            Bar d4 = new Bar();
                            d4.setColor(Color.parseColor("#67BB33"));
                            d4.setName("TH");
                            d4.setValue(Float.parseFloat(moneyOfDay.get(0).getTh()));
                            points.add(d4);

                            Bar d5 = new Bar();
                            d5.setColor(Color.parseColor("#Fae933"));
                            d5.setName("F");
                            d5.setValue(Float.parseFloat(moneyOfDay.get(0).getF()));
                            points.add(d5);

                            Bar d6 = new Bar();
                            d6.setColor(Color.parseColor("#FadB33"));
                            d6.setName("SA");
                            d6.setValue(Float.parseFloat(moneyOfDay.get(0).getSa()));
                            points.add(d6);
                        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
                        pager.setAdapter(new PagerAdapter()
                        {
                            @Override
                            public int getCount()
                            {
                                // to return only one week data return-> #1 not ->points.size()
                                return 1;
                            }

                            @Override
                            public Object instantiateItem(ViewGroup container, int position)
                            {
                                View view = LayoutInflater.from(getActivity()).inflate(R.layout.pagerview, null, false);
                                BarGraph g = (BarGraph) view.findViewById(R.id.graph);
                                g.setBars(points);
                                g.setUnit("SAR ");
                                container.addView(view);
                                return view;
                            }

                            @Override
                            public boolean isViewFromObject(View view, Object o)
                            {
                                return view == o;
                            }

                            @Override
                            public void destroyItem(ViewGroup container, int position, Object object)
                            {
                                container.removeView((LinearLayout) object);
                            }

                        });

                        }
//                        //setting PageChangeListener
//                        pager.addOnPageChangeListener(this /* OnPageChangeListener */);
//
//                       //// setting Flare
//                        indicator.setUpWithViewPager(pager);
//                        indicator.addOnPageChangeListener(this /* OnPageChangeListener */);
//
//                        //Customize Flare
//                        indicator.setIndicatorColor(Color.BLUE);
//                        indicator.setIndicatorGap(30);

                    }
                }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Error: " + error.getMessage());
                // hideProgressDialog();
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
