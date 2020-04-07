package com.lee.mystudy.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showToast(Context context ,String string){

        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();

    }
}
