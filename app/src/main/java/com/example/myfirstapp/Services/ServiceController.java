package com.example.myfirstapp.Services;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.myfirstapp.Models.HotelTable;
import com.example.myfirstapp.Models.MenuItemDetails;
import com.example.myfirstapp.Models.OrderDetails;
import com.example.myfirstapp.Models.OrderLineDetails;
import com.example.myfirstapp.Models.SessionInformation;
import com.example.myfirstapp.Models.UserDetails;
import com.example.myfirstapp.Views.HomeWaiter;
import com.example.myfirstapp.Views.MainActivity;
import com.example.myfirstapp.Views.OrderPlace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubhajain on 5/30/2017.
 */

public class ServiceController extends AppCompatActivity{
    static final String TAG = "ServiceController";
    public static JSONObject mainActivityHandler(String username, String password,String adminPassword) {
        JSONObject retJsonObject = new JSONObject();
        Map<String, Object> paramsMap = new HashMap<String,Object>();
        Map<String, Object> configMap = new HashMap<String,Object>();
        UserDetails userDetail = new UserDetails();
        if(username==null){
            userDetail.setUsername("Admin!1");
            userDetail.setPassword("password");
            userDetail.setAdminPassword(adminPassword);
        }else{
            userDetail.setUsername(username);
            userDetail.setPassword(password);
            userDetail.setAdminPassword(adminPassword);
        }
        paramsMap.put("jsonObject",userDetail);
        configMap.put("mappingURL","doLogin");
        JSONObject jsonObject = NodeJSConnector.doPost(paramsMap,configMap);
        Log.d(TAG,jsonObject.toString());
        try {
            if(jsonObject.getBoolean("isSuccess")){
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("resultArray");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray jsonObjectResultArray = jsonObjectResult.getJSONArray("Users");
                Log.d(TAG,jsonObjectResultArray.toString());
                Log.d(TAG, String.valueOf(jsonObjectResultArray.length()));
                for(int i=0; i< jsonObjectResultArray.length();i++){
                    retJsonObject = (JSONObject) jsonObjectResultArray.get(i);
                    retJsonObject.put("isSucessful",true);
                    SessionInformation.setHotelId((String) retJsonObject.get("_id"));
                    SessionInformation.setHotelName((String) retJsonObject.get("HotelName"));
                    return retJsonObject;
                }
            }else{
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("errorLIst");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray jsonObjectResultArray = jsonObjectResult.getJSONArray("Errors");
                if(jsonObjectResultArray.length()!=0){
                    for(int i=0; i< jsonObjectResultArray.length();i++){
                        retJsonObject = (JSONObject) jsonObjectResultArray.get(i);
                        Log.d(TAG,retJsonObject.toString());
                        retJsonObject.put("isSucessful",false);
                        return retJsonObject;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retJsonObject;
    }


    public static JSONObject homeWaiterHandler() {
        JSONObject retJsonObject = new JSONObject();
        Map<String, Object> paramsMap = new HashMap<String,Object>();
        Map<String, Object> configMap = new HashMap<String,Object>();
        paramsMap.put("hotelId",SessionInformation.getHotelId());
        configMap.put("mappingURL","getTableDetails");
        JSONObject jsonObject = NodeJSConnector.doPost(paramsMap,configMap);
        Log.d(TAG,jsonObject.toString());

        try {
            if(jsonObject.getBoolean("isSuccess")){
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("resultArray");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray jsonObjectResultArray = jsonObjectResult.getJSONArray("Order_Table");
                Log.d(TAG,jsonObjectResultArray.toString());
                Log.d(TAG, String.valueOf(jsonObjectResultArray.length()));
                ArrayList<HotelTable> lstHotelTable = new ArrayList<HotelTable>();
                for(int i=0; i< jsonObjectResultArray.length();i++){
                    retJsonObject = (JSONObject) jsonObjectResultArray.get(i);
                    lstHotelTable.add(new HotelTable(retJsonObject.getString("_id"), retJsonObject.getString("Name"), retJsonObject.getString("Hotel_Id"), retJsonObject.getString("Status")));
                }
                if(jsonObjectResultArray.length()>0){
                    retJsonObject.put("isSucessful",true);
                    retJsonObject.put("lstHotelTable",lstHotelTable);
                }
                return retJsonObject;
            }else{
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("errorLIst");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray jsonObjectResultArray = jsonObjectResult.getJSONArray("Errors");
                if(jsonObjectResultArray.length()!=0){
                    for(int i=0; i< jsonObjectResultArray.length();i++){
                        retJsonObject = (JSONObject) jsonObjectResultArray.get(i);
                        Log.d(TAG,retJsonObject.toString());
                        retJsonObject.put("isSucessful",false);
                        return retJsonObject;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retJsonObject;
    }

    public static JSONObject MenuItemDetailsHandler(String menuItemId) {
        JSONObject retJsonObject = new JSONObject();
        Map<String, Object> paramsMap = new HashMap<String,Object>();
        Map<String, Object> configMap = new HashMap<String,Object>();
        if(menuItemId!=null){
            paramsMap.put("menuItemId",menuItemId);
            configMap.put("mappingURL","getMenuItemDescription");
        }else{
            paramsMap.put("hotelId",SessionInformation.getHotelId());
            configMap.put("mappingURL","getMenuItemDetails");
        }
        JSONObject jsonObject = NodeJSConnector.doPost(paramsMap,configMap);

        try {
            if(jsonObject.getBoolean("isSuccess")){
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("resultArray");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray jsonObjectResultArray = jsonObjectResult.getJSONArray("Menu_Item");
                Log.d(TAG,jsonObjectResultArray.toString());
                Log.d(TAG, String.valueOf(jsonObjectResultArray.length()));
                ArrayList<MenuItemDetails> lstMenuItems = new ArrayList<MenuItemDetails>();
                for(int i=0; i< jsonObjectResultArray.length();i++){
                    retJsonObject = (JSONObject) jsonObjectResultArray.get(i);
                    lstMenuItems.add(new MenuItemDetails(retJsonObject.getString("_id"),retJsonObject.getString("Hotel_Id"),retJsonObject.getString("Name"),retJsonObject.getDouble("Amount__c"), retJsonObject.getString("Description__c"), retJsonObject.getString("Type__c"), retJsonObject.getString("Menu_Item_Image_URL__c")));
                }
                if(jsonObjectResultArray.length()>0){
                    retJsonObject.put("isSuccessful",true);
                    retJsonObject.put("lstMenuItems",lstMenuItems);
                }
                return retJsonObject;
            }else{
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("errorLIst");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray jsonObjectResultArray = jsonObjectResult.getJSONArray("Errors");
                if(jsonObjectResultArray.length()!=0){
                    for(int i=0; i< jsonObjectResultArray.length();i++){
                        retJsonObject = (JSONObject) jsonObjectResultArray.get(i);
                        Log.d(TAG,retJsonObject.toString());
                        retJsonObject.put("isSucessful",false);
                        return retJsonObject;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retJsonObject;
    }



    public static JSONObject orderPlaceHandler(String mode) {
        JSONObject retJsonObject = new JSONObject();
        Map<String, Object> paramsMap = new HashMap<String,Object>();
        Map<String, Object> configMap = new HashMap<String,Object>();
        paramsMap.put("tableId",SessionInformation.getHotelTableId());
        paramsMap.put("mode",mode);
        configMap.put("mappingURL","getOrderDetails");
        JSONObject jsonObject = NodeJSConnector.doPost(paramsMap,configMap);
        Log.d(TAG,jsonObject.toString());

        try {
            if(jsonObject.getBoolean("isSuccess")){
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("resultArray");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray orderDetailJSONArray = jsonObjectResult.getJSONArray("Order");
                Log.d(TAG,orderDetailJSONArray.toString());
                Log.d(TAG, String.valueOf(orderDetailJSONArray.length()));

                ArrayList<OrderDetails> lstOrderDetails = new ArrayList<OrderDetails>();
                for(int i = 0; i< orderDetailJSONArray.length(); i++){
                    retJsonObject = (JSONObject) orderDetailJSONArray.get(i);
                    lstOrderDetails.add(new OrderDetails(retJsonObject.getString("_id"),"Test order Number",retJsonObject.getString("Table_Id"), retJsonObject.getString("Order_Date_Time"), retJsonObject.getString("Order_Status")));
                }

                JSONArray orderLineJSONArray = jsonObjectResult.getJSONArray("Order_Line_Item");
                Log.d(TAG,orderLineJSONArray.toString());
                Log.d(TAG, String.valueOf(orderLineJSONArray.length()));
                ArrayList<OrderLineDetails> lstOrderLineDetails = new ArrayList<OrderLineDetails>();
                for(int i = 0; i< orderLineJSONArray.length(); i++){
                    retJsonObject = (JSONObject) orderLineJSONArray.get(i);
                    Log.d(TAG,retJsonObject.toString());
                    // /lstOrderLineDetails.add(new OrderLineDetails(retJsonObject.getString("_id"), retJsonObject.getString("menuItemId"),retJsonObject.getString("orderId"), retJsonObject.getString("quantity"), retJsonObject.getString("menuItemName")));
                }

                if(lstOrderDetails.size()>0){
                    retJsonObject.put("isSucessful",true);
                    retJsonObject.put("lstOrderDetails",lstOrderDetails);
                    retJsonObject.put("lstOrderLineDetails",lstOrderLineDetails);
                }
                return retJsonObject;
            }else{
                JSONObject jsonObjectResult = (JSONObject) jsonObject.get("errorLIst");
                Log.d(TAG,jsonObjectResult.toString());
                JSONArray jsonObjectResultArray = jsonObjectResult.getJSONArray("Errors");
                if(jsonObjectResultArray.length()!=0){
                    for(int i=0; i< jsonObjectResultArray.length();i++){
                        retJsonObject = (JSONObject) jsonObjectResultArray.get(i);
                        Log.d(TAG,retJsonObject.toString());
                        retJsonObject.put("isSucessful",false);
                        return retJsonObject;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retJsonObject;
    }








}
