//
// Created by Lee on 2019/10/25.
//

#include <jni.h>
#include <string>
#include "commtest.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_lee_mystudy_jnitest_MyJniLibs_loadString(JNIEnv *env,  jclass clazz){

    std::string hello = "Hello from C++";

    printLog(6);

    int a[10] = {0};
    //整个数组在内存中所占的字节数
    LOG_D("buffer size = %d", sizeof(a));

    return env->NewStringUTF(hello.c_str());

}

extern "C"
JNIEXPORT void JNICALL
Java_com_lee_mystudy_jnitest_MyJniLibs_rgb2yuv(JNIEnv *env, jclass clazz) {

    int a ;
    int *p = &a;
   // a =  a>>4;
    //;
    LOG_D("a = %x",a & 0xff000000);
   /* int k = 2;
    LOG_D("buffer size = %d ", k);
    for (int a = 0;a < 1024 * 1024 *1024;a++){

        k +=a;

    }
    LOG_D("buffer size = %d ", k);*/

   testFile();

}