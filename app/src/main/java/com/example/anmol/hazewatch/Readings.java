package com.example.anmol.hazewatch;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;

public class Readings extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    private TextView accelerometer;
    private TextView pressure;
    private TextView magnetometer;
    private TextView temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        accelerometer = (TextView) findViewById(R.id.accelerometer);
        magnetometer = (TextView) findViewById(R.id.magnetometer);
        pressure = (TextView) findViewById(R.id.pressure);
        temperature = (TextView) findViewById(R.id.temperature);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            accelerometer.setText("No Accelerometer Sensors");
            Toast.makeText(this, "No Accelerometer Sensors", Toast.LENGTH_SHORT).show();
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            Sensor pressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            mSensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            pressure.setText("No Pressure Sensors");
            Toast.makeText(this, "No Pressure Sensors", Toast.LENGTH_SHORT).show();
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            Sensor magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            magnetometer.setText("No Magnetic Sensors");
            Toast.makeText(this, "No Magnetic Sensors", Toast.LENGTH_SHORT).show();
        }
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) !=null){
            Sensor temperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            mSensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            temperature.setText("No Temperature Sensors");
            Toast.makeText(this, "No Temperature Sensors", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        float ax,ay,az;
        float mx,my,mz;
        float px,py,pz;
        float tx,ty,tz;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            accelerometer.setText("AX = " + ax + "\nAY = " + ay + "\nAZ = " + az);
        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            px = event.values[0];
            py = event.values[1];
            pz = event.values[2];
            pressure.setText("PX = " + px + "\nPY = " + py + "\nPZ = " + pz);

        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mx = event.values[0];
            my = event.values[1];
            mz = event.values[2];
            magnetometer.setText("MX = " + mx + "\nMY = " + my + "\nMZ = " + mz);
        }
        else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            tx = event.values[0];
            ty = event.values[1];
            tz = event.values[2];
            temperature.setText("TX = " + tx + "\nTY = " + ty + "\nTZ = " + tz);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
