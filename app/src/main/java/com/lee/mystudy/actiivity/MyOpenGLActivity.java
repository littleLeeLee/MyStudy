package com.lee.mystudy.actiivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lee.mystudy.util.LogUtil;

public class MyOpenGLActivity extends AppCompatActivity {

    private GLSurfaceView myGlSurceface;
    private boolean renderSet = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myGlSurceface = new GLSurfaceView(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo deviceConfigurationInfo = activityManager.getDeviceConfigurationInfo();
        int glEsVersion = deviceConfigurationInfo.reqGlEsVersion;
        boolean support = glEsVersion >= 0x20000;
    //    ToastUtils.showShort("support es :" + support);

        if(support){

            myGlSurceface.setEGLContextClientVersion(2);
            myGlSurceface.setRenderer(new MyOpenGLRender(this));
            renderSet = true;
            setContentView(myGlSurceface);
        }else {

      //      ToastUtils.showShort("not support EL2");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(renderSet){

            myGlSurceface.onPause();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(renderSet){

            myGlSurceface.onResume();

        }

    }
}
