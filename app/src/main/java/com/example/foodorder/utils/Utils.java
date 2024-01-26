package com.example.foodorder.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

public class Utils {

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
