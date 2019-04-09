package com.afh.busbay.utils;

import java.time.LocalDate;

public class FirebaseConstants {
    public static final String BRANCH_USERS = "/users/";

    public static String getFormattedDate() {
        LocalDate date = LocalDate.now();
        String today = date.toString();
        today = today.replaceAll("-", "_");
        today = "date_" + today;
        return today;
    }
}
