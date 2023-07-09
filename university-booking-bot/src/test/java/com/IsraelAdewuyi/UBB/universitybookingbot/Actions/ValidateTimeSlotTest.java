package com.IsraelAdewuyi.UBB.universitybookingbot.Actions;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ValidateTimeSlotTest {

    ValidateTimeSlot myValidator = new ValidateTimeSlot();

    @Test
    void splitValidator(){
        String response = myValidator.validateTimeDuration("2023:07:03-2023:08:09");
        assertEquals("invalid", response);
    }

    @Test
    void nullValidator(){
        String response = myValidator.validateTimeDuration(" ");
        assertEquals("invalid", response);
    }

    @Test
    void randomStringValidator(){
        String response = myValidator.validateTimeDuration("  -  ");
        assertEquals("invalid", response);
    }

    @Test
    void validStringValidator(){
        String response = myValidator.validateTimeDuration("2024 05 20 20:00 - 2024 05 20 21:00");
        assertEquals("valid", response);
    }

}