package com.example.ontime;

import java.io.Serializable;

class Event implements Serializable {
    int id;
    int ownerId;
    String eventName;
    String startDate;
    String endDate;
    String time;
    int repeatWeekly;
    String weeklySchedule;  // binary string
    String locationName;
    Double lat;
    Double lng;
    String code;
    String days;            // weekly schedule in days (Mon Tues etc)
    Boolean isPrivate;


    Event(int id, int ownerId, String eventName, String startDate, String endDate, String weeklySchedule, String time, int repeatWeekly, String locationName, Double lat, Double lng, String code, Boolean isPrivate) {
        // convert weekly schedule from binary string to a list of days (1000110 -> Sun, Thu, Fri)
        String days = "";
        if (weeklySchedule.charAt(0) == '1') {
            days += "Sun ";
        }
        if (weeklySchedule.charAt(1) == '1') {
            days += "Mon ";
        }
        if (weeklySchedule.charAt(2) == '1') {
            days += "Tue ";
        }
        if (weeklySchedule.charAt(3) == '1') {
            days += "Wed ";
        }
        if (weeklySchedule.charAt(4) == '1') {
            days += "Thu ";
        }
        if (weeklySchedule.charAt(5) == '1') {
            days += "Fri ";
        }
        if (weeklySchedule.charAt(6) == '1') {
            days += "Sat";
        }

        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        if (time.length() >5) {
            time = time.substring(0, 5);
            // Convert 24h time
            int hours = Integer.parseInt(time.substring(0,2));
            String minutes = time.substring(3,5);

            // TODO: convert time correctly
            if (hours > 12) {
                time = (hours % 12) + ":" + minutes + " PM";
            } else if (hours == 0 || hours == 12) {
                time = "12:" + minutes + "PM";
            } else {
                time = hours + ":" + minutes + " AM";
            }
        }
        this.time = time;
        this.weeklySchedule = weeklySchedule;
        this.days = days;
        this.id = id;
        this.ownerId = ownerId;
        this.repeatWeekly = repeatWeekly;
        this.locationName = locationName;
        this.lat = lat;
        this.lng = lng;
        this.code = code;
        this.isPrivate = isPrivate;
    }
}