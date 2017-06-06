package com.example.myfirstapp.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myfirstapp.Models.SessionInformation;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SFDCConnector;
import com.example.myfirstapp.Services.ServiceController;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPlace extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "OrderPlace";
    public static final String TABLE_ID = "TABLE_ID";
    public static final String HOTEL_ID = "HOTEL_ID";
    public static final String ORDER_ID = "ORDER_ID";

    public static String tableId;
    public static String hotelId;
    public static String orderId;
    public static OrderDetails orderDetails = new OrderDetails();
    public static ArrayList<OrderLineDetails> orderLineDetails = new ArrayList<OrderLineDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);
        Intent intent = getIntent();

        tableId = SessionInformation.getHotelTableId();
        hotelId = SessionInformation.getHotelId();
        Log.d(TAG,"tableId="+tableId);
        Button showMenuBtn = (Button) findViewById(R.id.showMenuBtn);
        showMenuBtn.setOnClickListener(this);
        //getOrderDetails(null);

        Button createOrderButton = (Button) findViewById(R.id.createOrderButton);
        createOrderButton.setOnClickListener(this);
        Button completeOrderBtn = (Button) findViewById(R.id.completeOrderbtn);
        completeOrderBtn.setOnClickListener(this);

        JSONObject jsonObject = ServiceController.orderPlaceHandler(getString(R.string.READ));
        getOrderDetails(jsonObject);
        Log.d(TAG,jsonObject.toString());
    }

    public void addOrderLineFromMenuPage(String menuItemId,String quantity,String menuItemName){
        orderLineDetails.add(new OrderLineDetails(orderId,menuItemId,quantity));
        TableLayout container = (TableLayout) findViewById(R.id.container);

        TableRow tr= new TableRow(this);
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(0,30,0,0);
        tr.setLayoutParams(trParams);

        TextView itemName = new TextView(this);
        itemName.setText(menuItemName);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0,0,20,20);
        itemName.setLayoutParams(params);
        tr.addView(itemName);

        TextView itemQuantity = new TextView(this);
        itemQuantity.setText(quantity);
        params.setMargins(0,0,20,20);
        itemQuantity.setLayoutParams(params);
        tr.addView(itemQuantity);

        container.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));




    }

    public void getOrderDetails(JSONObject jsonObject){
        try {
            if(jsonObject.getBoolean("isSuccessful")){

                TableRow orderStatusRow = (TableRow) findViewById(R.id.orderStatusRow);
                orderStatusRow.setVisibility(View.VISIBLE);
                TableRow orderNumberRow = (TableRow) findViewById(R.id.orderNumberRow);
                orderNumberRow.setVisibility(View.VISIBLE);
                TableRow orderTimeRow = (TableRow) findViewById(R.id.orderTimeRow);
                orderTimeRow.setVisibility(View.VISIBLE);
                Button completeOrderBtn = (Button) findViewById(R.id.completeOrderbtn);
                completeOrderBtn.setVisibility(View.VISIBLE);

                List<com.example.myfirstapp.Models.OrderDetails> lstOrderDetails = (List<com.example.myfirstapp.Models.OrderDetails>) jsonObject.get("lstOrderDetails");
                List<com.example.myfirstapp.Models.OrderLineDetails> lstOrderLineDetails = (List<com.example.myfirstapp.Models.OrderLineDetails>) jsonObject.get("lstOrderLineDetails");

                com.example.myfirstapp.Models.OrderDetails order = lstOrderDetails.get(0);

                TextView orderNumber = (TextView) findViewById(R.id.orderNumber);
                orderNumber.setText(order.getOrderName());

                TextView orderStatus = (TextView) findViewById(R.id.orderStatus);
                orderStatus.setText(order.getOrderStatus());

                TextView orderTime = (TextView) findViewById(R.id.orderTime);
                orderTime.setText(order.getOrderTime());

                orderDetails.setId(order.getId());

                if(lstOrderLineDetails!=null && lstOrderLineDetails.size()>0){
                    TableLayout container = (TableLayout) findViewById(R.id.container);
                    int i;
                    for(i=0;i<lstOrderLineDetails.size();i++) {


                        TableRow tr= new TableRow(this);
                        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                        trParams.setMargins(0,30,0,0);
                        tr.setLayoutParams(trParams);

                        com.example.myfirstapp.Models.OrderLineDetails orderLine = (com.example.myfirstapp.Models.OrderLineDetails) lstOrderLineDetails.get(i);
                        //orderLineDetails.add(orderLine);

                        Log.d(TAG,orderLine.toString());

                        TextView itemName = new TextView(this);
                        itemName.setText(orderLine.getMenuItemName());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
                        params.setMargins(0,0,20,20);
                        itemName.setLayoutParams(params);
                        tr.addView(itemName);

                        TextView itemQuantity = new TextView(this);
                        itemQuantity.setText(orderLine.getQuantity());
                        params.setMargins(0,0,20,20);
                        itemQuantity.setLayoutParams(params);
                        tr.addView(itemQuantity);
                        container.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                    }
                }else{
                    Log.d(TAG,"No Line Items Found");
                }

                Log.d(TAG,"SUCCESSFUL");
            }else{
                Log.d(TAG,jsonObject.getString("message"));
                hideOrderDetails(jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }
    }

    public void hideOrderDetails(String message){
        Log.d(TAG,"__________________START hideOrderDetails_____________");
        TableRow orderStatusRow = (TableRow) findViewById(R.id.orderStatusRow);
        orderStatusRow.setVisibility(View.INVISIBLE);
        TableRow orderNumberRow = (TableRow) findViewById(R.id.orderNumberRow);
        orderNumberRow.setVisibility(View.INVISIBLE);
        TableRow orderTimeRow = (TableRow) findViewById(R.id.orderTimeRow);
        orderTimeRow.setVisibility(View.INVISIBLE);
        if(message.compareTo(getString(R.string.NORECORDFOUND))==0){
            Button createOrderBtn = (Button) findViewById(R.id.createOrderButton);
            createOrderBtn.setVisibility(View.VISIBLE);
        }
        Log.d(TAG,"___________________FINISH hideOrderDetails_____________");

    }

    public void createOrderDetails(){
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderStatus("In Progress");
        orderDetails.setTableId(tableId);
        orderDetails.setOrderTime(DateFormat.getDateTimeInstance().format(new Date()));
        JSONObject jsonObject = SFDCConnector.handleOrderCalls(tableId,getString(R.string.CREATE),orderDetails,null);
        try {
            if(jsonObject.getBoolean("isSuccessful")){
                Log.d(TAG,"Message:"+jsonObject.get("message"));
                Log.d(TAG,"SUCCESSFUL");
                getOrderDetails(jsonObject);
                Button createOrderBtn = (Button) findViewById(R.id.createOrderButton);
                createOrderBtn.setVisibility(View.GONE);
            }else{
                Log.d(TAG,jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }
    }

    public void updateOrderDetails(){
        JSONObject jsonObject = SFDCConnector.handleOrderCalls(tableId,getString(R.string.UPDATE),orderDetails,orderLineDetails);
        try {
            if(jsonObject.getBoolean("isSuccessful")){
                Log.d(TAG,"Message:"+jsonObject.get("message"));
                Log.d(TAG,"SUCCESSFUL");
                getOrderDetails(jsonObject);
            }else{
                hideOrderDetails(jsonObject.getString("message"));
                Button completeOrderBtn = (Button) findViewById(R.id.completeOrderbtn);
                completeOrderBtn.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }
    }


  @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.showMenuBtn:
                Intent intent = new Intent(this, MenuItem.class);
                Button btn = (Button) findViewById(v.getId());
                intent.putExtra(HOTEL_ID,hotelId);
                intent.putExtra(TABLE_ID,tableId);
                startActivity(intent);
                break;
            case R.id.createOrderButton:
                createOrderDetails();
                break;
            case R.id.completeOrderbtn:
                updateOrderDetails();
                break;
            default:
                break;
        }
    }
}
