package com.asi.yalla_egy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class LastSixMonthActivity extends AppCompatActivity {

    private String currentdate;
    private String toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_six_month);
        // ============= get date ===========
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-yyyy");
        Calendar currentCall = Calendar.getInstance();
        currentdate = dateFormat.format(currentCall.getTime());
        Log.e("Current Date", currentdate);
        currentCall.add(Calendar.MONTH, -6);
        toDate = dateFormat.format(currentCall.getTime());
        Log.e("toDate", toDate);
        // ========  end ========


        DateFormat formater = new SimpleDateFormat("M-yyyy");

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(formater.parse(toDate));
            finishCalendar.setTime(formater.parse(currentdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<String> dates=new ArrayList<String>();
        while (beginCalendar.before(finishCalendar)) {
            // add one month to date per loop
            String date =     formater.format(beginCalendar.getTime()).toUpperCase();
            dates.add(date);
            beginCalendar.add(Calendar.MONTH, 1);
        }



        final TextView itemone= (TextView) findViewById(R.id.monOne);
        final TextView montwo= (TextView) findViewById(R.id.monTwo);
        final TextView monthree= (TextView) findViewById(R.id.monThree);
        final TextView monfour= (TextView) findViewById(R.id.monFour);
        final TextView monfive= (TextView) findViewById(R.id.monFive);
        final TextView monsix= (TextView) findViewById(R.id.monSix);
        final TextView curentmon= (TextView) findViewById(R.id.curentmon);


        itemone.setText(dates.get(0));
        montwo.setText(dates.get(1));
        monthree.setText(dates.get(2));
        monfour.setText(dates.get(3));
        monfive.setText(dates.get(4));
        monsix.setText(dates.get(5));
        curentmon.setText(currentdate);

        itemone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy");
                Date d = null;
                try {
                    d = sdf.parse((String) itemone.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String month = checkDigit(cal.get(Calendar.MONTH)+1);
                String year = checkDigit(cal.get(Calendar.YEAR));
                Intent intent =new Intent(LastSixMonthActivity.this,ProfitHistory.class);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });
        montwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy");
                Date d = null;
                try {
                    d = sdf.parse((String) montwo.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String month = checkDigit(cal.get(Calendar.MONTH)+1);
                String year = checkDigit(cal.get(Calendar.YEAR));
                Intent intent =new Intent(LastSixMonthActivity.this,ProfitHistory.class);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });
        monthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy");
                Date d = null;
                try {
                    d = sdf.parse((String) monthree.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String month = checkDigit(cal.get(Calendar.MONTH)+1);
                String year = checkDigit(cal.get(Calendar.YEAR));
                Intent intent =new Intent(LastSixMonthActivity.this,ProfitHistory.class);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });
        monfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy");
                Date d = null;
                try {
                    d = sdf.parse((String) monfour.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String month = checkDigit(cal.get(Calendar.MONTH)+1);
                String year = checkDigit(cal.get(Calendar.YEAR));
                Intent intent =new Intent(LastSixMonthActivity.this,ProfitHistory.class);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });
        monfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy");
                Date d = null;
                try {
                    d = sdf.parse((String) monfive.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String month = checkDigit(cal.get(Calendar.MONTH)+1);
                String year = checkDigit(cal.get(Calendar.YEAR));
                Intent intent =new Intent(LastSixMonthActivity.this,ProfitHistory.class);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });
        monsix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy");
                Date d = null;
                try {
                    d = sdf.parse((String) monsix.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String month = checkDigit(cal.get(Calendar.MONTH)+1);
                String year = checkDigit(cal.get(Calendar.YEAR));
                Intent intent =new Intent(LastSixMonthActivity.this,ProfitHistory.class);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });
        curentmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("M-yyyy");
                Date d = null;
                try {
                    d = sdf.parse((String) curentmon.getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                String month = checkDigit(cal.get(Calendar.MONTH)+1);
                String year = checkDigit(cal.get(Calendar.YEAR));
                Intent intent =new Intent(LastSixMonthActivity.this,ProfitHistory.class);
                intent.putExtra("month",month);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });
    }
    public String checkDigit (int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}
