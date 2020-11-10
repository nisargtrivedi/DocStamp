package com.docstamp.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static String DaysAgo(String date) {
        String time="";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date past = format.parse(date);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if (seconds < 60) {
                System.out.println(seconds + " seconds ago");
                time=seconds + " seconds ago";
            } else if (minutes < 60) {
                System.out.println(minutes + " minutes ago");
                time=minutes + " minutes ago";
            } else if (hours < 24) {
                System.out.println(hours + " hours ago");
            } else {
                time=days + " days ago";
                System.out.println(days + " days ago");
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return time;
    }
}
