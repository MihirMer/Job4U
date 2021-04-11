package com.almightymm.job4u.latex;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.almightymm.job4u.fragment.ProfileFragment;

/**
 * Created by alberto on 16/02/16.
 */
public class PreferenceHelper {

    public static String getOutputFolder(Context ctx) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(ctx);
        return s.getString(ProfileFragment.OUTPUT_FOLDER, "");
    }

    public static String getImageFolder(Context ctx) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(ctx);
        return s.getString(ProfileFragment.IMAGES_FOLDER, "");
    }

    public static String getServerAddress(Context ctx) {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(ctx);
        return s.getString(ProfileFragment.SERVER_ADDRESS, "");
    }

}
