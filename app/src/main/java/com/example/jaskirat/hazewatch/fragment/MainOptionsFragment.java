/* See http://variableinc.com/terms-use-license for the full license governing this code. */
package com.example.jaskirat.hazewatch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.R;

/**
 * Created by jaskirat on 09/07/16.
 */
public class MainOptionsFragment  extends Fragment {
    public static final String TAG = MainOptionsFragment.class.getName();

    private View.OnClickListener onClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.main_options, null, false);

        ButtonClickHandler clickHandler = new ButtonClickHandler();
        root.findViewById(R.id.btnMotion).setOnClickListener(clickHandler);
        root.findViewById(R.id.btnPairedNodes).setOnClickListener(clickHandler);
        root.findViewById(R.id.btnClima).setOnClickListener(clickHandler);

        root.findViewById(R.id.btnOxa).setOnClickListener(clickHandler);

        root.findViewById(R.id.btnRefreshSensors).setOnClickListener(clickHandler);

        root.findViewById(R.id.reading).setOnClickListener(clickHandler);



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
}
