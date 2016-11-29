package com.fiuba.tallerii.lincedin.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.tallerii.lincedin.fragments.ErrorFragment;

public class ViewUtils {
    public static void setToast(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null)
            v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void setSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    public static void setSnackbar(View view, int messageId, int duration) {
        Snackbar.make(view, messageId, duration).show();
    }

    public static void setErrorFragment(int containerId, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(containerId, new ErrorFragment())
                .commit();
    }
}
