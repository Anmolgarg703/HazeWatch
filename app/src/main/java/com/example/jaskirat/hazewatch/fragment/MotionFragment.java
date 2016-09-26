/* See http://variableinc.com/terms-use-license for the full license governing this code. */
package com.example.jaskirat.hazewatch.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jaskirat.hazewatch.NodeApplication;
import com.example.R;
import com.example.jaskirat.hazewatch.graph.MotionGraph;
import com.variable.framework.dispatcher.DefaultNotifier;
import com.variable.framework.node.MotionSensor;
import com.variable.framework.node.NodeDevice;
import com.variable.framework.node.reading.MotionReading;

import org.achartengine.GraphicalView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jaskirat on 09/07/16.
 */
public class MotionFragment extends Fragment implements
                                                    MotionSensor.AccelerometerListener,
                                                    MotionSensor.MagnetometerListener,
                                                    MotionSensor.GyroscopeListener{
    public static  final String TAG = MotionFragment.class.getName();

    private  static final List<GraphicalView> mCharts = new CopyOnWriteArrayList<GraphicalView>();
    private MotionGraph[] motionGraphs = new MotionGraph[3];
    private MotionSensor motion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.motion, null, false);
        motionGraphs[0]  = new MotionGraph("Acceleration (g)", "X Axis Title",   " Y Axis Title", .5f, false);
        motionGraphs[1]  = new MotionGraph("Gyroscope (dps)", " X Axis Title  " ,  " Y Axis Title", .5f, false);
        motionGraphs[2]  = new MotionGraph("Magnetic (G)", " X Axis Title   "  , " Y Axis Title", .5f, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        GraphicalView mAccelGraphView = motionGraphs[0].getView(getActivity());
        ((LinearLayout) getView().findViewById(R.id.accelChart)).addView(mAccelGraphView, 0);
        mAccelGraphView.refreshDrawableState();
        mCharts.add(mAccelGraphView);

        GraphicalView mGyroGraphView  = motionGraphs[1].getView(getActivity());
        ((LinearLayout) getView().findViewById(R.id.gyroChart)).addView(mGyroGraphView, 0);
        mCharts.add(mGyroGraphView);

        GraphicalView mMagGraphView   = motionGraphs[2].getView(getActivity());
        ((LinearLayout) getView().findViewById(R.id.magChart)).addView(mMagGraphView, 0);
        mCharts.add(mMagGraphView);

        updateGraphs();

        //Register for Events
        DefaultNotifier.instance().addAccelerometerListener(this);
        DefaultNotifier.instance().addGyroscopeListener(this);
        DefaultNotifier.instance().addMagnetometerListener(this);


        //Start the Motion Stream
        NodeDevice node = NodeApplication.getActiveNode();
        if(node != null)
        {
            motion = node.getMotionSensor();
            //For this demo we cut back the sampling rate to 50 ms period.
            motion.setStreamMode(true, true, true, 5, 0, true);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(motion != null) {
            //Stop the Motion Stream
            motion.stopSensor();
        }

        ((LinearLayout) getView().findViewById(R.id.accelChart)).removeViewAt(0);
        ((LinearLayout) getView().findViewById(R.id.gyroChart)).removeViewAt(0);
        ((LinearLayout) getView().findViewById(R.id.magChart)).removeViewAt(0);

        mCharts.clear();

        //Unregister for Events
        DefaultNotifier.instance().removeAccelerometerListener(this);
        DefaultNotifier.instance().removeMagnetometerListener(this);
        DefaultNotifier.instance().removeGyroscopeListener(this);
    }

    private Handler mHandler = new Handler();

    private void updateGraphs() {
        for (int i = 0; i < mCharts.size(); i++) {
            GraphicalView view = mCharts.get(i);
            view.repaint();
        }

        if (isAdded()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateGraphs();
                }
            }, 1000 / 30);
        }
    }

    @Override
    public void onAccelerometerUpdate(MotionSensor sensor, MotionReading reading) {
        motionGraphs[0].addPoint(reading);
    }


    @Override
    public void onMagnetometerUpdate(MotionSensor sensor, MotionReading reading) {
        motionGraphs[2].addPoint(reading);
    }

    @Override
    public void onGyroscopeUpdate(MotionSensor sensor, MotionReading reading) {
        motionGraphs[1].addPoint(reading);
    }
}
