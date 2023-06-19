package com.IsraelAdewuyi.UBB.universitybookingbot.Actions;

import com.IsraelAdewuyi.UBB.universitybookingbot.Models.Reservation;

public class ValidateTimeSlot {
    public static boolean validateTimeSlot(String timeSlot){
        if(timeSlot.length() != 11){
            return false;
        }

        if(timeSlot.charAt(0) > '9' || timeSlot.charAt(0) < '0')return false;
        if(timeSlot.charAt(1) > '9' || timeSlot.charAt(1) < '0')return false;
        if(timeSlot.charAt(4) > '9' || timeSlot.charAt(4) < '0')return false;
        if(timeSlot.charAt(3) > '9' || timeSlot.charAt(3) < '0')return false;

        if(timeSlot.charAt(6) > '9' || timeSlot.charAt(6) < '0')return false;
        if(timeSlot.charAt(7) > '9' || timeSlot.charAt(7) < '0')return false;
        if(timeSlot.charAt(9) > '9' || timeSlot.charAt(9) < '0')return false;
        if(timeSlot.charAt(10) > '9' || timeSlot.charAt(10) < '0')return false;

        int startHour = Integer.parseInt(timeSlot.substring(0, 2));
        int startMinute = Integer.parseInt(timeSlot.substring(3, 5));
        int endHour = Integer.parseInt(timeSlot.substring(6,8));
        int endMinute = Integer.parseInt(timeSlot.substring(9));


        boolean validTimeRange = isValidTimeRange(startHour, startMinute, endHour, endMinute) ;

        boolean validTimeDuration = isValidTimeDuration(startHour, startMinute, endHour, endMinute);


        System.out.println("This is the current time" +  startHour + startMinute + endHour + endMinute);

        return validTimeDuration && validTimeRange;
    }

    private static boolean isValidTimeRange(int startHour, int startMinute, int endHour, int endMinute) {
        boolean isStartHourValid = startHour >= 0 && startHour <= 23;
        boolean isStartMinuteValid = startMinute >= 0 && startMinute <= 59;
        boolean isEndHourValid = endHour >= 0 && endHour <= 23;
        boolean isEndMinuteValid = endMinute >= 0 && endMinute <= 59;

        return isStartHourValid && isStartMinuteValid && isEndHourValid && isEndMinuteValid;
    }

    private static boolean isValidTimeDuration(int startHour, int startMinute, int endHour, int endMinute){
        // Check the hours first
        if (startHour < endHour) {
            return true;  // Start hour is less than end hour
        } else if (startHour == endHour) {
            // Start hour is equal to end hour, compare the minutes
            return startMinute < endMinute;
        } else {
            return false;  // Start hour is greater than end hour
        }
    }

    public static Reservation getTime(String timeSlot){
        int startHour = Integer.parseInt(timeSlot.substring(0, 2));
        int startMinute = Integer.parseInt(timeSlot.substring(3, 5));
        int endHour = Integer.parseInt(timeSlot.substring(6,8));
        int endMinute = Integer.parseInt(timeSlot.substring(9));

        return new Reservation(startHour, endHour, startMinute, endMinute);
    }
}