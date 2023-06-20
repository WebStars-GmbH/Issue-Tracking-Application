package com.example.application.data.service;

public class TimeUtil {
    public String millisecondsToTimeFormat(long milliseconds){
        if (milliseconds == 0) return "";
        int seconds = (int)(milliseconds / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int days = hours / 24;
        return (days + " days, " + (hours%24) + " hours, " + (minutes%60) + " minutes, " + (seconds%60) + " seconds");
    }
}
