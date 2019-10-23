package com.lee.mystudy.view

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.lee.mystudy.util.CameraHelper
import com.lee.mystudy.util.LogUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyGlSurfaceViewRGB : GLSurfaceView, GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {


    private var mContext: Context? = null

    private var mTextrueId = -1
    private var mSurfaceTexture : SurfaceTexture?=null
    private var mDirectDrawer : DirectDrawer ?=null

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {

        mContext = context
        setEGLContextClientVersion(2)
        setRenderer(this)
        renderMode = RENDERMODE_WHEN_DIRTY

    }

    override fun onDrawFrame(gl: GL10) {
        LogUtil.d("onDrawFrame")
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or  GLES20.GL_DEPTH_BUFFER_BIT)
        mSurfaceTexture!!.updateTexImage()
        var mtx = FloatArray(16)
        mSurfaceTexture!!.getTransformMatrix(mtx)
        mDirectDrawer!!.draw(mtx)

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {


    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        //创建
        mTextrueId = createTextureID()
        mSurfaceTexture = SurfaceTexture(mTextrueId)
        mSurfaceTexture!!.setOnFrameAvailableListener(this)
        mDirectDrawer = DirectDrawer(mTextrueId)
        CameraHelper.openCamera(0,mSurfaceTexture!!)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {

            requestRender()

    }

     private fun createTextureID(): Int {
        var texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
        return texture[0]
    }

}