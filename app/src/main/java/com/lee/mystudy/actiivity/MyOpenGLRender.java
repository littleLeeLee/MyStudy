package com.lee.mystudy.actiivity;

import android.content.Context;
import android.media.Image;
import android.media.tv.TvContract;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.lee.mystudy.R;
import com.lee.mystudy.object.Mallet;
import com.lee.mystudy.object.Puck;
import com.lee.mystudy.object.Table;
import com.lee.mystudy.util.Geometry;
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
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
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
    private Puck puck;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;
    //储存视图矩阵
    private float[] viewMatrix = new float[16];
    private float[] viewProjectionMatrix = new float[16];
    private float[] modelViewProjectionMatrix = new float[16];

    private float[] invertedViewProjectionMatrix = new float[16];

    private float leftBound = -0.5f;
    private float rightBound = 0.5f;
    private float farBound = -0.8f;
    private float nearBound = 0.8f;


    private boolean malletPressed = false;
    private Geometry.Point blueMalletPosition;

    //上一步木槌的位置
    private Geometry.Point previousBlueMalletPosition;
    //冰球的位置
    private Geometry.Point puckPosition;
    //冰球的速度和方向
    private Geometry.Vector puckVector;

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

        blueMalletPosition = new Geometry.Point(0f,mallet.height / 2f,0.4f);
        puckPosition = new Geometry.Point(0f,puck.height / 2f,0f);
        puckVector = new Geometry.Vector(0f,0f,0f);

        textureShaderProgram = new TextureShaderProgram(mContext);
        colorShaderProgram = new ColorShaderProgram(mContext);
        texture = TextureHelper.loadTextrue(mContext,R.drawable.air_hockey_surface);

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

        MatrixHelper.perspectiveM(projectionMatrix,45,(float) width / (float)height,1f,10f);
        setLookAtM(viewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);



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
    /*    setIdentityM(modeMatrix,0);
        translateM(modeMatrix,0,0f,0f,-2.5f);
        rotateM(modeMatrix,0,-50f,1f,0f,0f);

        float[] temp = new float[16];
        multiplyMM(temp,0,projectionMatrix,0,modeMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);

*/

    }

    /**
     * 当绘制一帧时这个方法会被GLSurfaceView 调用，在这个方法中，我们一定要画些什么，即使只是清空屏幕，因为在这个方法返回后，
     * 渲染缓冲区会被交换并显示在屏幕上。如果什么都没有画，可能会出现糟糕的闪烁效果
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL_COLOR_BUFFER_BIT);
        puckPosition = puckPosition.translate(puckVector);
        //给冰球加入边界判断

        //向左移动的过远
        if(puckPosition.x <leftBound + puck.radius ||
        puckPosition.x > rightBound - puck.radius){

            puckVector = new Geometry.Vector(-puckVector.x,puckVector.y,puckVector.z);

        }
        //向右移动的过远
        if(puckPosition.z < farBound + puck.radius ||
        puckPosition.z > nearBound - puck.radius){
            puckVector  = new Geometry.Vector(puckVector.x,puckVector.y,puckVector.z);
        }
        //近边和远边的判断
        puckPosition = new Geometry.Point(clamp(puckPosition.x,leftBound + puck.radius,
                rightBound - puck.radius),puckPosition.y,
                clamp(puckPosition.z,farBound + puck.radius,nearBound - puck.radius));


        //投影矩阵和视图矩阵相乘的结果存到viewProjectionMatrix
        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
        //创建一个反转的矩阵
        invertM(invertedViewProjectionMatrix,0,viewProjectionMatrix,0);

        //draw table
        positionTableInScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        //draw mallet
        positionObjectInScene(0f,mallet.height / 2f,-0.4f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectionMatrix,1f,0f,0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        positionObjectInScene(blueMalletPosition.x,blueMalletPosition.y,blueMalletPosition.z);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix,0f,0f,1f);
        mallet.draw();

        //draw puck
        positionObjectInScene(puckPosition.x,puckPosition.y,puckPosition.z);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix,0.8f,0.8f,1f);
        puck.bindData(colorShaderProgram);
        puck.draw();
       puckVector = puckVector.scale(0.99f);
      //  puckVector = puckVector.scale(0.9f);
     //   puckVector = puckVector.scale(0.9f);


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

    }

    private void positionObjectInScene(float x, float y, float z) {

        setIdentityM(modeMatrix,0);
        translateM(modeMatrix,0,x,y,z);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,
                modeMatrix,0);

    }

    private void  positionTableInScene() {

        setIdentityM(modeMatrix,0);
        rotateM(modeMatrix,0,-90f,1f,0f,0f);
        multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modeMatrix,0);


    }


    public void handleTouchPress(float normalizedX, float normalizedY) {

        Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX,normalizedY);
        //创建与木槌相当大小的包围球
        Geometry.Sphere malletBoundingSphere = new Geometry.Sphere(
                new Geometry.Point(blueMalletPosition.x,blueMalletPosition.y,blueMalletPosition.z)
        ,mallet.height / 2f);
        malletPressed = Geometry.intersects(malletBoundingSphere,ray);

    }

    //反转透视投影和透视除法
    private Geometry.Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY) {

        Log.d("wylee","normalizedX：" + normalizedX +"，normalizedY：" + normalizedY);

            float[] nearPointNdc = {normalizedX,normalizedY,-1,1};
            float[] farPointNdc = {normalizedX,normalizedY,1,1};

            float[] nearPointWorld = new float[4];
            float[] farPointWorld = new float[4];
            multiplyMV(nearPointWorld,0,invertedViewProjectionMatrix,0,nearPointNdc,0);
            multiplyMV(farPointWorld,0,invertedViewProjectionMatrix,0,farPointNdc,0);
            divideByW(nearPointWorld);
            divideByW(farPointWorld);

        Geometry.Point nearPointRay = new Geometry.Point(nearPointWorld[0],
                nearPointWorld[1],nearPointWorld[2]);

        Geometry.Point farPointRay = new Geometry.Point(farPointWorld[0],
                farPointWorld[1],farPointWorld[2]);
        return new Geometry.Ray(nearPointRay,Geometry.vectorBetween(nearPointRay,farPointRay));
    }

    private void divideByW(float[] vector) {

        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];

    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {

        previousBlueMalletPosition = blueMalletPosition;
        if(malletPressed){

            Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
            Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0,0,0),new Geometry.Vector(0,1,0));
            Geometry.Point touchedPoint = Geometry.intersectionPoint(ray,plane);
            blueMalletPosition = new Geometry.Point(
                    clamp(touchedPoint.x,
                            leftBound + mallet.radius,
                            rightBound - mallet.radius),
                    mallet.height / 2f,
                    clamp(touchedPoint.z,
                            0f+mallet.radius,
                            nearBound - mallet.radius)
                    );

            //碰撞测试代码
            float distance = Geometry.vectorBetween(blueMalletPosition, puckPosition).length();

            //判断蓝色木槌和冰球之间的距离 如果小于它们的半径之和 就算是击中
            //并且用前一个木槌的位置和当前木槌的位置创建了一个方向向量，木槌移动的越快向量越大 冰球移动的越远
            if(distance < (puck.radius + mallet.radius)){

                puckVector = Geometry.vectorBetween(
                        previousBlueMalletPosition,blueMalletPosition
                );

            }

        }

    }


    public static float clamp(float value ,float min,float max){

        return Math.min(max,Math.max(value,min));

    }
}
