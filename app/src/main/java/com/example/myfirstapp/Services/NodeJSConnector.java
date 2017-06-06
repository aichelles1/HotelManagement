package com.example.myfirstapp.Services;

import android.util.Log;

import com.example.myfirstapp.Models.UserDetails;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubhajain on 5/28/2017.
 */

public class NodeJSConnector {
    static final String TAG = "NodeJSConnector";
    final static String serverURL = "http://9c939361.ngrok.io/";

    public static void getConnection(){

        //HttpClient httpclient = HttpClientBuilder.create().build();
        HttpClient httpclient = new DefaultHttpClient();

        // Login requests must be POSTs
        HttpGet httpGet = new HttpGet(serverURL+"getUsers");
        HttpResponse response = null;

        try {
            // Execute the login POST request
            response = httpclient.execute(httpGet);
        } catch (ClientProtocolException cpException) {
            cpException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // verify response is HTTP OK
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            Log.d(TAG,"Error authenticating to Force.com: "+statusCode);
            for(Header h:response.getAllHeaders()){
                Log.d(TAG,h.getName()+":"+h.getValue());
            }
            // Error is in EntityUtils.toString(response.getEntity())
            return;
        }

        String getResult = null;
        try {
            getResult = EntityUtils.toString(response.getEntity());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        JSONObject jsonObject = null;
        String loginAccessToken = null;
        try {
            jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            Log.d(TAG,jsonObject.toString());
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }


    public static JSONObject doPost(Map<String, Object> paramsMap,Map<String,Object> configMap){
        //create the JSON object containing the new lead details.
            try {
                JSONObject jsonRequest = new JSONObject();

                HttpClient httpclient = new DefaultHttpClient();
                String url = serverURL+configMap.get("mappingURL");
                //Construct the objects needed for the request
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                for(String key:paramsMap.keySet()) {

                    Gson gson = new Gson();
                    String json = gson.toJson(paramsMap.get(key));
                   jsonRequest.put(key, json);
                }
                // The message we are going to post
                Log.d(TAG,jsonRequest.toString());

                StringEntity body = new StringEntity(jsonRequest.toString());
                body.setContentType("application/json");
                httpPost.setEntity(body);
                //Make the request
                HttpResponse response = httpClient.execute(httpPost);

                //Process the results
                int statusCode = response.getStatusLine().getStatusCode();
                Log.d(TAG,statusCode+"");
                if (statusCode == 200) {
                  String response_string = EntityUtils.toString(response.getEntity());
                    Log.d(TAG,response_string);
                    /*response_string = response_string.replaceAll("\\\\","");
                    response_string = response_string.substring(1, response_string.length()-1);*/
                    JSONObject jsonObject = new JSONObject(response_string);
                    return jsonObject;
                    // Store the retrieved lead id to use when we update the lead.
                } else {
                    return null;
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    return null;
}
    
    
    
    
}
