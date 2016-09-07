/* See http://variableinc.com/terms-use-license for the full license governing this code. */
package com.example.jaskirat.hazewatch;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.variable.framework.chroma.service.ChromaServiceErrorListener;
import com.variable.framework.dispatcher.DefaultNotifier;
import com.variable.framework.node.NodeDevice;

/**
 * Created by jaskirat on 07-09-2016.
 */
public class NodeApplication extends Application {

    public static NodeDevice mActiveNode;
    private static Context context;


    public static void setActiveNode(NodeDevice node){ mActiveNode = node; }

    public static NodeDevice getActiveNode(){  return mActiveNode; }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        String libraryKey = null; // "....Insert Your Library Key, If Present.....";
        String devKey = null; //".....Insert Your API Access Key, If Present.....";
        if(devKey == null && libraryKey == null){
            //This is how to initialize the framework for everything other than chroma usage.
            com.variable.application.NodeApplication.initialize(this);
        }else {
            //For using chroma functionality. Please contact variable for more information on obtaining a developer key and libraries key.
            com.variable.application.NodeApplication.initialize(this, devKey, libraryKey);
        }

        DefaultNotifier.instance().addChromaErrorListener(new ChromaServiceErrorListener() {
            @Override
            public void onError(int i, Bundle bundle) {
                if (i == Codes.ACCOUNT_DEACTIVATED) {
                    Toast.makeText(NodeApplication.context, "The account has been deactivated that is using Chroma", Toast.LENGTH_SHORT).show();
                } else if (i == Codes.WEB_FAILURE) {
                    Toast.makeText(NodeApplication.context, "Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
