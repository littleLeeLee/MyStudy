package com.lee.mystudy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;



import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    private static String TAG = "WYLEE";

    private static int DEFAULT_HEIGHT = 480;

    private static int DEFAULT_WIDTH = 640;

    private static int DEFAULT_ANGLE = 90;
    private static int DEFAULT_REGIST_ANGLE = 0;
    // FaceAuthResult

    /**
     * 将图片转成byte 数组
     */
    private static byte[] imageToBytes(String picPath) {
        byte[] result = null;
        File file = new File(picPath);
        byte[] read = new byte[1024];
        if (!file.exists()) {
            LogUtil.INSTANCE.d("文件不存在");
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream bytesOpt = new ByteArrayOutputStream();
            int len = 0;
            while ((len = fileInputStream.read(read)) != -1) {

                bytesOpt.write(read, 0, len);

            }
            result = bytesOpt.toByteArray();
            fileInputStream.close();
            bytesOpt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }





    /**
     * 将jpeg 图片转为 ARGB bytes
     * @param picPath
     * @return
     */
    private static byte[] JPEGtoBytes(String picPath){

        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置 所需要的格式
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
        //ARGB 每个像素点为4个字节 所以要 * 4
        byte[] bytes = new byte[bitmap.getWidth() * bitmap.getHeight()*4];

        //行数
        for (int i = 0; i < bitmap.getHeight(); i++) {
            //列数
            for (int j = 0; j < bitmap.getWidth(); j++) {

                //当前点的像素
                int pixel = bitmap.getPixel(j, i);

                //提取像素中的4种颜色
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);


                //每次填充4个位置的颜色数据
                bytes[(i * bitmap.getWidth() + j) * 4 + 0] = (byte) alpha;
                bytes[(i * bitmap.getWidth() + j) * 4 + 1] = (byte) red;
                bytes[(i * bitmap.getWidth() + j) * 4 + 2] = (byte) green;
                bytes[(i * bitmap.getWidth() + j) * 4 + 3] = (byte) blue;

                //   break labe;

            }

        }

        return bytes;

    }




    /**
     * 顺时针旋转90度的yuv
     */
    private static byte[] rotateYUVDegree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    private static byte[] rotateYuvDegree180(byte[] res,int imageWidth,int imageHeight){

        byte[] des = new byte[imageHeight * imageWidth * 3 / 2];
        int k = 0;

        //先旋转Y
        for (int i = imageHeight; i > 0; i--) {

            for (int j = 0; j < imageWidth; j++) {

                des[k] = res[imageWidth * i -j -1];
                k++;
            }

        }
        //再旋转UV


        for (int i = imageHeight / 2; i >0 ; i--) {

            for (int j = 0; j < imageWidth; j+=2) {

                des[k] =  res[imageHeight * imageWidth + i * imageWidth -j -2]  ;
                k++;
                des[k] = res[imageHeight * imageWidth + i * imageWidth -j -1]  ;
                k++;

            }

        }
 /*       int imgSize = imageHeight * imageWidth;
        int len = res.length;
        int i = 0;
        for (i = len - 1; i >= imgSize; i -= 2) {
            des[k++] = res[i - 1];
            des[k++] = res[i ];

        }*/


        return des;

    }


    private static String saveRegisterUserImage(byte[] nv21, Context context) {
        String imagePath = null;
        try {
            imagePath = context.getFilesDir().getAbsolutePath() + "/" + System.currentTimeMillis()  + ".jpg";

            BitmapFactory.Options options = new BitmapFactory.Options();
            //设置 所需要的格式
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(nv21,0,nv21.length,options);

            // Bitmap bm = BitmapFactory.decodeByteArray()
            //      .convertNv21ToBmp(nv21, dir, width, height);
            saveBitmap(bitmap, imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }





    public static void ARGB2NV21(String picPath){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);

        byte[] nv21 = getNV21(480, 640, bitmap);

        LogUtil.INSTANCE.d("NV size ::"+ nv21.length);
        //  rotateYUV240SP(nv21,des,480,640);
        byte[] bytes = rotateYUVDegree90(nv21, 480, 640);
        //  byte[] bytes = rotateYuvDegree180(nv21, 480, 640);
        saveFile(bytes,"/sdcard/registpic/","test.yuv");


    }

    /**
     *  RGB 转为 nv21
     * @param inputWidth
     * @param inputHeight
     * @param scaled rgb 位图
     * @return
     */
    private static byte [] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {

        int [] argb = new int[inputWidth * inputHeight ];
        //扫描所有的像素点
        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

        byte [] yuv = new byte[inputWidth*inputHeight*3/2];
        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

        scaled.recycle();

        return yuv;
    }


    private static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uvIndex = frameSize;

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;

                // well known RGB to YUV algorithm
                Y = ( (  66 * R + 129 * G +  25 * B + 128) >> 8) +  16;
                U = ( ( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128;
                V = ( ( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128;

                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                //vu数量是y的1/2
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (byte)((V<0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (byte)((U<0) ? 0 : ((U > 255) ? 255 : U));
                }

                index ++;
            }
        }
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void saveFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    public static boolean saveBitmap(Bitmap bitmap, String imagePath) {
        if (bitmap == null) {
            return false;
        } else {

            File file = new File(imagePath);
            File parentFile = file.getParentFile();
            FileOutputStream fos = null;
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                boolean var5 = true;
                return var5;
            } catch (FileNotFoundException var17) {
                var17.printStackTrace();
            } catch (IOException var18) {
                var18.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException var16) {
                        Log.e(TAG, "FileOutputStream close failed");
                    }
                }

            }
            return false;

        }
    }

}
