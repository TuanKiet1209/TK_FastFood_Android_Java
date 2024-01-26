package com.example.foodorder.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static final String DEFAULT_FORMAT_DATE = "dd-MM-yyyy,hh:mm a";

    public static String convertTimeStampToDate(long timeStamp) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE, Locale.ENGLISH);
            Date date = (new Date(timeStamp));
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
