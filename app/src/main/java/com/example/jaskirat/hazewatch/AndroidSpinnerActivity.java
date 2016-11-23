package com.example.jaskirat.hazewatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.example.R;

import java.util.ArrayList;
import java.util.List;
public class AndroidSpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedPreferences mPrefs;

    private static final String PREFERENCE_NAME = "LoginActivity";
    private static final String ACTIVITY= "Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        mPrefs = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        // AndroidSpinnerActivity element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // AndroidSpinnerActivity click listener
        spinner.setOnItemSelectedListener(this);

        // AndroidSpinnerActivity Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Inactivity");
        categories.add("Running");
        categories.add("Cycling");
        categories.add("Jogging");
        categories.add("Driving (in car)");
        categories.add("Bike");
        categories.add("Walking");
        categories.add("Lawn and Gardening");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        String storedValue= mPrefs.getString(ACTIVITY, "Driving");
        Toast.makeText(getBaseContext(), "earlier Selected: " + storedValue, Toast.LENGTH_SHORT).show();
        spinner.setSelection(getIndex(spinner, storedValue));
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void submitback (View v)
    {
        finish();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        mPrefs.edit().putString(ACTIVITY, item).commit();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
