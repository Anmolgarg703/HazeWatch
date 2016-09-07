package com.example.jaskirat.hazewatch.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.variable.framework.node.AndroidNodeDevice;
import com.variable.framework.node.NodeDevice;


/**
 * Created by coreymann on 3/3/15.
 */
public class AppPreferences {
    
    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    public AppPreferences(Context mContext) {
        this.mContext = mContext;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    
    

    public void setBluetoothAddress(@NonNull String address, int deviceType){
        mSharedPreferences.edit()
                .putString(BluetoothDevice.EXTRA_DEVICE, address)
                .putInt("DEVICE_TYPE", deviceType)
                .apply();
    }
    
    public void clearBluetoothAddress(){
        mSharedPreferences.edit()
                .putString(BluetoothDevice.EXTRA_DEVICE, "")
                .apply();
    }

    /**
     *  
     * @return null, if no known address is present, otherwise the instance of NodeDevice.
     */
    @Nullable
    public NodeDevice getNODE(Context ctx){
        //Get the Bluetooth Device
        BluetoothDevice device = getBluetoothDevice();
        
        //Return null if no device is present
        if(device == null){
            return null;
        }
        
        //Lookup in the manager if the address has a NodeDevice instance.
        NodeDevice node = AndroidNodeDevice.getManager().findFromAddress(device.getAddress());
        if(node == null){
            //Request the factory to construct a new NODE device or retrieve an existing one from the manager.
            node = NodeDevice.getFactory().create(ctx, device.getAddress(), getLastBluetoothDeviceType());
        }
        return node;
    }
    
    @Nullable
    public BluetoothDevice getBluetoothDevice(){
        String address = getBluetoothAddress();
        if(address.equals("")){ return null; }
        else { return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address); }
    }
    
    @NonNull
    public String getBluetoothAddress(){
        return mSharedPreferences.getString(BluetoothDevice.EXTRA_DEVICE, "");  
    }

    public int getLastBluetoothDeviceType() {
        return mSharedPreferences.getInt("DEVICE_TYPE", 3 /**Assume DEVICE_TYPE_DUAL**/);
    }
}
