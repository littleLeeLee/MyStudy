package com.lee.mystudy.util

import android.graphics.SurfaceTexture
import android.hardware.Camera

object CameraHelper {

    private var mCamera : Camera?=null

    fun openCamera(cameraId : Int,textrue:SurfaceTexture){

        mCamera = Camera.open(cameraId)
        mCamera!!.setPreviewTexture(textrue)
        mCamera!!.setDisplayOrientation(90)
        mCamera!!.startPreview()
    }

    fun stopCamera(){
        mCamera?.stopPreview()
        mCamera?.release()
    }

}