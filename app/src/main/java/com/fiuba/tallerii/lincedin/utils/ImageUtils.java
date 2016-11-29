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
        int size = aBitmap.getRowBytes() * aBitmap.getHeight();
        ByteBuffer b = ByteBuffer.allocate(size);

        aBitmap.copyPixelsToBuffer(b);
        byte[] bytes;

        bytes = b.array();

        return bytes;
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
