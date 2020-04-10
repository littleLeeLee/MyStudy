package com.lee.mystudy.object;

import com.lee.mystudy.util.Geometry;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

public class ObjectBuilder {

    //顶点需要的浮点数
    private static int FLOATS_PER_VERTEX = 3;
    private float[] vertexData;
    private int offset = 0;


    static class GeneratedData{

        float[] vertexData;
        List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }

    }

    private GeneratedData build(){
        return new GeneratedData(vertexData,drawList);
    }


    private ObjectBuilder(int sizeOfVertices) {

        //顶点数据的长度
        vertexData = new float[sizeOfVertices * FLOATS_PER_VERTEX];

    }

    //计算圆柱体顶部顶点数量的方法作为开始
    private static int sizeOfCircleInVertices(int numPoints){

        return 1 + (numPoints + 1);

    }

    //计算圆柱体侧面顶点的数量
    private static int sizeOfOpenCylinderInVertices(int numPoints){

        return (numPoints +1) * 2;

    }


    //生成冰球的方法

    static GeneratedData createPuck(Geometry.Cylinder puck, int numPoints){

        //这个冰球有多少个顶点
        int size = sizeOfCircleInVertices(numPoints) +
                sizeOfOpenCylinderInVertices(numPoints);
        //创建了一个包含顶点数量得数组
        ObjectBuilder builder = new ObjectBuilder(size);

        //冰球的顶部
        Geometry.Circle puckTop = new Geometry.Circle(
                puck.center.translateY(puck.height / 2f), puck.radius);

        builder.appendCircle(puckTop,numPoints);
        //冰球的侧面
        builder.appendOpenCylinder(puck,numPoints);

        return builder.build();

    }

    //用三角形扇构造冰球的顶部  把数据写入verdata 数组
    private void appendCircle(Geometry.Circle circle,int numPoints){

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);


        //扇形的圆心
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        for (int i = 0; i <= numPoints; i++) {

            float angleInRadius = ((float)i / (float)numPoints)
                    * (float)(Math.PI * 2f);
            vertexData[offset++] = circle.center.x +
                    circle.radius * (float) Math.cos(angleInRadius);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z +
                    circle.radius * (float)Math.sin(angleInRadius);

        }


        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN,startVertex,numVertices);
            }
        });

    }

    //用三角形带构建圆柱体侧面
    private void appendOpenCylinder(Geometry.Cylinder cylinder, final int numPoints){

        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        float yStart = cylinder.center.y - (cylinder.height / 2f);
        float yEnd = cylinder.center.y + (cylinder.height / 2f);

        for (int i = 0; i <= numPoints; i++) {

            float angleInRadians = ((float)i / (float)numPoints)
                    * ((float)Math.PI *2f);

            float xPosition = cylinder.center.x +
                    cylinder.radius * (float) Math.cos(angleInRadians);

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


    private List<DrawCommand> drawList = new ArrayList<>();
    //绘制命令的接口
    static interface DrawCommand{

        void draw();

    }



    static GeneratedData createMallet(Geometry.Point center,float radius,float height,int numPoint){

        int size = sizeOfCircleInVertices(numPoint) * 2 +
                sizeOfOpenCylinderInVertices(numPoint) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);
        float baseHeight = height * 0.25f;

        Geometry.Circle baseCircle =
                new Geometry.Circle(center.translateY(-baseHeight), radius);
        Geometry.Cylinder baseCylinder =
                new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight);

        builder.appendCircle(baseCircle,numPoint);
        builder.appendOpenCylinder(baseCylinder,numPoint);

        float handlerHeight = height *0.75f;
        float handRadius = radius / 3f;

        Geometry.Circle handleCircle =
                new Geometry.Circle(center.translateY(height * 0.5f), handRadius);
        Geometry.Cylinder handleCylinder =
                new Geometry.Cylinder(handleCircle.center.translateY(-handlerHeight / 2f), handRadius, handlerHeight);

        builder.appendCircle(handleCircle,numPoint);
        builder.appendOpenCylinder(handleCylinder,numPoint);


        return builder.build();

    }




}
