/* See http://variableinc.com/terms-use-license for the full license governing this code. */
package com.example.jaskirat.hazewatch.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.R;
import com.example.anmol.hazewatch.JSONClasses.DatabaseEntryModel;
import com.example.jaskirat.hazewatch.MessageConstants;
import com.example.jaskirat.hazewatch.NodeApplication;
import com.variable.framework.dispatcher.DefaultNotifier;
import com.variable.framework.node.NodeDevice;
import com.variable.framework.node.OxaSensor;
import com.variable.framework.node.enums.NodeEnums;
import com.variable.framework.node.reading.SensorReading;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by jaskirat on 09/07/16.
 */
public class OxaFragment extends Fragment implements OxaSensor.OxaListener {
    public static final String TAG = OxaFragment.class.getName();

    private TextView oxaText, oxaPortBText;
    private TextView oxaBaseLineA, oxaBaseLineB;
    private List<OxaSensor> oxaSensors;
    static String sO2 = "1.0";
    static String cO = "1.0";
    static String COBaseline = "1.0";
    static String SO2Baseline = "1.0";
    static float tia_gain = (float) 1.0;
    static float responseRatio = (float) 1.0;
    static String serialNumber = "1.0";
    static String sO2_raw = "1.0";
    static String cO_Raw = "1.0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.oxa, null, false);
        oxaText = (TextView) root.findViewById(R.id.txtOxa);
        oxaBaseLineA = (TextView) root.findViewById(R.id.txtBaseLineA);
        
        oxaBaseLineB = (TextView) root.findViewById(R.id.txtBaseLineB);
        oxaPortBText = (TextView) root.findViewById(R.id.txtOxaB);
        

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        //Register Oxa Listener
        DefaultNotifier.instance().addOxaListener(this);

