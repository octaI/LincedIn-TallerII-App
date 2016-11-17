package com.fiuba.tallerii.lincedin.utils;


import android.content.Context;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fiuba.tallerii.lincedin.R;

public class ImageUtils {

    public static void setBase64ImageFromString(Context ctxInUse,String encString, ImageView anImage){
        byte[] decodedString = decodeBase64String(encString);
        Glide
                .with(ctxInUse)
                .load(decodedString)
                .placeholder(R.mipmap.ic_loading)
                .into(anImage);
    }

    public static byte[] decodeBase64String(String encString){
        byte[] decodedString = Base64.decode(encString,Base64.DEFAULT);
        return decodedString;
    }
}
