package com.lee.mystudy.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResouceReader {

    public static String readTextFileFromResouce(Context context,int resouceId){

        StringBuilder stringBuilder = new StringBuilder();
        try {
        InputStream inputStream = context.getResources().openRawResource(resouceId);

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String nextLine;
        while((nextLine = bufferedReader.readLine()) != null){
            stringBuilder.append(nextLine);
            stringBuilder.append('\n');
        }


        } catch (IOException e) {
            e.printStackTrace();

        }


        return stringBuilder.toString();
    }
}
