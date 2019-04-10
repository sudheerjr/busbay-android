package com.afh.busbay.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.afh.busbay.models.User;

import static com.afh.busbay.utils.AppConstants.PREF_FILE;
import static com.afh.busbay.utils.AppConstants.PREF_USER_EMAIL;
import static com.afh.busbay.utils.AppConstants.PREF_USER_ID;
import static com.afh.busbay.utils.AppConstants.PREF_USER_NAME;
import static com.afh.busbay.utils.AppConstants.PREF_USER_TYPE;

public class AppUtils {
    public static void saveUserToSharedPref(User user, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USER_ID, user.getUserId());
        editor.putString(PREF_USER_NAME, user.getUsername());
        editor.putString(PREF_USER_EMAIL, user.getEmail());
        editor.putInt(PREF_USER_TYPE, user.getUserType());
        editor.apply();
    }

    public static boolean isCurrentUserFaculty(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        int userType = sharedPreferences.getInt(PREF_USER_TYPE, 0);
        return userType != 0;
    }

    public static String getCurrentUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(PREF_USER_NAME, "");
        return username;
    }
}
