package com.nwuking.ytalk;


import android.content.Context;
import android.widget.Toast;

/**
 * @desc   对android Toast的简单封装
 *
 */

public final class ToastUtils {
    public static void showShortToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
