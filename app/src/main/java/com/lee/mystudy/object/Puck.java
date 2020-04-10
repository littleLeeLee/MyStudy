package com.lee.mystudy.object;

import com.lee.mystudy.data.VertexArray;
import com.lee.mystudy.util.Geometry;
import com.lee.program.ColorShaderProgram;

import java.util.List;

public class Puck {

    private static int POSITION_COMPONENT_COUNT = 3;
    public float radius,height;
    private VertexArray vertexArray;

    private List<ObjectBuilder.DrawCommand> drawList;


    public Puck(float radius, float height, int numPointsAroundPuck) {
        ObjectBuilder.GeneratedData generatedData =
                ObjectBuilder.createPuck(new Geometry.Cylinder(
                new Geometry.Point(0f,0f,0f),radius,height),
                        numPointsAroundPuck);
        this.radius = radius;
        this.height = height;
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }


    public void bindData(ColorShaderProgram colorShaderProgram){

        vertexArray.setVertexAttribPointer(
                0,colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,0);

    }

    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {

            drawCommand.draw();

        }
    }
}

