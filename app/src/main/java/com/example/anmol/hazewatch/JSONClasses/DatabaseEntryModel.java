package com.example.anmol.hazewatch.JSONClasses;

/**
 * Created by Anmol on 10/15/2016.
 */
public class DatabaseEntryModel {
    //Phone internal sensors
    String username;
    float accelerometerX;
    float accelerometerY;
    float accelerometerZ;
    float magnetometerX;
    float magnetometerY;
    float magnetometerZ;
    String gyrometer;
    float pressure;
    float temperature;
    String humidity;
    String sound;
    String timestamp;
    float gpsSpeed;
    String activity;


    //GPS Location
    double latitude;
    double longitude;
    String speed;

    //Node Sensors
    String SO2;
    String CO;
    String COBaseline;
    String SO2Baseline;

    public void setAccelerometerReadings(float ax, float ay, float az){
        this.accelerometerX = ax;
        this.accelerometerY = ay;
        this.accelerometerZ = az;
    }

    public void setMagnetometerReadings(float mx, float my, float mz){
        this.magnetometerX = mx;
        this.magnetometerY = my;
        this.magnetometerZ = mz;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getAccelerometerX() {
        return accelerometerX;
    }

    public void setAccelerometerX(float accelerometerX) {
        this.accelerometerX = accelerometerX;
    }

    public float getAccelerometerY() {
        return accelerometerY;
    }

    public void setAccelerometerY(float accelerometerY) {
        this.accelerometerY = accelerometerY;
    }

    public float getAccelerometerZ() {
        return accelerometerZ;
    }

    public void setAccelerometerZ(float accelerometerZ) {
        this.accelerometerZ = accelerometerZ;
    }

    public float getMagnetometerX() {
        return magnetometerX;
    }

    public void setMagnetometerX(float magnetometerX) {
        this.magnetometerX = magnetometerX;
    }

    public float getMagnetometerY() {
        return magnetometerY;
    }

    public void setMagnetometerY(float magnetometerY) {
        this.magnetometerY = magnetometerY;
    }

    public float getMagnetometerZ() {
        return magnetometerZ;
    }

    public void setMagnetometerZ(float magnetometerZ) {
        this.magnetometerZ = magnetometerZ;
    }

    public String getGyrometer() {
        return gyrometer;
    }

    public void setGyrometer(String gyrometer) {
        this.gyrometer = gyrometer;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSO2() {
        return SO2;
    }

    public void setSO2(String SO2) {
        this.SO2 = SO2;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public float getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(float gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    public String getCOBaseline() {
        return COBaseline;
    }

    public void setCOBaseline(String COBaseline) {
        this.COBaseline = COBaseline;
    }

    public String getSO2Baseline() {
        return SO2Baseline;
    }

    public void setSO2Baseline(String SO2Baseline) {
        this.SO2Baseline = SO2Baseline;
    }
}
