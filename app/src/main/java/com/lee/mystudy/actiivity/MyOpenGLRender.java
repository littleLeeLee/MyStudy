package com.lee.mystudy.actiivity;

import android.content.Context;
import android.media.tv.TvContract;
import android.opengl.GLSurfaceView;

import com.lee.mystudy.R;
import com.lee.mystudy.object.Mallet;
import com.lee.mystudy.object.Puck;
import com.lee.mystudy.object.Table;
import com.lee.mystudy.util.LogUtil;
import com.lee.mystudy.util.MatrixHelper;
import com.lee.mystudy.util.ShaderHelper;
import com.lee.mystudy.util.TextResouceReader;
import com.lee.mystudy.util.TextureHelper;
import com.lee.program.ColorShaderProgram;
import com.lee.program.TextureShaderProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

public class MyOpenGLRender implements GLSurfaceView.Renderer {

    private Context mContext;

    private float[] projectionMatrix = new float[16];
    private float[] modeMatrix = new float[16];

    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;

    private float[] viewMatrix = new float[16];
    private float[] viewProjectionMatrix = new float[16];
    private float[] modelViewProjectionMatrix = new float[16];

    private Puck puck;


    public MyOpenGLRender(Context context) {
        mContext = context;

    }



    /**
     * 当surface 被创建时 Glsurfaceview 回调用这个方法，这个发生再程序第一次运行的时候，并且当设备被唤醒或者用户从其他activity
     * 切换回来时也可能会被调用。也就是说再实际中该方法可能会被调用多次。
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
        table = new Table();
        mallet = new Mallet(0.08f,0.15f,32);
        puck = new Puck(0.06f,0.02f,32);

        textureShaderProgram = new TextureShaderProgram(mContext);
        colorShaderProgram = new ColorShaderProgram(mContext);
        texture = TextureHelper.loadTextrue(mContext,R.drawable.table);

    }

    /**
     * 在surface 被创建后，每次surface 尺寸发生变化   这个方法都会被GLsufaceview 调用。
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
        //j计算不同方向时的宽高比 不管哪个方向 比值是一样的   在于什么时候使用不同的比值
        //横屏扩展宽度的比值   竖屏扩大高度的比值
     /*   float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float)width;

        if(width > height){

            *//**
             *  目标数组16位  结果矩阵的起始偏移值  x轴的最小范围  x 轴的最大取值范围
             *
             *  y轴的最小范围   y轴的最大取值范围  z轴的最小范围  z轴的最大取值范围
             *
             *//*
            //landscape 横屏扩展宽度的比值  -1，1 >>>> -aspectRatio,aspectRatio
            orthoM(projectionMatirx,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
        }else {
            //square 竖屏 扩展高度比值 -1，1 >>> aspectRatio,aspectRatio
            orthoM(projectionMatirx,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
        }*/

        MatrixHelper.perspectiveM(projectionMatrix,45,(float) width / (float)height,1f,10f);
        setLookAtM(viewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);
        setIdentityM(modeMatrix,0);
        translateM(modeMatrix,0,0f,0f,-2.5f);
        rotateM(modeMatrix,0,-50f,1f,0f,0f);

        float[] temp = new float[16];
        multiplyMM(temp,0,projectionMatrix,0,modeMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);



    }

    /**
     * 当绘制一帧时这个方法会被GLSurfaceView 调用，在这个方法中，我们一定要画些什么，即使只是清空屏幕，因为在这个方法返回后，
     * 渲染缓冲区会被交换并显示在屏幕上。如果什么都没有画，可能会出现糟糕的闪烁效果
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL_COLOR_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

      /*  //draw table
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(projectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        //draw mallet
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();*/

        positionTableScene();

        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();


        positionObjectInScene(0f,mallet.);

    }
}
