package com.example.anmol.hazewatch.Communication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anmol on 9/5/2016.
 */
public class DBConnect extends AsyncTask<String,Void,String> {
    private Context context;
    private String request;
    //private String response;

    public DBConnect(Context context, String request) {
        this.context = context;
        this.request = request;
        //this.response = response;
    }

    protected void onPreExecute(){

        //Toast.makeText(context,"dasdas",Toast.LENGTH_SHORT).show();
        Log.d("DBConnect", "CalledDb Connect");
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{

            HttpURLConnection conn = getConnection();
            conn.connect();
            Log.d("connected","Connected");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(request);
            wr.flush();

            InputStream is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            return rd.readLine();
        }
        catch(Exception e){
            Log.d("Exception","Exception" + e.getMessage());
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result){
        //Gson gson = new Gson();
        //String response = gson.fromJson(result);
        //UserLoginModel userLogin = gson.fromJson(result,UserLoginModel.class);
        //response = result;
        //Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        Communication l = (Communication) context;
        l.onCompletion(result);
    }

    private HttpURLConnection getConnection() throws Exception
    {
        //String urlString = "http://172.31.73.146/ServerFiles_Capstone/receive.php";
        //String urlString = "http://115.248.114.147/sensor/receive.php";
        String urlString = "http://pecfest.in/Hazeaway_Capstone/receive.php";
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //conn.setReadTimeout(10000 /*milliseconds*/);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        // Set Headers Here
        //conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        //conn.setRequestProperty("sessionId", Utility.loggedInUser.sessionId);

        return  conn;
    }
}
