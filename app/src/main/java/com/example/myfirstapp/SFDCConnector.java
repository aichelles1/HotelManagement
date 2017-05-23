package com.example.myfirstapp;

/**
 * Created by shubhajain on 5/23/2017.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

public class SFDCConnector {

    static final String USERNAME     = "shubham@dev450.com";
    static final String PASSWORD     = "Chhotu!5";
    static final String LOGINURL     = "https://login.salesforce.com";
    static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
    static final String CLIENTID     = "3MVG9ZL0ppGP5UrBYN430wIxgDm4XiYCCfUtO35GpfkOsvp.oxvp_QqXPevtEzGLOsutMd.MfKNfkCiqFMKYu";
    static final String CLIENTSECRET = "1871916102086625552";
    static final String TAG = "SFDCConnector";
    private static String REST_ENDPOINT = "/services/data" ;
    private static String API_VERSION = "/v32.0" ;
    private static String baseUri;
    private static Header oauthHeader;
    private static Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
    private static String leadId ;
    private static String leadFirstName;
    private static String leadLastName;
    private static String leadCompany;
    static String loginInstanceUrl = null;


    public static void getConnection() {

        //HttpClient httpclient = HttpClientBuilder.create().build();
        HttpClient httpclient = new DefaultHttpClient();
        // Assemble the login request URL
        String loginURL = LOGINURL +
                GRANTSERVICE +
                "&client_id=" + CLIENTID +
                "&client_secret=" + CLIENTSECRET +
                "&username=" + USERNAME +
                "&password=" + PASSWORD;

        // Login requests must be POSTs
        HttpPost httpPost = new HttpPost(loginURL);
        HttpResponse response = null;

        try {
            // Execute the login POST request
            response = httpclient.execute(httpPost);
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
            loginAccessToken = jsonObject.getString("access_token");
            loginInstanceUrl = jsonObject.getString("instance_url");
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        Log.d(TAG,response.getStatusLine().toString());
        Log.d(TAG,"Successful login");
        Log.d(TAG,"  instance URL: "+loginInstanceUrl);
        Log.d(TAG,"  access token/session ID: "+loginAccessToken);

       // baseUri = loginInstanceUrl + REST_ENDPOINT + API_VERSION ;
        oauthHeader = new BasicHeader("Authorization", "OAuth " + loginAccessToken) ;
        System.out.println("baseUri: "+ baseUri);
        //return queryLeads();
        //return queryLeads();
        //return doGet();


        // release connection
        //httpPost.releaseConnection();
    }

    // Query Leads using REST HttpGet
    public static String queryLeads() {
        System.out.println("\n_______________ Lead QUERY _______________");
        String leadData = "";

        try {

            //Set up the HTTP objects needed to make the request.
            HttpClient httpClient = new DefaultHttpClient();

            String uri = loginInstanceUrl + REST_ENDPOINT + API_VERSION + "/query?q=Select+Id+,+FirstName+,+LastName+,+Company+From+Lead+Limit+5";
            System.out.println("Query URL: " + uri);
            HttpGet httpGet = new HttpGet(uri);
            System.out.println("oauthHeader2: " + oauthHeader);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);

            // Make the request.
            HttpResponse response = httpClient.execute(httpGet);

            // Process the result
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String response_string = EntityUtils.toString(response.getEntity());
                try {
                    JSONObject json = new JSONObject(response_string);
                    System.out.println("JSON result of Query:\n" + json.toString(1));
                    JSONArray j = json.getJSONArray("records");
                    for (int i = 0; i<j.length(); i++){
                        leadId = json.getJSONArray("records").getJSONObject(i).getString("Id");
                        leadFirstName = json.getJSONArray("records").getJSONObject(i).getString("FirstName");
                        leadLastName = json.getJSONArray("records").getJSONObject(i).getString("LastName");
                        leadCompany = json.getJSONArray("records").getJSONObject(i).getString("Company");
                        leadData+="Lead record is: " + i + ". " + leadId + " " + leadFirstName + " " + leadLastName + "(" + leadCompany + ")";
                    }

                } catch (JSONException je) {
                    je.printStackTrace();
                }
            } else {
                System.out.println("Query was unsuccessful. Status code returned is " + statusCode);
                System.out.println("An error has occured. Http status: " + response.getStatusLine().getStatusCode());
                System.out.println(getBody(response.getEntity().getContent()));
                System.exit(-1);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        return leadData;
    }

    // Query Leads using REST HttpGet
    public static String doGet() {
        try {

            //Set up the HTTP objects needed to make the request.
            HttpClient httpClient = new DefaultHttpClient();

            String uri = loginInstanceUrl +  "/services/apexrest/loginController/";
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);

            // Make the request.
            HttpResponse response = httpClient.execute(httpGet);

            // Process the result
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String response_string = EntityUtils.toString(response.getEntity());
                try {
                    return response_string;
                    /*JSONObject json = new JSONObject(response_string);
                    return "1:"+response_string;*/
                } catch (Exception je) {
                    return "2:"+je.getMessage().toString();
                }
            } else {
                return "3:"+getBody(response.getEntity().getContent());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        return "Code Executed";
    }

    // Query Leads using REST HttpGet
    public static String handleHotel() {
        Log.d(TAG,"\n_______________ Lead INSERT _______________");

        String uri = loginInstanceUrl +  "/services/apexrest/getHotelDetails/";
        try {

            //create the JSON object containing the new lead details.
            JSONObject lead = new JSONObject();
            lead.put("passKey", "0123456789");

            //Construct the objects needed for the request
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader(oauthHeader);
            httpPost.addHeader(prettyPrintHeader);
            // The message we are going to post
            StringEntity body = new StringEntity(lead.toString(1));
            body.setContentType("application/json");
            httpPost.setEntity(body);

            //Make the request
            HttpResponse response = httpClient.execute(httpPost);

            //Process the results
            int statusCode = response.getStatusLine().getStatusCode();
            Log.d(TAG,statusCode+"");
            if (statusCode == 200) {
                String response_string = EntityUtils.toString(response.getEntity());
                response_string = response_string.replaceAll("\\\\","");
                Log.d(TAG,"before:"+response_string);
                //response_string = response_string.replaceAll("\"","");
                //Log.d(TAG,"after:"+response_string);
                response_string = response_string.substring(1, response_string.length()-1);
                JSONObject jsonObject = new JSONObject(response_string);
                Log.d(TAG,"isSuccessful:"+jsonObject.get("isSuccessful"));
                return "SUCCESS";
                // Store the retrieved lead id to use when we update the lead.
            } else {
                Log.d(TAG,"Insertion unsuccessful. Status code returned is " + statusCode);
            }
        } catch (JSONException e) {
            Log.d(TAG,"Issue creating JSON or processing results");
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return "Error";
    }

    private static String getBody(InputStream inputStream) {
        String result = "";
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inputStream)
            );
            String inputLine;
            while ( (inputLine = in.readLine() ) != null ) {
                result += inputLine;
                result += "\n";
            }
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

    // Query Leads using REST HttpGet
    public static String handleHotelCalls() {
        Log.d(TAG,"\n_______________ inside handleHotelCalls _______________");

        String uri = loginInstanceUrl +  "/services/apexrest/getHotelDetails/";
        try {
            //create the JSON object containing the new lead details.
            JSONObject lead = new JSONObject();
            lead.put("passKey", "0123456789");

            //Construct the objects needed for the request
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader(oauthHeader);
            httpPost.addHeader(prettyPrintHeader);
            // The message we are going to post
            StringEntity body = new StringEntity(lead.toString(1));
            body.setContentType("application/json");
            httpPost.setEntity(body);

            //Make the request
            HttpResponse response = httpClient.execute(httpPost);

            //Process the results
            int statusCode = response.getStatusLine().getStatusCode();
            Log.d(TAG,statusCode+"");
            if (statusCode == 200) {
                String response_string = EntityUtils.toString(response.getEntity());
                response_string = response_string.replaceAll("\\\\","");
                Log.d(TAG,"before:"+response_string);
                //response_string = response_string.replaceAll("\"","");
                //Log.d(TAG,"after:"+response_string);
                response_string = response_string.substring(1, response_string.length()-1);
                JSONObject jsonObject = new JSONObject(response_string);
                Log.d(TAG,"isSuccessful:"+jsonObject.get("isSuccessful"));
                return "SUCCESS";
                // Store the retrieved lead id to use when we update the lead.
            } else {
                Log.d(TAG,"Insertion unsuccessful. Status code returned is " + statusCode);
            }
        } catch (JSONException e) {
            Log.d(TAG,"Issue creating JSON or processing results");
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return "Error";
    }






}