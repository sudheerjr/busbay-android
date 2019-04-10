package com.afh.busbay.utils;

import java.time.LocalDate;

public class FirebaseUtils {
    public static final String BRANCH_USERS = "/users/";
    public static final String BRANCH_BUS_LOCATION = "/bus_location/";

    public static String getFormattedDate() {
        LocalDate date = LocalDate.now();
        String today = date.toString();
        String[] dateList = today.split("-");
        String year = dateList[0];
        String month = dateList[1];
        String day = dateList[2];
        today = "date_" + month + "_" + day + "_" + year;
        return today;
    }

    public static String getFeeDetailsPath(String userId) {
        String feeDetailsPath = "/fee_details/" + userId;
        return feeDetailsPath;
    }

    public static String getUserExpensesPath(String userId) {
        String userExpensesPath = "/user_expenses/" + userId;
        return userExpensesPath;
    }
}
