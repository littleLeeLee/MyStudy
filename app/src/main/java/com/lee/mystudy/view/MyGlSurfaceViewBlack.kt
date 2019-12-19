package com.lee.mystudy.view

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import com.lee.mystudy.util.CameraHelper
import com.lee.mystudy.util.LogUtil
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyGlSurfaceViewBlack : GLSurfaceView, GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {


    private var mContext: Context? = null

    private var mTextrueId = -1
    private var mSurfaceTexture : SurfaceTexture?=null
    private var mDirectDrawer : DirectDrawer ?=null
    private val transformMatrix = FloatArray(16)
    private var aPositionLocation = -1
    private var aTextureCoordLocation = -1
    private var uTextureMatrixLocation = -1
    private var uTextureSamplerLocation = -1



    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {

        mContext = context
        setEGLContextClientVersion(2)
        setRenderer(this)
        renderMode = RENDERMODE_WHEN_DIRTY

    }

    override fun onDrawFrame(gl: GL10) {
        LogUtil.d("onDrawFrame")
        val t1 = System.currentTimeMillis()
       /* GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or  GLES20.GL_DEPTH_BUFFER_BIT)
        mSurfaceTexture!!.updateTexImage()
        var mtx = FloatArray(16)
        mSurfaceTexture!!.getTransformMatrix(mtx)
        mDirectDrawer!!.draw(mtx)*/

        if (mSurfaceTexture != null) {
            mSurfaceTexture!!.updateTexImage()
            mSurfaceTexture!!.getTransformMatrix(transformMatrix)
        }


        glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

        aPositionLocation = glGetAttribLocation(mShaderProgram, FilterEngine.POSITION_ATTRIBUTE)
        aTextureCoordLocation = glGetAttribLocation(mShaderProgram, FilterEngine.TEXTURE_COORD_ATTRIBUTE)
        uTextureMatrixLocation = glGetUniformLocation(mShaderProgram, FilterEngine.TEXTURE_MATRIX_UNIFORM)
        uTextureSamplerLocation = glGetUniformLocation(mShaderProgram, FilterEngine.TEXTURE_SAMPLER_UNIFORM)

        glActiveTexture(GLES20.GL_TEXTURE0)
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextrueId)
        glUniform1i(uTextureSamplerLocation, 0)
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)

        if (mDataBuffer != null) {
            mDataBuffer!!.position(0)
            glEnableVertexAttribArray(aPositionLocation)
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, mDataBuffer)

            mDataBuffer!!.position(2)
            glEnableVertexAttribArray(aTextureCoordLocation)
            glVertexAttribPointer(aTextureCoordLocation, 2, GL_FLOAT, false, 16, mDataBuffer)
        }

        //glDrawElements(GL_TRIANGLE_FAN, 6,GL_UNSIGNED_INT, 0);
        //glDrawArrays(GL_TRIANGLE_FAN, 0 , 6);
        glDrawArrays(GL_TRIANGLES, 0, 6)
        //glDrawArrays(GL_TRIANGLES, 3, 3);
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        val t2 = System.currentTimeMillis()
        val t = t2 - t1
        Log.i("lb6905", "onDrawFrame: time: $t")



    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {


    }


    private var mFilterEngine: FilterEngine? = null
    private var mDataBuffer: FloatBuffer? = null
    private var mShaderProgram = -1
    private val mFBOIds = IntArray(1)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {
        //创建
        mTextrueId = createTextureID()
        mSurfaceTexture = SurfaceTexture(mTextrueId)
        mSurfaceTexture!!.setOnFrameAvailableListener(this)
       /* mSurfaceTexture = SurfaceTexture(mTextrueId)
        mSurfaceTexture!!.setOnFrameAvailableListener(this)
        mDirectDrawer = DirectDrawer(mTextrueId)*/
        mFilterEngine = FilterEngine(mTextrueId, mContext)
        mDataBuffer = mFilterEngine!!.getBuffer()
        mShaderProgram = mFilterEngine!!.getShaderProgram()
        glGenFramebuffers(1, mFBOIds, 0)
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOIds[0])
        Log.i("lb6905", "onSurfaceCreated: mFBOId: " + mFBOIds[0])

        CameraHelper.openCamera(0,mSurfaceTexture!!)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {

            requestRender()

    }

     private fun createTextureID(): Int {
        var texture = IntArray(1)
        glGenTextures(1, texture, 0)
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat())
        glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
        glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
        return texture[0]
    }

}