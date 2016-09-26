package com.example.anmol.hazewatch;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.R;
import com.example.anmol.hazewatch.Utility.GPSTracker;

public class Readings extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private TextView accelerometer;
    private TextView pressure;
    private TextView magnetometer;
    private TextView temperature;
    private TextView gps;

    GPSTracker gpsTracker;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        accelerometer = (TextView) findViewById(R.id.accelerometer);
        magnetometer = (TextView) findViewById(R.id.magnetometer);
        pressure = (TextView) findViewById(R.id.pressure);
        temperature = (TextView) findViewById(R.id.temperature);
        gps = (TextView) findViewById(R.id.gps);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGPSLocation();
                handler.postDelayed(this, 3000);
            }
        }, 2000);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            accelerometer.setText("No Accelerometer Sensors");
            //Toast.makeText(this, "No Accelerometer Sensors", Toast.LENGTH_SHORT).show();
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            Sensor pressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            mSensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            pressure.setText("No Pressure Sensors");
            //Toast.makeText(this, "No Pressure Sensors", Toast.LENGTH_SHORT).show();
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            Sensor magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            magnetometer.setText("No Magnetic Sensors");
            //Toast.makeText(this, "No Magnetic Sensors", Toast.LENGTH_SHORT).show();
        }
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            Sensor temperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            mSensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            temperature.setText("No Temperature Sensors");
            //Toast.makeText(this, "No Temperature Sensors", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGPSLocation();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        float ax, ay, az;
        float mx, my, mz;
        float pressureReading, temperatureReading;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            accelerometer.setText("AX = " + ax + "\nAY = " + ay + "\nAZ = " + az);
        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressureReading = event.values[0];
            pressure.setText("Pressure = " + pressureReading);

        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mx = event.values[0];
            my = event.values[1];
            mz = event.values[2];
            magnetometer.setText("MX = " + mx + "\nMY = " + my + "\nMZ = " + mz);
        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperatureReading = event.values[0];
            temperature.setText("Temperature = " + temperatureReading);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void getGPSLocation() {
        gpsTracker = new GPSTracker(Readings.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            gps.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void viewOnMap(View v){
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

}
