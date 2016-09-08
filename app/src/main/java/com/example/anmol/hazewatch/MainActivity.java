package com.example.anmol.hazewatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        //Intent readings = new Intent(this, Readings.class);
        //startActivity(readings);
        mPhone = (EditText)findViewById(R.id.phone);
        mPassword = (EditText)findViewById(R.id.password);
    }

    public void processForm(View v){
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();

        loginUser(phone,password);

        if (validatePhoneNumber(phone) && phone.length() == 10 && (phone.charAt(0) == '7' || phone.charAt(0) == '8' || phone.charAt(0) == '9')) {
            if(password.length()>=8){
                loginUser(phone,password);
                Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show();
                //Login Successful
                Intent readings = new Intent(this, Readings.class);
                startActivity(readings);
                //Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Password must be atleast 8 characters long", Toast.LENGTH_SHORT).show();
                mPassword.requestFocus();
            }
        }
        else{
            Toast.makeText(this,"Invalid Phone Number", Toast.LENGTH_SHORT).show();
            mPhone.requestFocus();
        }
    }

     private void loginUser(String phone, String password) {
        UserLoginModel userLogin = new UserLoginModel(phone,password);
        Request request = new Request("loginUser");
        Gson gson = new Gson();
        request.setRequest(gson.toJson(userLogin));
        String requestObject = gson.toJson(request);
        //String response = null;
        new DBConnect(this, requestObject).execute();
        //return response;
    }

    public void signUp(View v){
        Intent signUpRedirect = new Intent(this,SignUpActivity.class);
        startActivity(signUpRedirect);
    }

    public void loadExternalSensorsPage(View v){
        Intent externalSensorRedirect = new Intent(this,ExternalSensors.class);
        startActivity(externalSensorRedirect);
    }

    private boolean validatePhoneNumber(String phone) {
        try{
            String value = "";
            for(int i = 0 ; i<10 ; i++) {
                value = phone.substring(i,i+1);
                Integer.parseInt(value);

            }return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
