package com.example.myfirstapp.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myfirstapp.R;

import java.util.HashMap;
import java.util.Map;

public class MenuItemCategory extends AppCompatActivity {
    private static final String TAG = "MenuItemCategory";
    public static final String HOTEL_ID = "HOTEL_ID";
    public static String hotelId;
    public static Map<String,String> tableData = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_category);
        Intent intent = getIntent();
        //hotelId = intent.getStringExtra(HomeWaiter.HOTEL_ID);



    }
}
