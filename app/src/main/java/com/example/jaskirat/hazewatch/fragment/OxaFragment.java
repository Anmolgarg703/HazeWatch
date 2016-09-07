/* See http://variableinc.com/terms-use-license for the full license governing this code. */
package com.example.jaskirat.hazewatch.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jaskirat.hazewatch.MessageConstants;
import com.example.jaskirat.hazewatch.NodeApplication;
import com.example.R;
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

        DefaultNotifier.instance().removeOxaListener(this);

        for(OxaSensor oxa : oxaSensors) {
            //Turn off oxa
            oxa.stopSensor();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //Register Oxa Listener
        DefaultNotifier.instance().addOxaListener(this);

        NodeDevice node = NodeApplication.getActiveNode();
        if(node != null)
        {
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
        Message m = mHandler.obtainMessage(MessageConstants.MESSAGE_OXA_READING, sensor.getConnectedPort().ordinal(), -1,  reading);
        m.getData().putFloat(MessageConstants.FLOAT_VALUE_KEY, reading.getValue());
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
                    oxaText.setText(formatter.format(value) + " RAW");
                }else{
                    oxaPortBText.setText(formatter.format(value) + " RAW");
                }
                break;
            case MessageConstants.MESSAGE_OXA_BASELINE_A:
                oxaBaseLineA.setText(message.obj.toString());
                break;
            
            case MessageConstants.MESSAGE_OXA_BASELINE_B:
                oxaBaseLineB.setText(message.obj.toString());
                break;
        }
      }
    };
}
