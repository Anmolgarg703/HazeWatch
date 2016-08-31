package com.example.anmol.hazewatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mName = (EditText)findViewById(R.id.name);
        mEmail = (EditText)findViewById(R.id.email);
        mPhone = (EditText)findViewById(R.id.phone);
        mPassword = (EditText)findViewById(R.id.password);
        mConfirmPassword = (EditText)findViewById(R.id.password2);
    }

    public void processForm(View v){
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        
        if(validateName(name)){
            if(validateEmail(email)){
                if (validatePhoneNumber(phone)){
                    if(validatePassword(password)){
                        if(passwordsMatch(password,confirmPassword)){
                            //Sign Up
                            Toast.makeText(this,"Successfully Signed Up", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this,"Passwords do not match", Toast.LENGTH_SHORT).show();
                            mConfirmPassword.setText("");
                            mConfirmPassword.requestFocus();
                        }
                    }
                    else{
                        Toast.makeText(this,"Password must be atleast 8 characters long", Toast.LENGTH_SHORT).show();
                        mPassword.requestFocus();
                    }
                }
                else{
                    Toast.makeText(this,"Please enter a valid Phone Number", Toast.LENGTH_SHORT).show();
                    mPhone.requestFocus();
                }
            }else {
                Toast.makeText(this,"Please enter a valid Email Id", Toast.LENGTH_SHORT).show();
                mEmail.requestFocus();
            }
        }else{
            Toast.makeText(this,"Please enter a valid name", Toast.LENGTH_SHORT).show();
            mName.requestFocus();
        }
        //Toast.makeText(this.getApplicationContext(),phone + password, Toast.LENGTH_LONG).show();
    }

    private boolean passwordsMatch(String password, String confirmPassword) {
        if(password.equals(confirmPassword))
            return true;
        return false;
    }

    private boolean validatePassword(String password) {
        if(password.length()>=8)
            return true;
        return false;
    }

    private boolean validateEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        //public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean validateName(String name) {
        for (int i = 0; i < name.length(); i++)
            if (((int) name.charAt(i) >= 65 && (int) name.charAt(i) <= 90) || ((int) name.charAt(i) >= 97 && (int) name.charAt(i) <= 122) || (name.charAt(i) == ' ' && i>=3)){
                return true;
            }
        return false;
    }

    public boolean validatePhoneNumber(String phone) {
        try{
            String value = "";
            for(int i = 0 ; i<10 ; i++) {
                value = phone.substring(i,i+1);
                Integer.parseInt(value);
            }
            if(phone.length() == 10 && (phone.charAt(0) == '7' || phone.charAt(0) == '8' || phone.charAt(0) == '9'))
                return true;
            else
                return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
