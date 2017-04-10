package com.example.anmol.hazewatch.JSONClasses;

/**
 * Created by Anmol on 4/8/2017.
 */
public class GPS {
    private double latitude;
    private double longitude;
    private float speed;

    public GPS(){
        this.latitude = 1.0;
        this.longitude = 1.0;
        this.speed = 0;
    }
    public GPS(double latitude, double longitude, float speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
