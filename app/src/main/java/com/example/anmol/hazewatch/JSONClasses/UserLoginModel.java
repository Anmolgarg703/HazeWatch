package com.example.anmol.hazewatch.JSONClasses;

/**
 * Created by Anmol on 9/5/2016.
 */
public class UserLoginModel {
    String phone;
    String password;

    public UserLoginModel(String phone, String password){
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
