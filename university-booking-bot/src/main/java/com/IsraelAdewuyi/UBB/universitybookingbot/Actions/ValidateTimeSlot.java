package com.IsraelAdewuyi.UBB.universitybookingbot.Actions;

import com.IsraelAdewuyi.UBB.universitybookingbot.Models.Reservation;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateTimeSlot {
    public static boolean validateTimeDuration(String timeDuration) {
        String[] parts = timeDuration.split(" - ");

        if (parts.length != 2) {
            return false;
        }

        System.out.println("Got here");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            LocalDateTime startDateTime = LocalDateTime.parse(parts[0], formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(parts[1], formatter);

            String formattedDateTime = startDateTime.format(formatter);
            String formattedEndTime = endDateTime.format(formatter);

            System.out.println(formattedDateTime + " " +  formattedEndTime);

            return startDateTime.isBefore(endDateTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }




    public static Reservation getTime(String timeSlot){
//        int startHour = Integer.parseInt(timeSlot.substring(0, 2));
//        int startMinute = Integer.parseInt(timeSlot.substring(3, 5));
//        int endHour = Integer.parseInt(timeSlot.substring(6,8));
//        int endMinute = Integer.parseInt(timeSlot.substring(9));

//        return new Reservation(startHour, endHour, startMinute, endMinute);
        LocalDateTime Null = null;
        return new Reservation(Null, Null);
    }
}