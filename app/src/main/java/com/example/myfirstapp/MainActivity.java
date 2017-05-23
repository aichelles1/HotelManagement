package com.example.myfirstapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MyActivity";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button:
                String url = "http://www.google.com/search?q=developer";

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                // add request header
                request.addHeader("User-Agent", USER_AGENT);

                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,"\nSending 'GET' request to URL : " + url);
                Log.d(TAG,"Response Code : " +
                        response.getStatusLine().getStatusCode());

                BufferedReader rd = null;
                try {
                    rd = new BufferedReader(
                            new InputStreamReader(response.getEntity().getContent()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                StringBuffer result = new StringBuffer();
                String line = "";
                try {
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,result.toString());
                TextView tv = (TextView)findViewById(R.id.TextView1);
                tv.setText(result);

               break;

            case R.id.button2:
                TextView tv1 = (TextView)findViewById(R.id.TextView1);
                SFDCConnector.getConnection();
                tv1.setText(SFDCConnector.handleHotel());

                /*Intent intent = new Intent(this, DisplayMessageActivity.class);
                EditText editText = (EditText) findViewById(R.id.editText1);
                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);*/
                break;

            default:
                break;
        }



    }
}
