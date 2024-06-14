package com.example.alarmapp;

import java.util.concurrent.atomic.AtomicInteger;

public class Alarm {
    private int id;
    private int hour;
    private int minute;
    private boolean isEnabled;

    public Alarm(int id, int hour, int minute, boolean isEnabled) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.isEnabled = isEnabled;
    }

    public Alarm(int hour, int minute, boolean isEnabled) {
        this(0, hour, minute, isEnabled);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getFormattedTime() {
        String amPm = (hour >= 12) ? "PM" : "AM";
        int formattedHour = (hour > 12) ? hour - 12 : hour;
        formattedHour = (formattedHour == 0) ? 12 : formattedHour;
        return String.format("%02d:%02d %s", formattedHour, minute, amPm);
    }
}
