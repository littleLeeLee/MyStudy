package com.lee.program;

import android.content.Context;

import com.lee.mystudy.util.ShaderHelper;
import com.lee.mystudy.util.TextResouceReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {
    //uniform
    protected static String U_MATRIX = "u_Matrix";
    protected static String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static String U_COLOR = "u_Color";

    //attribute
    protected static String A_POSITION = "a_Position";
    protected static String A_COLOR = "a_Color";
    protected static  String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    //shader program
    protected int program;

    protected ShaderProgram(Context context,int vertexShaderSourceId,int fragmentShaderSourceId){

        program = ShaderHelper.buildProgram(TextResouceReader.readTextFileFromResouce(context,vertexShaderSourceId),
                TextResouceReader.readTextFileFromResouce(context,fragmentShaderSourceId));


    }


    public void useProgram(){

        glUseProgram(program);

    }


}
