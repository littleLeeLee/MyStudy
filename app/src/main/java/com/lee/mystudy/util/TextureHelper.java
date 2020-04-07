package com.lee.mystudy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.util.Log;


import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class TextureHelper {

    public static String TAG = "TextureHelper";
      public static int loadTextrue(Context context,int resourceId){

          int[] textrueObjectId = new int[1];
          glGenTextures(1,textrueObjectId,0);
          if(textrueObjectId[0] == 0){
              //失败
             Log.d(TAG,"fail");
              return 0;

          }
          BitmapFactory.Options options = new BitmapFactory.Options();
          //不要缩放
          options.inScaled = false;

          Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
          if(bitmap == null){
              Log.d(TAG,"decode bitmap failed");

              glDeleteTextures(1,textrueObjectId,0);
              return 0;
          }
         Log.d(TAG,"WIDTH::"+bitmap.getWidth());
          //2d方式绑定纹理
          glBindTexture(GL_TEXTURE_2D,textrueObjectId[0]);
          //缩小的情况下 使用三线性过滤
          glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
          //放大情况下使用双线性过滤
          glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
          //告诉OpenGL 输入bitmap定义的位图数据，并把它复制到当前绑定的w纹理对象
          texImage2D(GL_TEXTURE_2D,0,bitmap,0);
          //回收掉位图
          bitmap.recycle();
          //生成的级别
        glGenerateMipmap(GL_TEXTURE_2D);
          //解除纹理绑定 防止其他地方意外调用  传入0即可解除绑定
          glBindTexture(GL_TEXTURE_2D,0);
          return textrueObjectId[0];
      }
}
