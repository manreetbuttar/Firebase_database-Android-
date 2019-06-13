package com.example.firebasechatfinalmodule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final String TIME_FORMAT = "h:mm a";
    private long UTC_TIMEZONE=1559195367;
    private String OUTPUT_DATE_FORMATE="hh:mm a";


    public  static Integer generateRandomNumber() {
        double randomDouble = Math.random();
        randomDouble = randomDouble * 50 + 1;
        int randomInt = (int) randomDouble;
        return randomInt;
    }

    public  static String getCurrentTimeStamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        return  timestamp;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * ********** get local formatted time *************
     *
     * @param milliseconds
     * @return
     */
    public static String getFormatedLocalGmtTime(long milliseconds) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(milliseconds);
        cal.setTimeZone(Calendar.getInstance().getTimeZone());
        return DateFormat.format("h:mm a", cal).toString();

    }

    public static String getDateFromUTCTimestamp(long mTimestamp) {
        Date date = new Date((long) mTimestamp * 1000);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a");
        return localDateFormat.format(date);
    }



}
