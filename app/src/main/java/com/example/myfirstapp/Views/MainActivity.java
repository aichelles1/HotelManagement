package com.example.myfirstapp.Views;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.SFDCConnector;
import com.example.myfirstapp.Services.NodeJSConnector;
import com.example.myfirstapp.Services.ServiceController;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MyActivity";
    public static final String HOTEL_ID = "HOTEL_ID";
    public static final String HOTEL_NAME = "HOTEL_NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.signupbtn);
        button.setOnClickListener(this);
        Button button2 = (Button)findViewById(R.id.loginbtn);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.signupbtn:
                JSONObject jsonObject = ServiceController.mainActivityHandler(null,null,null);
                Log.d(TAG, jsonObject.toString());
                try {
                    if(jsonObject.getBoolean("isSucessful")==true){
                        Intent intent = new Intent(this,PostLogin.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }{

                }

                break;

            case R.id.loginbtn:
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                TextView tv1 = (TextView)findViewById(R.id.message);
                //String passKey = password.getText()+"";
                String passKey = "0123456789";
                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    tv1.setText("Please enter username & password");
                }else{

                    ServiceController.mainActivityHandler(username.getText().toString(),password.getText().toString(),null);
                }
                break;

            default:
                break;
        }



    }
}

