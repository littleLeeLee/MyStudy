package com.lee.mystudy.util;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetAttachedShaders;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * 着色器对象
 */
public class ShaderHelper {


    private static String TAG = "wylee ShaderHelper";

    public static int complieVertexShadex(String shaderCode){

        return complieShader(GL_VERTEX_SHADER,shaderCode);

    }


    public static int complieFragmentShadex(String shaderCode){

        return complieShader(GL_FRAGMENT_SHADER,shaderCode);

    }


    private static int complieShader(int type,String shaderCode) {

        //创建一个着色器对象，并把这个对象的ID 存入shaderObjectId  type 代表时顶点着色器 还是片段着色器
        //这个int id 就是openGL 对象的引用，无论后面什么时候要引用这个对象，都要把这个id传回OpenGL
        //返回0就代表创建失败，类似于返回null   因为OpenGL 实际上内部不会抛出错误  可通过glGetError 获取
         int shaderObjectId = glCreateShader(type);
        LogUtil.INSTANCE.d(" shaderObjectId :" + shaderObjectId);
        if(shaderObjectId == 0){

            LogUtil.INSTANCE.d("create shader fail");

            return 0;
        }
        //有了着色器对象 就可以掉用此方法上传源代码 告诉OpenGL 读入字符串shaderCode 定义的代码，
        // 并把他于shaderObjectId 对应的着色器对象关联起来
        //然后就可以调用glComplieShader  编译这个着色器
        glShaderSource(shaderObjectId,shaderCode);
        //编译着色器  编译上个方法上传的代码
        glCompileShader(shaderObjectId);

        //取出编译后的代码
        int[] complieState = new int[1];
        //告诉OpenGL 读取与shaderID 关联的编译状态  并把他们写入compileState 第0个元素
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,complieState,0);
        LogUtil.INSTANCE.d(TAG + " complieState "+complieState[0]);

        //顺便看看log信息
        String getShaderInfoLog = glGetShaderInfoLog(shaderObjectId);
     //   LogUtil.INSTANCE.d(TAG + " getShaderInfoLog "+getShaderInfoLog);
        Log.v(TAG,"result of shader code " + "\n" + shaderCode + "\n:" + getShaderInfoLog);

        if(complieState[0] == 0){
            //失败  删除着色器对象
            glDeleteShader(shaderObjectId);

            LogUtil.INSTANCE.d(" compile shader fail");

            return 0;

        }

        return shaderObjectId;
    }


    //一个OpenGL程序就是把一个顶点着色器和一个片段着色器链接在一起变成单个对象
    //顶点着色器和片段着色器总是一起工作的，没有片段着色器OpenGL就不知道怎么绘制那些组成点 直线 三角形的片段
    //没有顶点着色器OpenGL 就不知道在哪里绘制这些片段
    //顶点着色器是计算屏幕上每个顶点d的最终位置，OpenGL把这些顶点组织成点，线，三角形，并且分解为多个片段时
    //会询问片段着色器每个片段的最终颜色


    /**
     *  构建program 对象
     * @param vextexShaderId
     * @param fragmentSharderId
     * @return
     */

    public static int linkProgram(int vertexShaderId,int fragmentSharderId){

        int programObjectId = glCreateProgram();
        LogUtil.INSTANCE.d(" programObjectId :" + programObjectId);

        if(programObjectId == 0){

            LogUtil.INSTANCE.d(" create program fail");

            return 0;

        }
        //把顶点着色器和片段着色器f附加到程序对象上
        glAttachShader(programObjectId,vertexShaderId);
        glAttachShader(programObjectId,fragmentSharderId);

        //链接程序
        glLinkProgram(programObjectId);

        //检查是否链接成功
        int[] linkPrograState = new int[1];
        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkPrograState,0);


        //再查看一下日志
        LogUtil.INSTANCE.d(" program log :" + glGetProgramInfoLog(programObjectId));

        //检查状态
        if(linkPrograState[0] == 0){

            LogUtil.INSTANCE.d("link program fail");
            glDeleteProgram(programObjectId);
            return 0;
        }

        return programObjectId;
    }

    //验证当前程序在opengl 中的性能
    public static boolean validateProgram(int programId){

        glValidateProgram(programId);
        int[] validateState = new int[1];
        glGetProgramiv(programId,GL_VALIDATE_STATUS,validateState,0);
        //看看log信息
        LogUtil.INSTANCE.d("program validate :" + glGetProgramInfoLog(programId));

        return validateState[0] != 0;

    }

    //编译vertexShaderSource和fragmentShaderSource 并把他们链接成一个程序
    public static int buildProgram(String vertexShaderSource,String fragmentShaderSource){

        int program;
        //加载着色器
        int vertexShadex = complieVertexShadex(vertexShaderSource);
        int fragmentShadex = complieFragmentShadex(fragmentShaderSource);

        //链接
        program = linkProgram(vertexShadex,fragmentShadex);

        validateProgram(program);

        return program;

    }

    }
