package com.example.myfirstapp.Views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.myfirstapp.Models.HotelTable;
import com.example.myfirstapp.Models.SessionInformation;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SFDCConnector;
import com.example.myfirstapp.Services.ServiceController;
import com.example.myfirstapp.ViewIdGenerator;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWaiter extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "HomeWaiter";
    public static Map<String,String> tableData = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_waiter);
        JSONObject jsonObject = ServiceController.homeWaiterHandler();
        try {
            if(jsonObject.getBoolean("isSucessful")==true){
                List<HotelTable> lstHotelTable = (List<HotelTable>) jsonObject.get("lstHotelTable");
                TableLayout container = (TableLayout) findViewById(R.id.container);
                TableRow tr= new TableRow(this);

                for(int i=0;i<lstHotelTable.size();i++){
                    HotelTable hotelTable = lstHotelTable.get(0);
                    Button myButton = new Button(this);
                    if (hotelTable.getStatus().compareTo(getString(R.string.VACANT))==0) {
                        myButton.setBackgroundColor(Color.WHITE);
                    }else if(hotelTable.getStatus().compareTo(getString(R.string.OCCUPIED))==0){
                        myButton.setBackgroundColor(Color.GREEN);
                    }else if(hotelTable.getStatus().compareTo(getString(R.string.RESERVED))==0){
                        myButton.setBackgroundColor(Color.RED);
                    }

                    tableData.put(hotelTable.getTableName(),hotelTable.getId());
                    myButton.setText(hotelTable.getTableName());
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                    params.setMargins(30,0,0,20);
                    myButton.setLayoutParams(params);
                    myButton.setOnClickListener(this);
                    myButton.setId(ViewIdGenerator.generateViewId());
                    tr.addView(myButton);
                    if(i%3==2){
                        container.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        tr = new TableRow(this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        tr.setLayoutParams(lp);
                    }
                }
                container.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, OrderPlace.class);
        Button btn = (Button) findViewById(v.getId());
        SessionInformation.setHotelTableId(tableData.get(btn.getText()));
        startActivity(intent);
    }
}
