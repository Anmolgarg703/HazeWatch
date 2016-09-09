/* See http://variableinc.com/terms-use-license for the full license governing this code. */
package com.example.jaskirat.hazewatch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.R;
import com.example.anmol.hazewatch.MainActivity;
import com.example.anmol.hazewatch.Readings;

/**
 * Created by jaskirat on 09/07/16.
 */
public class MainOptionsFragment  extends Fragment {
    public static final String TAG = MainOptionsFragment.class.getName();

    private View.OnClickListener onClickListener;

    private Button readings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.main_options, null, false);

        ButtonClickHandler clickHandler = new ButtonClickHandler();

        readings = (Button) root.findViewById(R.id.reading);
        readings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent internalSensorReadings = new Intent(getActivity(), Readings.class);
                startActivity(internalSensorReadings);
            }
        });
        root.findViewById(R.id.btnMotion).setOnClickListener(clickHandler);
        root.findViewById(R.id.btnPairedNodes).setOnClickListener(clickHandler);
        root.findViewById(R.id.btnClima).setOnClickListener(clickHandler);

        root.findViewById(R.id.btnOxa).setOnClickListener(clickHandler);

        root.findViewById(R.id.btnRefreshSensors).setOnClickListener(clickHandler);

        //root.findViewById(R.id.reading).setOnClickListener(clickHandler);



        return root;
    }


    public MainOptionsFragment setOnClickListener(View.OnClickListener listener) { onClickListener = listener; return this; }




    public class ButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()){

                default:
                    if(onClickListener != null){
                        onClickListener.onClick(view);
                    }
                    break;
            }
        }
    }

  //  public void readings(View v){
  //      Intent internalSensorReadings = new Intent(this.getActivity(), Readings.class);
   //     startActivity(internalSensorReadings);
  //  }
}
