package com.lee.mystudy.actiivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lee.mystudy.R
import com.lee.mystudy.util.CameraHelper

class CameraActivityGLRgb :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gl_rgb)

    }

    companion object{

        fun start(context : Context){
            context.startActivity(Intent(context,CameraActivityGLRgb::class.java))
        }

    }


    override fun onPause() {
        super.onPause()
        CameraHelper.stopCamera()
    }
}