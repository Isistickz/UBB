package com.IsraelAdewuyi.UBB.universitybookingbot.Actions;

import com.IsraelAdewuyi.UBB.universitybookingbot.Models.Reservation;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateTimeSlot {
    public static String validateTimeDuration(String timeDuration) {
        String[] parts = timeDuration.split(" - ");

        if (parts.length != 2) {
            return "invalid";
        }

        System.out.println("Got here " + parts[0] + " " + parts[1]);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");

        try {
            LocalDateTime startDateTime = LocalDateTime.parse(parts[0], formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(parts[1], formatter);

            String formattedDateTime = startDateTime.format(formatter);
            String formattedEndTime = endDateTime.format(formatter);

            System.out.println(formattedDateTime + " " +  formattedEndTime);

            LocalDateTime currentTime = LocalDateTime.now();

            boolean validStartTime = startDateTime.isAfter(currentTime) || startDateTime.isEqual(currentTime);

            if(startDateTime.isBefore(endDateTime) && validStartTime){
                return "valid";
            }
            else if(!validStartTime){
                return "curTime";
            }
            else if(!startDateTime.isBefore(endDateTime)){
                return "startTimeBeforeEndTime";
            }
            else return "invalid";

//            return startDateTime.isBefore(endDateTime) && validStartTime;
        } catch (DateTimeParseException e) {
            return "invalid";
        }
    }
}