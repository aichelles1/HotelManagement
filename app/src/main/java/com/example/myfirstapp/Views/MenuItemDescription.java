package com.example.myfirstapp.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstapp.Models.MenuItemDetails;
import com.example.myfirstapp.R;
import com.example.myfirstapp.SFDCConnector;
import com.example.myfirstapp.Services.ServiceController;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuItemDescription extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MenuItemDescription";
    public static final String MENU_ITEM_ID = "MENU_ITEM_ID";
    public static final String MENU_ITEM_NAME = "MENU_ITEM_NAME";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String QUANTITY = "QUANTITY";
    public static final String TABLE_ID = "TABLE_ID";
    public static final String HOTEL_ID = "HOTEL_ID";


    public static String menuItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_description);
        Intent intent = getIntent();
        menuItemId = intent.getStringExtra(MenuItem.MENU_ITEM_ID);

        ImageView removebtn= (ImageView) findViewById(R.id.removebtn);
        removebtn.setOnClickListener(this);
        ImageView addbtn= (ImageView) findViewById(R.id.addbtn);
        addbtn.setOnClickListener(this);
        Button addToCartBtn = (Button) findViewById(R.id.addToCartbtn);
        addToCartBtn.setOnClickListener(this);

        JSONObject jsonObject = ServiceController.MenuItemDetailsHandler(menuItemId);
        Log.d(TAG,jsonObject.toString());
        getMenuItemDetails(jsonObject);

    }


    public void getMenuItemDetails(JSONObject jsonObject){
        try {
            if(jsonObject.getBoolean("isSuccessful")){

                List<MenuItemDetails> lstMenuItems = (List<MenuItemDetails>) jsonObject.get("lstMenuItems");
                MenuItemDetails menuItem = lstMenuItems.get(0);

                TextView menuItemName = (TextView) findViewById(R.id.menuItemName);
                menuItemName.setText(menuItem.getItemName());

                    TextView menuItemDesc = (TextView) findViewById(R.id.menuItemDesc);
                    Log.d(TAG,menuItem.getItemDescription());
                    menuItemDesc.setText(menuItem.getItemDescription());

                    TextView menuItemPrice = (TextView) findViewById(R.id.menuItemPrice);
                    menuItemPrice.setText("Rs. "+menuItem.getAmount());

                    ImageView menuItemImage = (ImageView) findViewById(R.id.menuItemTypeImage);
                    if(menuItem.getItemType().compareTo("Veg")==0){
                        menuItemImage.setImageResource(R.drawable.veg_symbol);
                    }else if(menuItem.getItemType().compareTo("Non-Veg")==0){
                        menuItemImage.setImageResource(R.drawable.non_veg_symbol);
                    }else{
                        menuItemImage.setImageResource(R.drawable.egg_symbol);
                    }
                }
                Log.d(TAG,"SUCCESSFUL");
            } catch (JSONException e1) {
               e1.printStackTrace();
            }

    }

    @Override
    public void onClick(View v) {
        TextView quantityView = (TextView) findViewById(R.id.quantity);
        TextView menuItemNameView = (TextView) findViewById(R.id.menuItemName);

        int quantity = Integer.parseInt(quantityView.getText().toString());
        switch (v.getId()){
            case R.id.removebtn:
                if(quantity!=0){
                    quantity--;
                }
                break;
            case R.id.addbtn:
                quantity++;
                break;
            case R.id.addToCartbtn:
                Intent intent = new Intent(this, OrderPlace.class);
                intent.putExtra(MENU_ITEM_ID,menuItemId);
                intent.putExtra(MENU_ITEM_NAME,menuItemNameView.getText());
                intent.putExtra(QUANTITY,String.valueOf(quantity));
                startActivity(intent);

                break;
            default:
                Log.d(TAG,"No case matched");
                break;
        }
        quantityView.setText(String.valueOf(quantity));

    }
}

