package com.lee.program;

import android.content.Context;

import com.lee.mystudy.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ColorShaderProgram extends ShaderProgram{

    private int uMatrixLocation;

    private int aPositionLocation;
  //  private int aColorLocation;
    private int uColorLocation;

    public ColorShaderProgram(Context context){

    super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader);

    uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
    aPositionLocation = glGetAttribLocation(program,A_POSITION);
  //  aColorLocation = glGetAttribLocation(program,A_COLOR);
    uColorLocation = glGetUniformLocation(program,U_COLOR);

    }

    public void setUniforms(float[] matrix,float r,float g,float b){

        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        glUniform4f(uColorLocation,r,g,b,1f);
     // glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

    }

    public int getPositionAttributeLocation(){

        return aPositionLocation;
    }

  /*  public int getColorAttributeLocation(){

        return aColorLocation;
    }*/


}