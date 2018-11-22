package com.kangzhan.student.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by kangzhan011 on 2017/6/15.
 */

public class ImageCompress {
    public static Bitmap getSmallBitmap(File imageFile){
        BitmapFactory.Options options= new BitmapFactory.Options();
        //不加载图片，只读信息
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(String.valueOf(imageFile),options);
        options.inSampleSize=calculateInSampleSize(options,480,640);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(String.valueOf(imageFile),options);
    }
    //计算图片的缩放值
    public  static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //把bitmap转换成String
    public static String bitmapToString(File filePath) {
        if(filePath!=null){
            Bitmap bm = getSmallBitmap(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }else {
            return "";
        }
    }
    //将bitmap转化成数组
    public static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }

}
