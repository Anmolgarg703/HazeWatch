package com.example.anmol.hazewatch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.R;
import com.example.anmol.hazewatch.Communication.Communication;
import com.example.anmol.hazewatch.Communication.DBConnect;
import com.example.anmol.hazewatch.Communication.DBHelper;
import com.example.jaskirat.hazewatch.SensorActivity;

import java.util.ArrayList;

public class SyncDatabase extends AppCompatActivity implements Communication{

    DBHelper mydb;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_database);

        mydb = new DBHelper(this);

        boolean isConnectedToInternet = checkInternetConnection();
        if(isConnectedToInternet) {
            ArrayList dataEntries = mydb.getAllEntries();
            for(Object dataEntry : dataEntries){
                data = dataEntry.toString();
                new DBConnect(this, data).execute();
                mydb.deleteRecord(data);
                Log.d("Sync Db","Number of Rows: "+ mydb.getNumberOfRows());
            }
            if(mydb.getNumberOfRows()==0){
                Toast.makeText(this, "All entries successfully synced", Toast.LENGTH_SHORT).show();
                Intent mainOptions = new Intent(this, SensorActivity.class);
                mainOptions.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainOptions);
                finish();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "You are not connected to the internet", Toast.LENGTH_SHORT).show();
            Intent mainOptions = new Intent(this, SensorActivity.class);
            mainOptions.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainOptions);
            finish();
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = connec.getActiveNetworkInfo();

        if(ni==null){
            return false;
        }
        else{
            if(ni.isConnected()){
                return true;
            }
            else{
                return false;
            }
        }
    }

    @Override
    public void onCompletion(String response) {
        //Toast.makeText(this, "Deleting "+ data, Toast.LENGTH_SHORT).show();
        //mydb.deleteRecord(data);
    }
}
