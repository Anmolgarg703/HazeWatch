package com.example.anmol.hazewatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.jaskirat.hazewatch.SensorActivity;


import com.example.anmol.hazewatch.Communication.Communication.*;
import com.example.anmol.hazewatch.Communication.DBConnect;
import com.example.anmol.hazewatch.Communication.Request;
import com.example.anmol.hazewatch.JSONClasses.UserLoginModel;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private EditText mPhone;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Remove the below two lines to enable login
       // Intent sensorActivity = new Intent(this, SensorActivity.class);
        //startActivity(sensorActivity);
        mPhone = (EditText)findViewById(R.id.phone);
        mPassword = (EditText)findViewById(R.id.password);
    }

    public void processForm(View v){
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();

        loginUser(phone,password);
    }

    private void loginUser(String phone, String password) {
        UserLoginModel userLogin = new UserLoginModel(phone,password);
        Request request = new Request("loginUser");
        Gson gson = new Gson();
        request.setRequest(gson.toJson(userLogin));
        String requestObject = gson.toJson(request);
        new DBConnect(this, requestObject).execute();
    }

    public void signUp(View v){
        Intent signUpRedirect = new Intent(this,SignUpActivity.class);
        startActivity(signUpRedirect);
    }

   // public void loadExternalSensorsPage(View v){
   //     Intent externalSensorRedirect = new Intent(this,ExternalSensors.class);
   //     startActivity(externalSensorRedirect);
   // }


    public void onCompletion(String response) {
        Gson gson = new Gson();
        UserLoginModel userLogin = gson.fromJson(response, UserLoginModel.class);
        if(userLogin.getLogin().equals("1")){
            Intent mainOptionsFragment = new Intent(this, SensorActivity.class);
            startActivity(mainOptionsFragment);
        }
        else{
            Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show();
        }
    }
}
