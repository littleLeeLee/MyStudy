//
// Created by Lee on 2019/10/29.
//

#ifndef MYSTUDY_COMMTEST_H

#include <jni.h>
#include <string>
#include <stdio.h>
#include <android/log.h>

#define LOG_D(...)  __android_log_print(ANDROID_LOG_DEBUG, "wylee_jni", __VA_ARGS__)



void printLog(int num){

    LOG_D("NUM = %d ",num);

}


void rgb2yuv(unsigned int* buffer){

    int* buff = (int *)(malloc(1024));

    LOG_D("buffer size = %d", sizeof(buff));


}

void testFile(){

   FILE * file =  fopen("/sdcard/facepic/jniFile.txt","w");

   if(file == NULL){

       LOG_D("create file failed");

   } else{

       LOG_D("create file success");
       fputs("testJni",file);
       fclose(file);
   }

   int a = 1024;
   int *p;
   p = &a;

   long *i ,*j;

    LOG_D("指针地址为：%x",*p);
}


#endif //MYSTUDY_COMMTEST_H
