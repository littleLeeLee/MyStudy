package com.lee.mystudy.object;

import android.util.FloatMath;

import com.lee.mystudy.util.Geometry;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_ALWAYS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

public class ObjectBuilder {

    private static final int FLOATS_PER_VERTEX = 3;
    //保存顶点数据的数组
    private float[] vertexData;
    private int offset = 0;

    private ArrayList<DrawCommand> drawList = new ArrayList<DrawCommand>();

    public ObjectBuilder(int  sizeInVertices) {
       vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    //计算圆柱体顶不顶点数量
    private static int sizeOfCircleInVertices(int numPoints){
        //第一个顶点需要重复两次
        return 1 + ( numPoints + 1);
    }

    //计算圆柱体侧面的顶点数量
    private static int sizeOfOpenCylinderInVertices(int numPoints){
        return (numPoints + 1) * 2;
    }


    //生成冰球 (一个冰球由一个圆柱体的顶部和一个圆柱体的侧面构成)
    static GeneratedData CreatePuck(Geometry.Cylinder puck,int numPoints){
        int size = sizeOfCircleInVertices(numPoints)
                + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder objectBuilder = new ObjectBuilder(size);
        Geometry.Circle puckTop = new Geometry.Circle(puck.center.translateY(puck.height / 2f), puck.radius);

        objectBuilder.appendCircle(puckTop,numPoints);
        objectBuilder.appendOpenCylinder(puck,numPoints);

        return objectBuilder.build();


    }

    static GeneratedData createMallet(Geometry.Point center,float radius,float height,int numPoints){

        int size = sizeOfCircleInVertices(numPoints) * 2
                + sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder objectBuilder = new ObjectBuilder(size);
        float baseHeight = height * 0.25f;

        Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight), radius);

        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight);

        objectBuilder.appendCircle(baseCircle,numPoints);
        objectBuilder.appendOpenCylinder(baseCylinder,numPoints);

        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Geometry.Circle handCircle = new Geometry.Circle(center.translateY(height * 0.5f), handleRadius);
        Geometry.Cylinder handleCylinder = new Geometry.Cylinder(handCircle.center.translateY(-handleHeight / 2f), handleRadius, handleHeight);

        objectBuilder.appendCircle(handCircle,numPoints);
        objectBuilder.appendOpenCylinder(handleCylinder,numPoints);


        return objectBuilder.build();
    }


    private void appendCircle(Geometry.Circle circle , int numPoints){

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        //center point of fan
        vertexData[offset++]  = circle.center.x;
        vertexData[offset++]  = circle.center.y;
        vertexData[offset++]  = circle.center.z;

        for (int i = 0; i < numPoints; i++) {

            float angleInRadians = ((float) i / (float) numPoints)
                    * ((float) Math.PI * 2f);

            vertexData[offset++] = circle.center.x
                    + circle.radius * (float) Math.cos(angleInRadians);

            vertexData[offset++] = circle.center.y;

            vertexData[offset++] = circle.center.z +
                    circle.radius * (float) Math.sin(angleInRadians);


        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN,startVertex,numVertices);
            }
        });

    }


    private void appendOpenCylinder(Geometry.Cylinder cylinder,int numPoints){

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        float yStart = cylinder.center.y - (cylinder.height / 2f);
        float yEnd = cylinder.center.y + (cylinder.height / 2f);


        for (int i = 0; i < numPoints; i++) {

            float angleInRadians = ((float) i / (float)numPoints)
                    * ((float)Math.PI * 2f);

            float xPosition = cylinder.center.x + cylinder.radius
                    * (float) Math.cos(angleInRadians);

            float zPosition = cylinder.center.z +
                    cylinder.radius * (float) Math.sin(angleInRadians);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;

            drawList.add(new DrawCommand() {
                @Override
                public void draw() {

                    glDrawArrays(GL_TRIANGLE_STRIP,startVertex,numVertices);

                }
            });
        }


    }

    interface DrawCommand{
        void draw();
    }

    static class GeneratedData{

        float[] vertexData;
        List<DrawCommand> drawList;

         GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }

        private GeneratedData build(){
             return new GeneratedData(vertexData,drawList);
        }

    }

}
