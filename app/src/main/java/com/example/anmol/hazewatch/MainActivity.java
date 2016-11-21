package com.example.anmol.hazewatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;
import com.example.anmol.hazewatch.Communication.Communication;
import com.example.anmol.hazewatch.Communication.DBConnect;
import com.example.anmol.hazewatch.Communication.Request;
import com.example.anmol.hazewatch.JSONClasses.UserLoginModel;
import com.example.jaskirat.hazewatch.SensorActivity;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements Communication {

    private static final String LOGIN = "isLogin";
    private static final String PREFERENCE_NAME = "LoginActivity";
    private EditText mPhone;
    private EditText mPassword;
    private SharedPreferences mPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);

        boolean isLogin = mPrefs.getBoolean(LOGIN,false);
        if(!isLogin){
            Intent sensorActivity = new Intent(this, SensorActivity.class);
            startActivity(sensorActivity);
        }

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

    public void developers(View v){
        Intent developer = new Intent(this,DeveloperActivity.class);
        startActivity(developer);
    }

    public void onCompletion(String response) {
        Gson gson = new Gson();
        UserLoginModel userLogin = gson.fromJson(response, UserLoginModel.class);
        if(userLogin.getLogin().equals("1")){
            mPrefs.edit().putBoolean(LOGIN, true).commit();
            //Toast.makeText(this,"Phone Number" + userLogin.getPhone(),Toast.LENGTH_SHORT).show();
            mPrefs.edit().putString("Name", userLogin.getName()).commit();
            mPrefs.edit().putString("Phone", userLogin.getPhone()).commit();
            Intent mainOptionsFragment = new Intent(this, SensorActivity.class);
            mainOptionsFragment.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainOptionsFragment);
            finish();
        }
        else{
            Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
