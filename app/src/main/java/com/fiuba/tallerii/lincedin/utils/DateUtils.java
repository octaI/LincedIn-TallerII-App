package com.fiuba.tallerii.lincedin.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String parseToLocalDate(Context context, int dayNumber, int monthNumber, int yearNumber) {
        String day = dayNumber >= 10 ? String.valueOf(dayNumber) : "0" + String.valueOf(dayNumber);
        String month = monthNumber >= 10 ? String.valueOf(monthNumber) : "0" + String.valueOf(monthNumber);
        String year = String.valueOf(yearNumber);

        String localDate = day + "/" + month + "/" + year;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(localDate);
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            localDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return localDate;
    }

}
