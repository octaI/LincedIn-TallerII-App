package com.fiuba.tallerii.lincedin.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String parseToDatetime(int dayNumber, int monthNumber, int yearNumber) {
        String day = dayNumber >= 10 ? String.valueOf(dayNumber) : "0" + String.valueOf(dayNumber);
        String month = monthNumber >= 10 ? String.valueOf(monthNumber) : "0" + String.valueOf(monthNumber);
        String year = String.valueOf(yearNumber);

        return year + "-" + month + "-" + day + " 00:00:00";
    }

    public static String parseToLocalDate(Context context, int dayNumber, int monthNumber, int yearNumber) {
        String day = dayNumber >= 10 ? String.valueOf(dayNumber) : "0" + String.valueOf(dayNumber);
        String month = (monthNumber + 1) >= 10 ? String.valueOf(monthNumber + 1) : "0" + String.valueOf(monthNumber + 1);   // January = 0
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

    public static String parseDatetimeToLocalDate(Context context, String datetime) {
        return parseToLocalDate(
                context,
                Integer.valueOf(extractDayOfMonthFromDatetime(datetime)),
                Integer.valueOf(extractMonthFromDatetime(datetime)),
                Integer.valueOf(extractYearFromDatetime(datetime))
        );
    }

    public static String getActualDatetime() {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(new Date());
    }

    public static String parseDatetimeToDateWithoutTime(String datetime) {
        return datetime.replaceAll(" .*", "");
    }

    public static String parseDateWithoutTimeToDatetime(String dateWithoutTime) {
        return dateWithoutTime + " 00:00:00";
    }

    public static String extractYearFromDatetime(String datetime) {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final Calendar c = Calendar.getInstance();
        Integer yearInt = 0;
        try {
            c.setTime(df.parse(datetime));
            yearInt = c.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yearInt.toString();
    }

    public static String extractMonthFromDatetime(String datetime) {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final Calendar c = Calendar.getInstance();
        Integer monthInt = 0;
        try {
            c.setTime(df.parse(datetime));
            monthInt = c.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthInt.toString();
    }

    public static String extractDayOfMonthFromDatetime(String datetime) {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final Calendar c = Calendar.getInstance();
        Integer dayOfMonthInt = 0;
        try {
            c.setTime(df.parse(datetime));
            dayOfMonthInt = c.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayOfMonthInt.toString();
    }

    public static String getAgeFromDatetime(String datetime) {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final Calendar c = Calendar.getInstance();
        Integer ageInt = 0;
        try {
            c.setTime(df.parse(datetime));

            Calendar dateOfBirth = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dateOfBirth.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)){
                age--;
            }

            ageInt = Integer.valueOf(age);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ageInt.toString();
    }

}
