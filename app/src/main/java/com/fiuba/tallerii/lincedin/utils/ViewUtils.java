package com.fiuba.tallerii.lincedin.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUtils {
    public static void setToast(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null)
            v.setGravity(Gravity.CENTER);
        toast.show();
    }
}
