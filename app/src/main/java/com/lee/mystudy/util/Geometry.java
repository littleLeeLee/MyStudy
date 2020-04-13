package com.lee.mystudy.util;



//几何构造类
public class Geometry {


    public static boolean intersects(Sphere sphere, Ray ray) {

        //计算球心与射线的距离，如果距离小于球的半径 那么球体就与射线相交
        return distanceBetween(sphere.center,ray) < sphere.radius;

    }

    private static float distanceBetween(Point point, Ray ray) {

        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector),point);

        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();

        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;


    }

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

        public Point translate(Vector vector) {

            return new Point(
                    x + vector.x,
                    y + vector.y,
                    z + vector.z);

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

    //射线
    public static class Ray{

            //一条射线包含一个起点和一个表示射线方向的向量
        public Point point;
        public Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }

//射线方向的向量
    public static class Vector{
        public float x,y,z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length(){
            //利用勾股定理返回向量的长度
            return (float) Math.sqrt(x * x + y * y + z * z );

        }

        //计算两个向量的交叉乘积
        public Vector crossProduct(Vector other){

            return new Vector(
                    (y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x));

        }

    public float dotProduct(Vector other) {

        return x * other.x
                + y * other.y
                + z * other.z;


    }

    public Vector scale(float f){

            return new Vector(x *f,y * f,z * f);

    }

}
    //创建从近点指向远点的向量
    public static Vector vectorBetween(Point from, Point to) {

        return new Vector(to.x - from.x,
                to.y - from.y,
                to.z - from.z);

    }

    //木槌的包围球类
    public static class Sphere{

        public Point center;
        public float radius;

        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    //木槌移动平面类

    public  static class Plane{

        public  Point point;
        public Vector normal;

        public Plane(Point point, Vector normal) {

            this.point = point;
            this.normal = normal;

        }
    }


    public static Point intersectionPoint(Ray ray,Plane plane){

        Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);
        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;

    }

}
