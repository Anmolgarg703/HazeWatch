package com.example.anmol.hazewatch.Communication;

/**
 * Created by Anmol on 9/5/2016.
 */
public class Request {
    String method;
    String request;

    public Request(String method, String request){
        this.method = method;
        this.request = request;
    }

    public Request(String method){
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
