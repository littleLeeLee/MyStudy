package com.lee.mystudy.object;

import com.lee.mystudy.data.VertexArray;
import com.lee.program.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static com.lee.mystudy.data.Constants.BYTES_PER_FLOAT;

/**
 * 定义桌子数据的类
 */
public class Table {
    //位置坐标分量
    private static int POSITION_COMPONENT_COUNT = 2;
    //纹理坐标分量
    private static int  TEXTURE_COORDINATES_COMPONET_COUNT = 2;
    //跨距
    private static int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONET_COUNT) * BYTES_PER_FLOAT;

    //顶点数据数组
    private static float[] VERTEX_DATA = {
            //order X,Y ,S,T

            0f,0f,         0.5f,0.5f,
            -0.4f,-0.8f,   0f,0.9f,
            0.4f,-0.8f,    1f,0.9f,
            0.4f,0.8f,     1f,0.1f,
            -0.4f,0.8f,    0f,0.1f,
            -0.4f,-0.8f,   0f,0.9f

    };

    private VertexArray  vertexArray;
    public Table(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureShaderProgram){
        //从着色器获取每个属性的w位置
        vertexArray.setVertexAttribPointer(0,
                //把位置s数据绑定到b被引用的着色器属性上
                textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
                //把纹理坐标数据绑定到被引用的着色器属性上
                textureShaderProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONET_COUNT,
                STRIDE);

    }


    public void draw(){

        glDrawArrays(GL_TRIANGLE_FAN,0,6);

    }



}
