package com.example.anmol.hazewatch.JSONClasses;

/**
 * Created by Anmol on 9/5/2016.
 */
public class UserLoginModel {
    String phone;
    String password;
    String Login;
    String Name;

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        this.Login = login;
    }

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
