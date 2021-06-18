package com.asi.yalla_egy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import Adapters.payStatementAdpter;
import Models.payModel;

public class PayStatement extends AppCompatActivity {

    RecyclerView rvPay;
    payStatementAdpter payStatementAdpter;
    ArrayList<payModel> payModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_statement);
        ini();
    }


    public void ini()
    {
        rvPay= (RecyclerView) findViewById(R.id.rvPayStam);
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payModels.add(new payModel("12/12-19/12","129.4SAR"));
        payStatementAdpter=new payStatementAdpter(PayStatement.this,payModels);
        rvPay.setLayoutManager(new LinearLayoutManager(PayStatement.this,LinearLayoutManager.VERTICAL,false));
        rvPay.setAdapter(payStatementAdpter);


    }
}
