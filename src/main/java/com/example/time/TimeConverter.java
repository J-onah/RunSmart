package com.example.time;

public class TimeConverter {
    // Converting minutes in hours
    public static String convertMinutesToHourMinutes(int input) {
        String displayMsg = "";
        int hour = (int) input / 60;
        int minutes = input % 60;
        displayMsg = Integer.toString(hour) + ":" + Integer.toString(minutes);
        return displayMsg;
        }
}
