package com.example.myfirstapp.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfirstapp.Models.SessionInformation;
import com.example.myfirstapp.R;
import com.example.myfirstapp.Services.ServiceController;

public class PostLogin extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "PostLogin";
    public static final String HOTEL_ID = "HOTEL_ID";
    public static final String HOTEL_NAME = "HOTEL_NAME";
    public static String hotelId;
    public static String hotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        TextView tv = (TextView) findViewById(R.id.welcomeTxt);
        tv.setText("Welcome to "+ SessionInformation.getHotelName());

        Button waiterbtn = (Button)findViewById(R.id.waiterbtn);
        waiterbtn.setOnClickListener(this);

        Button adminbtn = (Button)findViewById(R.id.adminbtn);
        adminbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.waiterbtn:
                Intent intent = new Intent(this, HomeWaiter.class);
                startActivity(intent);
                break;
            default:
                Log.d(TAG,"Select a valid option");
        }
    }
}
