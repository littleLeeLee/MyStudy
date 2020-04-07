package com.lee.mystudy.object;

import com.lee.mystudy.data.VertexArray;
import com.lee.program.ColorShaderProgram;

import java.net.PortUnreachableException;
import java.util.List;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.lee.mystudy.data.Constants.BYTES_PER_FLOAT;

/**
 * 定义棒槌的数据类
 */
public class Mallet {

    //坐标属性的个数
    private static int POSITON_COMPONENT_COUNT = 3;
   /* //颜色属性的个数
    private static int COLOR_COMPONENT_COUNT = 3;
    //行距 所占用的字节数 *8
    private static int STRIDE = (POSITON_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    //顶点数据
    private static float[] VERTEX_DATA = {
            0f,-0.4f,   0f,1f,1f,
            0f,0.4f,    1f,1f,0f

    };
    private VertexArray vertexArray;

    public  Mallet(){

        vertexArray = new VertexArray(VERTEX_DATA);

    }

    public void bindData(ColorShaderProgram colorShaderProgram){

        vertexArray.setVertexAttribPointer(0,colorShaderProgram.getPositionAttributeLocation(),
                POSITON_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(POSITON_COMPONENT_COUNT,colorShaderProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);

    }

    public void draw(){
        glDrawArrays(GL_POINTS,0,2);
    }*/

   public float radius;
   private float height;

   private VertexArray vertexArray;
   private List<ObjectBuilder.DrawCommand> drawList;



}
