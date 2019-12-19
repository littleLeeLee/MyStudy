package com.lee.mystudy.jnitest;

public class MyJniLibs {

    static {

        System.loadLibrary("myJni");

    }


    public  static native String loadString();


    public static native void rgb2yuv();
 }
