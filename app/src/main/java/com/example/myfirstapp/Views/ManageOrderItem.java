package com.example.myfirstapp.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.myfirstapp.R;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManageOrderItem extends AppCompatActivity {
    private static final String TAG = "ManageOrderItem";
    public static final String MENU_ITEM_ID = "MENU_ITEM_ID";
    public static final String ORDER_ID = "ORDER_ID";
    public static String menuItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order_item);
        Intent intent = getIntent();
        menuItemId = intent.getStringExtra(MenuItem.MENU_ITEM_ID);
        //Server Call to get existing orderLine
        String jsonString = "{\"objectData\":{\"orderLineDetail\":{\"Id\":\"a0s28000002qhlHAAQ\",\"Name\":\"ORL-0000000018\",\"Menu_Item_Name__c\":\"Kadhai Paneer\",\"Quantity__c\":\"2\"}},\"message\":null,\"isSuccessful\":true}";
        jsonString = jsonString.replaceAll("\\\\","");
        Log.d(TAG,"Before:"+jsonString);
        //jsonString = jsonString.substring(1, jsonString.length()-1);
        Log.d(TAG,"After:"+jsonString);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            getOrderLineDetails(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getOrderLineDetails(JSONObject jsonObject){
        if(jsonObject==null){
            //jsonObject = SFDCConnector.handleOrderLineItemsCalls(getString(R.string.READ),orderId,menuItemId,orderLineItemId);
        }
        try {
            if(jsonObject.getBoolean("isSuccessful")){

                JSONObject objectData = jsonObject.getJSONObject("objectData");
                Map<String, Object> retMap = new Gson().fromJson(
                        objectData+"", new TypeToken<HashMap<String, Object>>() {}.getType()
                );
                Log.d(TAG,"Data:"+retMap.get("orderLineDetail").toString());
                LinkedTreeMap<String,Object> orderLineDetail = (LinkedTreeMap<String,Object>)retMap.get("orderLineDetail");






                Log.d(TAG,"SUCCESSFUL");
            }else{
                Log.d(TAG,jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }

    }

}
