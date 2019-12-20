package com.lee.mystudy.actiivity;

import android.content.Context;
import android.media.tv.TvContract;
import android.opengl.GLSurfaceView;

import com.lee.mystudy.R;
import com.lee.mystudy.util.LogUtil;
import com.lee.mystudy.util.ShaderHelper;
import com.lee.mystudy.util.TextResouceReader;

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
import static android.opengl.Matrix.orthoM;

public class MyOpenGLRender implements GLSurfaceView.Renderer {

    //加入矩阵  解决普通位置坐标导致的画面变形问题
    //保存顶点着色器定义的uniform 矩阵的名字
    private final String U_MATRIX = "u_Matrix";
    //还需要一个储存矩阵的数组  一般是16个长度
    private float[] projectionMatirx = new float[16];
    //还需要一个储存位置的值
    private int uMatrixLocation ;


    private final int POSITION_COMPONENT_COUNT = 2;
    private  final int BYTES_PER_FLOAT = 4;
    private  FloatBuffer vertextData = null;
    private Context mContext;

    private final String A_COLOR = "a_Color";
    private final int COLOR_COMPONENT_COUNT = 3;
    //表示一个顶点在数组里占用几位  本地内存里
    private final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;



    //链接程序的id
    private int program;

    //当OpenGL把着色器链接为一个程序时，它实际上用一个位置编号把片段着色器中定义的每个uniform都关联起来
    //这些位置编号用来给着色器发送数据，并且我们需要u_color的位置，以便我们在绘制的时候确定颜色
    private static String U_COLOR = "u_Color";
    //uniform 的位置  uniform 的位置  不是事先指定好的，当程序链接成功了，w我们就需要查询这个位置，一个uniform的位置时唯一的，
    //即使在两个程序使用了同一个uniform名字，位置也不一样。
    private int uColorLocation;

    //与uniform一样属性也需要位置信息
    private static String A_POSITION = "a_Position";
    private int aPositionLocation;

