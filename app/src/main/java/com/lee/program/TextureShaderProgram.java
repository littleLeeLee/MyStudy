package com.lee.program;

import android.content.Context;

import com.lee.mystudy.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class TextureShaderProgram extends ShaderProgram{

    //uniform location
    private int uMatrixLocation;
    private int uTextureUnitLocation;

    //attribute location
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context){
        super(context, R.raw.texture_vertex_shader,R.raw.texture_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program,U_TEXTURE_UNIT);

        //RETRIEVE
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program,A_TEXTURE_COORDINATES);

    }


    public void setUniforms(float[] matrix ,int textureId){

        //传递矩阵给unforme
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        //把活动的纹理单元设置为纹理单元0，以此开始
        glActiveTexture(GL_TEXTURE0);
        //把纹理绑定到这个单元
        glBindTexture(GL_TEXTURE_2D,textureId);
        //把选定的纹理单元传递给片段着色器的u_TextureUnit
        glUniform1i(uTextureUnitLocation,0);

    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation(){

        return aTextureCoordinatesLocation;

    }
}
