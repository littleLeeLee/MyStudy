package com.lee.mystudy.util;

public class Geometry {

    //三维场景中的一个点
    public static class Point{

        public float x,y,z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        //沿着Y方向平移distance个距离
        public Point translateY(float distance){
            return new Point(x,y + distance,z);

        }
    }

    //三维场景中的一个圆
    public static class Circle{

        //一个圆是由一个点和半径组成
        public Point center;
        public float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        //缩放scale
        public Circle scale(float scale){

            return new Circle(center,radius * scale);

        }

    }

    //圆柱体得定义
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
