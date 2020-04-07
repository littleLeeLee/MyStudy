package com.lee.mystudy.util;

public class Geometry {


    //点的定义类
    public static class Point{

        public  float x,y,z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        //将Y坐标平移distance 个距离
        public Point translateY(float distance){

                return new Point(x,y+distance,z);

        }
    }

    //圆的定义类

    public static class Circle{

        public Point center;
        public float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
        //缩放圆的半径
        public Circle scale(float scale){

            return new Circle(center,radius * scale);

        }
    }


    //圆柱体的定义
    public static class Cylinder{

        public Point center;
        public float radius;
        public float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

}