        NodeDevice node = NodeApplication.getActiveNode();
        if(node != null)
        {
            //Toast.makeText(this.getActivity(),"Node found",Toast.LENGTH_SHORT).show();
            oxaSensors = node.findAllSensors(NodeEnums.ModuleType.OXA);
            if(oxaSensors.size() > 1) {
                if (getView() != null) {
                    getView().findViewById(R.id.portBLayout).setVisibility(View.VISIBLE);
                }
            }
            for(OxaSensor sensor : oxaSensors){
                sensor.startSensor();
            }
        }
    }

    @Override
    public void onOxaBaselineUpdate(OxaSensor sensor, final SensorReading<Float> baseline_reading) {
        if(sensor.getConnectedPort() == NodeEnums.ModuleLocation.PortA) {
            mHandler.obtainMessage(MessageConstants.MESSAGE_OXA_BASELINE_A, baseline_reading.getValue()).sendToTarget();
        }else{
            mHandler.obtainMessage(MessageConstants.MESSAGE_OXA_BASELINE_B, baseline_reading.getValue()).sendToTarget();
        }
    }

    @Override
    public void onOxaUpdate(OxaSensor sensor, SensorReading<Float> reading) {
        float baseline = sensor.getBaseline();
        float value = 1.0f;
        //float responseRatio=-1, tia_gain = -1;
        /*Log.d("RAW value", String.valueOf(reading.getValue()));
        Log.d("Baseline", String.valueOf(baseline));*/
        switch(sensor.getSubtype()){
            case CO:
                cO_Raw = String.valueOf(reading.getValue());
                COBaseline = String.valueOf(baseline);
                tia_gain = 35000.0f;
                responseRatio = 39.0f;
                value = ((reading.getValue() - baseline) / 0.37736f / (tia_gain * responseRatio)) * 1E9f;
                cO = String.valueOf(value);
                //Log.d("Subtype", "CO");
                break;

            case SO2:
                tia_gain = 350000.0f;
                responseRatio = 265.0f;
                SO2Baseline = String.valueOf(baseline);
                //Log.d("Subtype", "SO2");
                sO2_raw = String.valueOf(reading.getValue());
                value = ((reading.getValue() - baseline) / 0.37736f / (tia_gain * responseRatio)) * 1E9f;
                sO2 = String.valueOf(value);
                break;
        }
        /*tia_gain = sensor.getTiaGainSetting().toConversionUnit();
        responseRatio = sensor.getResponse();
        serialNumber = sensor.getSerialNumber();
        Log.d("Sensor Name", sensor.getSubtype().toString());*/
        /*Log.d("tia_gain", String.valueOf(tia_gain));
        Log.d("responseRatio", String.valueOf(responseRatio));
        Log.d("serialNumber",serialNumber);
        Log.d("Baseline", String.valueOf(baseline));
        Log.d("RAW value", String.valueOf(reading.getValue()));*/
        //SensorReading reading2 = convertReading(sensor,reading);
        //value = ((reading.getValue() - baseline) / 0.37736f / (tia_gain * responseRatio)) * 1E9f;
        //Log.d("PPM Value before" , String.valueOf(value));
        //Float valueppm = (float) Math.max(0, value);
        //Log.d("PPM Value", String.valueOf(value));

        //coReadings.add(reading.getTimeStamp() + ", " + sensor.getSubtype().name()+ " ," + valueppm);

        Message m = mHandler.obtainMessage(MessageConstants.MESSAGE_OXA_READING, sensor.getConnectedPort().ordinal(), -1,  reading);
        m.getData().putFloat(MessageConstants.FLOAT_VALUE_KEY, value);
        m.sendToTarget();
    }

    private final Handler mHandler = new Handler(){
     private final DecimalFormat formatter = new DecimalFormat("0.00");

     @Override
     public void handleMessage(Message message)
     {
        float value = message.getData().getFloat(MessageConstants.FLOAT_VALUE_KEY);
        switch(message.what){
            case MessageConstants.MESSAGE_OXA_READING:
                if(message.arg1 == NodeEnums.ModuleLocation.PortA.ordinal()) {
                    oxaText.setText(formatter.format(value) + " PPM");
                    //Log.d("SO2 value", formatter.format(value));
                    //cO = formatter.format(value);
                }else{
                    oxaPortBText.setText(formatter.format(value) + " PPM");
                    //sO2 = formatter.format(value);
                    //Log.d("CO value", formatter.format(value));
                }
                break;
            case MessageConstants.MESSAGE_OXA_BASELINE_A:
                oxaBaseLineA.setText(message.obj.toString());
                //COBaseline = message.obj.toString();
                break;
            
            case MessageConstants.MESSAGE_OXA_BASELINE_B:
                oxaBaseLineB.setText(message.obj.toString());
                //SO2Baseline = message.obj.toString();
                break;
        }
      }
    };

    private SensorReading<Float> convertReading(OxaSensor sensor, SensorReading<Float> reading){
        if(sensor != null){
            float baseline = sensor.getBaseline();
            float tia_gain = sensor.getTiaGainSetting().toConversionUnit();
            float responseRatio = sensor.getResponse();

            double value = ((reading.getValue() - baseline) / 0.37736f / (tia_gain * responseRatio)) * 1E9f;
            return new SensorReading<Float>((float) Math.max(0, value), reading.getTimeStamp(), reading.getTimeStampSource());
        }else{

        }
        return reading;
    }

    static public DatabaseEntryModel combineValues(DatabaseEntryModel databaseEntry){
        Log.d("OxaFragment","Combine Values Called");
        databaseEntry.setCO(cO);
        databaseEntry.setSO2(sO2);
        databaseEntry.setCOBaseline(COBaseline);
        databaseEntry.setSO2Baseline(SO2Baseline);
        databaseEntry.setTia_gain(tia_gain);
        databaseEntry.setResponseRatio(responseRatio);
        databaseEntry.setSerialNumber(serialNumber);
        databaseEntry.setCO_Raw(cO_Raw);
        databaseEntry.setSO2_Raw(sO2_raw);
        return databaseEntry;
    }
}
