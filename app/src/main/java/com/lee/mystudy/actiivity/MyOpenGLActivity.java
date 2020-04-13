package com.lee.mystudy.actiivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyOpenGLActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo deviceConfigurationInfo = activityManager.getDeviceConfigurationInfo();
        int glEsVersion = deviceConfigurationInfo.reqGlEsVersion;
        boolean support = glEsVersion >= 0x20000;
    //    ToastUtils.showShort("support es :" + support);
        final MyOpenGLRender myOpenGLRender = new MyOpenGLRender(this);
        if(support){

            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer( myOpenGLRender );
            renderSet = true;
            glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if(motionEvent != null){

                        final float normalizedX =
                                (motionEvent.getX() / (float) view.getWidth()) * 2 - 1;

                        final float normalizedY =
                                -((motionEvent.getY() / (float) view.getHeight()) * 2 -1);

                        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                            glSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    myOpenGLRender.handleTouchPress(normalizedX, normalizedY);
                                }
                            });

                        }else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){

                            glSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {

                                    myOpenGLRender.handleTouchDrag(normalizedX,normalizedY);


                                }
                            });

                        }

                        return true;

                    }else{

                        return false;

                    }

                }
            });

            setContentView(glSurfaceView);
        }else {

      //      ToastUtils.showShort("not support EL2");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(renderSet){

            glSurfaceView.onPause();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(renderSet){

            glSurfaceView.onResume();

        }

    }
}