    public MyOpenGLRender(Context context) {
        mContext = context;
        float[] tableVerticsWithTriangle = {
                //openGL 只能绘制点 线 三角形
                //所以可以把一个矩形看作两个三角形

                //给桌子加上一个边框
                -0.55f,-0.83f,0.7f,0.1f,03f,
                0.55f,0.83f,0.7f,0.1f,03f,
                -0.55f,0.83f,0.7f,0.1f,03f,

                -0.55f,-0.83f,0.7f,0.1f,03f,
                0.55f,-0.83f,0.7f,0.1f,03f,
                0.55f,0.83f,0.7f,0.1f,03f,

                //继续增加多个三角形  减少三角形边缘突出
                //第一个
                -0.25f,-0.4f,1f,1f,1f,
                -0.5f,-0.8f,0.7f,0.7f,0.7f,
                0f,-0.8f,0.7f,0.7f,0.7f,
                0f,0f,0.7f,0.7f,0.7f,
                -0.5f,0f,0.7f,0.7f,0.7f,
                -0.5f,-0.8f,0.7f,0.7f,0.7f,

               //第二个
                0.25f,-0.4f,1f,1f,1f,
                0f,-0.8f,0.7f,0.7f,0.7f,
                0.5f,-0.8f,0.7f,0.7f,0.7f,
                0.5f,0f,0.7f,0.7f,0.7f,
                0f,0f,0.7f,0.7f,0.7f,
                0f,-0.8f,0.7f,0.7f,0.7f,

                //第三个
               0.25f,0.4f,1f,1f,1f,
                0f,0f,0.7f,0.7f,0.7f,
                0.5f,0f,0.7f,0.7f,0.7f,
                0.5f,0.8f,0.7f,0.7f,0.7f,
                0f,0.8f,0.7f,0.7f,0.7f,
                0f,0f,0.7f,0.7f,0.7f,

                //第四个
                -0.25f,0.4f,1f,1f,1f,
                -0.5f,0f,0.7f,0.7f,0.7f,
                0f,0f,0.7f,0.7f,0.7f,
                0f,0.8f,0.7f,0.7f,0.7f,
                -0.5f,0.8f,0.7f,0.7f,0.7f,
                -0.5f,0f,0.7f,0.7f,0.7f,

               //开始优化
               /* 0.0f,0.0f,1f,1f,1f,
                -0.5f,-0.5f,0.7f,0.7f,0.7f,
                0.5f,-0.5f,0.7f,0.7f,0.7f,

                0.5f,0.5f,0.7f,0.7f,0.7f,
                -0.5f,0.5f,0.7f,0.7f,0.7f,
                -0.5f,-0.5f,0.7f,0.7f,0.7f,*/

                //mid line

                -0.5f,0f,1f,1f,0.0f,
                0.5f,0f,0f,0f,1.0f,


                //first handle
                0f,-0.4f,0.2f,0.2f,0.7f,
                //second handle
                0f,0.4f,0.1f,0.7f,0.1f,
                //third
                0.0f,0.0f,0.3f,0.7f,0.2f
        };

        //顶点数据在本地内存里
        vertextData = ByteBuffer.allocateDirect(tableVerticsWithTriangle.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertextData.put(tableVerticsWithTriangle);

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
        String vertexResouce = TextResouceReader.readTextFileFromResouce(mContext, R.raw.simple_vertex_shader);
        LogUtil.INSTANCE.d("vertexShadex code : "+vertexResouce);

        String fragmentResouce = TextResouceReader.readTextFileFromResouce(mContext, R.raw.simple_fragment_shader);
        LogUtil.INSTANCE.d("fragmentShadex code : "+fragmentResouce);

        int vertexShadex = ShaderHelper.complieVertexShadex(vertexResouce);
        int fragmentShadex = ShaderHelper.complieFragmentShadex(fragmentResouce);

        program = ShaderHelper.linkProgram(vertexShadex,fragmentShadex);

        //检查性能
        boolean validateProgram = ShaderHelper.validateProgram(program);
        LogUtil.INSTANCE.d("validateProgram : "+validateProgram);
        //告诉OpenGL 绘制的时候使用这个program
        glUseProgram(program);

        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        //获取uniform的位置
      //  uColorLocation = glGetUniformLocation(program,U_COLOR);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
        //获取属性位置
       aPositionLocation = glGetAttribLocation(program,A_POSITION);

        //关联属性与顶点数据的数组  告诉OpenGL从第一个开始读取
        vertextData.position(0);
        //告诉OpenGL 在缓冲区vertexData  中找到a_Position 对应的数据

        /**
         * aPositionLocation 属性的位置
         * POSITION_COMPONENT_COUNT 属性的数据计数，对于这个属性有多少个分量与每个顶点关联，上文中我们定义每个顶点使用两个float
         * （** 我们为每个顶点传递了两个分量，但在着色器中，a_Position 被定义为vec4,它有4个分量，如果一个分量在没有被指定值的情况下。OpenGL会设置0.0.0.1 **）
         * GL_FLOAT 数据类型
         * normailzed 使用整形时才有意义
         * stride 当一个数组储存多个参数是才有意义
         * vertextData  读取的数据源
         */

        glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertextData);
        //使能顶点数据  告诉OpenGL所需要的数据位置
        glEnableVertexAttribArray(aPositionLocation);

        //告诉OpenGL从哪个位置开始读取颜色属性
        vertextData.position(POSITION_COMPONENT_COUNT);
        //把带颜色属性跟着色器的a_color关联起来
        glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertextData);
        glEnableVertexAttribArray(aColorLocation);

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
        float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float)width;

        if(width > height){

            /**
             *  目标数组16位  结果矩阵的起始偏移值  x轴的最小范围  x 轴的最大取值范围
             *
             *  y轴的最小范围   y轴的最大取值范围  z轴的最小范围  z轴的最大取值范围
             *
             */
            //landscape 横屏扩展宽度的比值  -1，1 >>>> -aspectRatio,aspectRatio
            orthoM(projectionMatirx,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
        }else {
            //square 竖屏 扩展高度比值 -1，1 >>> aspectRatio,aspectRatio
            orthoM(projectionMatirx,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
        }

    }

    /**
     * 当绘制一帧时这个方法会被GLSurfaceView 调用，在这个方法中，我们一定要画些什么，即使只是清空屏幕，因为在这个方法返回后，
     * 渲染缓冲区会被交换并显示在屏幕上。如果什么都没有画，可能会出现糟糕的闪烁效果
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatirx,0);
        //绘制桌子边框
       // glUniform4f(uColorLocation,0.7f,0.1f,03f,0.7f);
        glDrawArrays(GL_TRIANGLE_FAN,0,6);
        //绘制桌子
        //更新着色器中的u_color 的值，与属性不同uniform没有默认值  所以指定4个分量 RGBA
    //   glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
        //绘制的形状  一个桌子是两个三角形组成   从数组开始处开始读取顶点信息  总共多少个顶点  6个
        glDrawArrays(GL_TRIANGLE_FAN,6,6);

        glDrawArrays(GL_TRIANGLE_FAN,12,6);

        glDrawArrays(GL_TRIANGLE_FAN,18,6);

        glDrawArrays(GL_TRIANGLE_FAN,24,6);

        //绘制分割线  分割线有两个点
     //   glUniform4f(uColorLocation,1.0f,0.2f,0.4f,0.6f);
         //同上  第7个开始就是分割线的顶点了  有两个

         glDrawArrays(GL_LINES,30,2);

         //绘制两个棒槌😂 其实就是两个点
     //  glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f);
       glDrawArrays(GL_POINTS,32,1);
        //第二个
     //   glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        glDrawArrays(GL_POINTS,33,1);
        //第三个
     //   glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        glDrawArrays(GL_POINTS,34,1);



    }
}
