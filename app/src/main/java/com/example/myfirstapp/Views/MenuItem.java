package com.example.myfirstapp.Views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myfirstapp.Models.HotelTable;
import com.example.myfirstapp.Models.MenuItemDetails;
import com.example.myfirstapp.SFDCConnector;
import com.example.myfirstapp.Services.DesignUtilClass;
import com.example.myfirstapp.R;
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

public class MenuItem extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MenuItem";
    public static final String HOTEL_ID = "HOTEL_ID";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String TABLE_ID = "TABLE_ID";
    public static final String MENU_ITEM_ID = "MENU_ITEM_ID";
    public static String hotelId;
    public static String orderId;
    public static String tableId;
    public static Map<String,String> tableData = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        JSONObject jsonObject = ServiceController.MenuItemDetailsHandler(null);
         Log.d(TAG,jsonObject.toString());
        Intent intent = getIntent();
        try {
            if(jsonObject.getBoolean("isSuccessful")){

                List<MenuItemDetails> lstMenuItems = (List<MenuItemDetails>) jsonObject.get("lstMenuItems");
                Log.d(TAG,"Size:"+lstMenuItems.size());

                TableLayout container = (TableLayout) findViewById(R.id.activity_menu_item);
                int i;
                ScrollView scrollView = new ScrollView(this);
                ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT,ScrollView.LayoutParams.WRAP_CONTENT);


                LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(params);
                for(i=0;i<lstMenuItems.size();i++) {
                    Log.d(TAG,lstMenuItems.get(i).toString());

                    MenuItemDetails menuItem = lstMenuItems.get(i);
                    TableLayout table = new TableLayout(this);
                    TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                    table.setLayoutParams(tableParams);
                    table.setOnClickListener(this);
                    int id = ViewIdGenerator.generateViewId();
                    //Logic for generating unique ids.
                    while(tableData.containsKey(String.valueOf(id))){
                        id = ViewIdGenerator.generateViewId();
                    }
                    table.setId(id);
                    tableData.put(String.valueOf(id),menuItem.getId());

                    TableRow itemNameRow= new TableRow(this);
                    TextView itemName = new TextView(this);
                    itemName.setText(menuItem.getItemName());
                    itemName.setTextSize(18);
                    itemName.setTypeface(null, Typeface.BOLD);
                    itemNameRow.addView(itemName);

                    TableRow itemDescRow= new TableRow(this);
                    TextView itemType = new TextView(this);
                    itemType.setText(menuItem.getItemType());
                        if(Build.VERSION.SDK_INT>=16){
                            itemType.setBackground(DesignUtilClass.drawCircle(this,50,50,10));
                        }else{
                            itemType.setBackgroundDrawable(DesignUtilClass.drawCircle(this,50,50,10));
                        }
                        itemDescRow.addView(itemType);


                    TableRow itemPriceRow= new TableRow(this);
                    TextView itemPrice = new TextView(this);
                    itemPrice.setText("Rs. "+menuItem.getAmount());

                    itemPriceRow.addView(itemPrice);

                    TableRow separatorRow= new TableRow(this);
                    View separator = new View(this);
                    separator.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    separator.setBackgroundColor(Color.rgb(51, 51, 51));
                    separatorRow.addView(separator);

                    table.addView(itemNameRow);
                    table.addView(itemDescRow);
                    table.addView(itemPriceRow);
                    table.addView(separatorRow);
                    linearLayout.addView(table);
                }
                scrollView.addView(linearLayout);
                container.addView(scrollView);
            }else{
                Log.d(TAG,jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getClass().getName()){
            case "android.widget.TableLayout":
                Log.d(TAG, tableData.get(String.valueOf(v.getId())));
                Intent intent = new Intent(this, MenuItemDescription.class);
                intent.putExtra(MENU_ITEM_ID,tableData.get(String.valueOf(v.getId())));
                startActivity(intent);
            break;
            default:

                break;
        }


    }
}
