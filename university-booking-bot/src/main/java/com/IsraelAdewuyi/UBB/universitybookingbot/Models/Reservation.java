package com.IsraelAdewuyi.UBB.universitybookingbot.Models;

public class Reservation {
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;

    public Reservation(int startHour, int endHour, int startMinute, int endMinute) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }
}
