package com.fiuba.tallerii.lincedin.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fiuba.tallerii.lincedin.R;

import java.io.ByteArrayOutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class ImageUtils {

    public static byte[] returnByteArrayFromBitmap(Bitmap aBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        aBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return byteArrayOutputStream .toByteArray();
    }

    public static void setImageview(Context ctxInUse, ImageView anImage, Bitmap aBm) {
        anImage.setImageBitmap(aBm);
    }

    public static void setBase64ImageFromString(Context ctxInUse,String encString, ImageView anImage){
        byte[] decodedString = decodeBase64String(encString);
        Glide
                .with(ctxInUse)
                .load(decodedString)
                .placeholder(R.mipmap.ic_loading)
                .centerCrop()
                .into(anImage);
    }

    public static byte[] decodeBase64String(String encString){
        byte[] decodedString = Base64.decode(encString,Base64.DEFAULT);
        return decodedString;
    }

    public static String encodeByteArrayToBase64(byte[] byteArrayImage){
        String encodedString = Base64.encodeToString(byteArrayImage,Base64.DEFAULT);
        return encodedString;
    }
}
