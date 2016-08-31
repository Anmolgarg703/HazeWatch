package com.example.anmol.hazewatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mPhone;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhone = (EditText)findViewById(R.id.phone);
        mPassword = (EditText)findViewById(R.id.password);
    }

    public void processForm(View v){
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();

        if (validatePhoneNumber(phone) && phone.length() == 10 && (phone.charAt(0) == '7' || phone.charAt(0) == '8' || phone.charAt(0) == '9')) {
            if(password.length()>=8){
                //Login Successful
                Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show();
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

    public void signUp(View v){
        Intent signUpRedirect = new Intent(this,SignUp.class);
        startActivity(signUpRedirect);
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
