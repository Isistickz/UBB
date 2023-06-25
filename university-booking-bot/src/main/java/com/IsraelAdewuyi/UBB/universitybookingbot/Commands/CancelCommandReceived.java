package com.IsraelAdewuyi.UBB.universitybookingbot.Commands;

import com.IsraelAdewuyi.UBB.universitybookingbot.Controller.BookingController;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CancelCommandReceived implements CommandReceived<Long, String> {
    @Autowired
    private BookingController bookingController;


    @Override
    public SendMessage executeCommand(Long chatID, String firstName) {
        String reply = firstName + ", are you sure you want to cancel your room reservation? If yes, pick a time slot";

        Student student = bookingController.getStudentByFirstName(firstName);
        long id = student.getId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Booking> bookingList = bookingController.getBookingsByStudent(id);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlinee = new ArrayList<>();

        for (Booking booking : bookingList) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton newKey = new InlineKeyboardButton();
            String roomName = booking.getStartTime().format(formatter)
                    + " at "
                    + booking.getRoom().getRoomName();
            newKey.setText(roomName);
            newKey.setCallbackData("CANCL " + booking.getId());
            rowInline.add(newKey);
            rowsInlinee.add(rowInline);
        }

        keyboardMarkup.setKeyboard(rowsInlinee);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(reply);
        sendMessage.setReplyMarkup(keyboardMarkup);



        return  sendMessage;
    }
}
