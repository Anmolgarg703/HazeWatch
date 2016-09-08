package com.example.anmol.hazewatch.JSONClasses;

/**
 * Created by Anmol on 9/6/2016.
 */
public class UserSignUpModel {
    String name;
    String email;
    String phone;
    String password;

    public UserSignUpModel(String name, String email, String phone, String password){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}
