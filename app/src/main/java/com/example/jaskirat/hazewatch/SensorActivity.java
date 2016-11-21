package com.example.jaskirat.hazewatch;

/**
 * Created by jaskirat on 07-09-2016.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.example.R;
import com.example.anmol.hazewatch.MainActivity;
import com.example.anmol.hazewatch.Readings;
import com.example.anmol.hazewatch.SyncDatabase;
import com.example.jaskirat.hazewatch.fragment.MainOptionsFragment;
import com.example.jaskirat.hazewatch.fragment.MotionFragment;
import com.example.jaskirat.hazewatch.fragment.NodeConnectionDialog;
import com.example.jaskirat.hazewatch.fragment.OxaFragment;
import com.variable.framework.android.bluetooth.BluetoothService;
import com.variable.framework.dispatcher.DefaultNotifier;
import com.variable.framework.node.BaseSensor;
import com.variable.framework.node.NodeDevice;
import com.variable.framework.node.enums.NodeEnums;



public class SensorActivity extends FragmentActivity implements View.OnClickListener, NodeDevice.SensorDetector{

    private static final String LOGIN = "isLogin";
    private static final String PREFERENCE_NAME = "LoginActivity";
    private static final String TAG = SensorActivity.class.getName();

   // private boolean isPulsing = false;
    private ProgressDialog mProgressDialog;
    private NodeConnectionDialog mConnectionFragment;
    private SharedPreferences mPrefs;

    //region Lifecycle Events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        mPrefs = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
    }


    @Override
    public void onResume(){
        super.onResume();

        ensureBluetoothIsOn();

        //Start Options Fragment
        Fragment frag = new MainOptionsFragment().setOnClickListener(this);
        animateToFragment(frag, MainOptionsFragment.TAG);

        //Registering for Events.
        DefaultNotifier.instance().addSensorDetectorListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();

        //NodeDevice node = NodeApplication.getActiveNode();
        //if(isNodeConnected(node)){
         //   node.disconnect(); //Clean up after ourselves.
        //}

        //Registering for Events
        //DefaultNotifier.instance().removeSensorDetectorListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){ ensureBluetoothIsOn();  }
    }

    //endregion

    public void spinner(View v)
    {
        Intent intent= new Intent(this,com.example.jaskirat.hazewatch.AndroidSpinnerActivity.class);
        startActivity(intent);

    }
    @Override
    public void onClick(View view) {
        NodeDevice node = NodeApplication.getActiveNode();
        if(view.getId() == R.id.btnPairedNodes){
            if(mConnectionFragment == null) {
                mConnectionFragment = NodeConnectionDialog.newInstance();
            }
            animateToFragment(mConnectionFragment, NodeConnectionDialog.FRAGMENT_TAG);
            return;
        }

        else if(view.getId()==R.id.syncDatabase){
            Intent syncDatabase = new Intent(this, SyncDatabase.class);
            startActivity(syncDatabase);
        }

        else if(view.getId() == R.id.reading){
            Intent internalSensorReadings = new Intent(this, Readings.class);
            startActivity(internalSensorReadings);
        }

        else if(view.getId() == R.id.logout){
            mPrefs.edit().putBoolean(LOGIN,false).commit();
            Intent logout = new Intent(this, MainActivity.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logout);
            finish();
        }

        else if(!isNodeConnected(node))
        {
            Toast.makeText(this, "No Connection Available", Toast.LENGTH_SHORT ).show();
            return;
        }
        switch(view.getId()){

            case R.id.btnMotion:
                animateToFragment(new MotionFragment(), MotionFragment.TAG);
                break;

            case R.id.btnOxa:
                if(checkForSensor(node, NodeEnums.ModuleType.OXA, true))
                    animateToFragment(new OxaFragment(), OxaFragment.TAG);
                break;

            //NODE must be polled to maintain an up to date array of sensors.
            case R.id.btnRefreshSensors:
                new AlertDialog.Builder(this)
                        .setTitle("Disconnect all sensors")
                        .setMessage("Are you sure you want to disconnect all sensors?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                BluetoothService.killConnections();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //node.requestSensorUpdate();
                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == NodeConnectionDialog.REQUEST_LOCATION_PERMISSION){
            mConnectionFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    //region Private Methods

    /**
     * Invokes a new intent to request to start the bluetooth, if not already on.
     */
    private boolean ensureBluetoothIsOn(){
        if(!BluetoothAdapter.getDefaultAdapter().isEnabled()){
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            btIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(btIntent, 200);
            return false;
        }

        return true;
    }


    /**
     * Checks if a fragment with the specified tag exists already in the Fragment Manager. If present, then removes fragment.
     *
     * Animates out to the specified fragment.
     *
     *
     * @param frag
     * @param tag
     */
    public void animateToFragment(final Fragment frag, final String tag){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment existingFrag = getSupportFragmentManager().findFragmentByTag(tag);
        if(existingFrag != null){
            getSupportFragmentManager().beginTransaction().remove(existingFrag).commitAllowingStateLoss();
        }

        ft.replace(R.id.center_fragment_container, frag, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * Checks for a specific sensor on a node.
     * @param node - the node
     * @param type - the module type to check for on the node parameter.
     * @param displayIfNotFound - allows toasting a message if module is not found on node.
     * @return true, if the node contains the module
     */
    private boolean checkForSensor(NodeDevice node, NodeEnums.ModuleType type, boolean displayIfNotFound){
        BaseSensor sensor = node.findSensor(type);
        if(sensor == null && displayIfNotFound){
            Toast.makeText(SensorActivity.this, type.toString() + " not found on " + node.getName(), Toast.LENGTH_SHORT).show();
        }

        return sensor != null;
    }

    /**
     * Determines if the node is connected. Null is permitted.
     * @param node
     * @return
     */
    private boolean isNodeConnected(NodeDevice node) { return node != null && node.isConnected(); }


    //Convience Method
    private final void dismissProgressDialog(){
        if(mProgressDialog != null){
            try { mProgressDialog.dismiss(); } catch(Exception e){ e.printStackTrace(); }
        }
    }

    //Convience Method
    private final void updateProgressDialog(String title, String message){
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Restart and Kill all connections....
                    BluetoothService.killConnections();
                }
            });
        }

        if(title != null) { mProgressDialog.setTitle(title);    }
        if(message != null) {   mProgressDialog.setMessage(message);    }

        if(!mProgressDialog.isShowing()){
            try { mProgressDialog.show(); } catch (Exception e){e.printStackTrace(); }
        }
    }

    //endregion

    //region Sensor Detector Callbacks

    @Override
    public void onSensorConnected(NodeDevice nodeDevice, final BaseSensor baseSensor) {
        //1. called when a new connection occurs
        //2. called when a refreshSensors has been called and a sensor has been added
    }

    @Override
    public void onSensorDisconnected(NodeDevice nodeDevice, final BaseSensor baseSensor) {
        //1. called when a new connection occurs
        //2. called when a refreshSensors has been called and a sensor has been removed
    }

    //endregion

}
