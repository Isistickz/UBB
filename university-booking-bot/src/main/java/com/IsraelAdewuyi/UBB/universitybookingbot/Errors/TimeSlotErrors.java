package com.IsraelAdewuyi.UBB.universitybookingbot.Errors;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TimeSlotErrors extends DefaultAbsSender {
    public TimeSlotErrors(DefaultBotOptions options, String botToken) {
        super(options, botToken);
    }

    public void pastStartTime(long chatID) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText("You cannot book a time in the past");

        execute(sendMessage);
    }

    public void startTimeInvalid(long chatID) throws TelegramApiException{
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText("Students cannot book any time duration before 7pm");

        execute(sendMessage);
    }

    public void startTimeBeforeEndTime(long chatID) throws TelegramApiException{
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText("Start time is before end time");

        execute(sendMessage);
    }



//    TelegramBot myBot = new TelegramBot();
}
