package com.IsraelAdewuyi.UBB.universitybookingbot.Service;

import com.IsraelAdewuyi.UBB.universitybookingbot.Actions.ValidateTimeSlot;
import com.IsraelAdewuyi.UBB.universitybookingbot.Booking.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Booking.BookingsRunner;
import com.IsraelAdewuyi.UBB.universitybookingbot.Configuration.BotConfiguration;
import com.IsraelAdewuyi.UBB.universitybookingbot.Models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RestController
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfiguration botConfiguration;

    @Autowired
    private BookingService bookingService;

    public TelegramBot(BotConfiguration botConfiguration){
        this.botConfiguration = botConfiguration;
    }
    @Override
    public String getBotUsername() {
        return botConfiguration.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            long chatId = callbackQuery.getMessage().getChatId();

            long startHour = Integer.parseInt(callbackData.substring(0, 2));
            long endHour = Integer.parseInt(callbackData.substring(5, 7));

            bookingService.deleteByTime(startHour, endHour);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText(startHour + " " + endHour);

            sendMessage(response);
        }

        else if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();

            long chatID = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            String lastName = update.getMessage().getChat().getLastName();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatID, firstName, lastName);
                    break;
                case "/cancel":
                    cancelCommandReceived(chatID, firstName, lastName);
                    break;
                case "/view":
                    viewCommandReceived(chatID, firstName, lastName);
                    break;
                case "/book":
                    bookCommandReceived(chatID, firstName, lastName);
                    break;
                default:
                    boolean validTimeSlot = validateTimeSlot(messageText);

                    if(validTimeSlot){
                        Reservation currentReservation = ValidateTimeSlot.getTime(messageText);

                        boolean isAvailable = bookingService.isRoomAvailable(301,
                                currentReservation.getStartHour(),
                                currentReservation.getEndHour());

                        if(isAvailable){
                            bookingService.bookRoom(301,
                                    currentReservation.getStartHour(),
                                    currentReservation.getEndHour());
                        }

                        SendMessage reply = new SendMessage();
                        reply.setChatId(chatID);
                        if(isAvailable){
                            reply.setText("Room 301 has been booked for you");
                        }
                        else{
                            reply.setText("Room 301 is not available at the selected time slot.");
                        }
                        sendMessage(reply);
                    }
                    else{
                        String messageReply = "You have entered an unrecognized command.";
                        SendMessage message = new SendMessage(String.valueOf(chatID), messageReply);
                        sendMessage(message);
                    }


            }

        }
    }

    private boolean validateTimeSlot(String timeslot){
        return ValidateTimeSlot.validateTimeSlot(timeslot);
    }

    @RequestMapping("/start")
    private void startCommandReceived(long chatID, String firstName, String lastName) {
        String answer = "Hi, " + firstName + " , " + lastName + ", nice to meet you! " +
                "\nHow can I help you??" +
                "\nPress \\book to book a room" +
                "\nPress \\view to view all your current bookings" +
                "\nPress \\cancel to cancel an existing booking";


        sendMessage(chatID, answer);
    }
    @RequestMapping("/cancel")
    private void cancelCommandReceived(long chatID, String firstName, String lastName){
        String reply = firstName + ", are you sure you want to cancel your room reservation? If yes, pick a time slot";

        List<Booking> bookings =  bookingService.getBookingsByRoomId(301l);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlinee = new ArrayList<>();

        for(Booking book : bookings){
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton newKey = new InlineKeyboardButton();
            String time = book.getStartTime() + " - " +  book.getEndTime() + " at " + book.getRoomId();
            newKey.setText(time);
            newKey.setCallbackData(time);
            rowInline.add(newKey);
            rowsInlinee.add(rowInline);
        }

        keyboardMarkup.setKeyboard(rowsInlinee);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(reply);
        sendMessage.setReplyMarkup(keyboardMarkup);

        sendMessage(sendMessage);
    }
    @RequestMapping("/view")
    private void viewCommandReceived(long chatID, String firstName, String lastName){
        String reply = firstName + ", Here are the list of available rooms: ";

        List<Booking> bookings =  bookingService.getBookingsByRoomId(301l);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlinee = new ArrayList<>();

        for(Booking book : bookings){
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton newKey = new InlineKeyboardButton();
            String time = book.getStartTime() + " - " +  book.getEndTime() + " at " + book.getRoomId();
            newKey.setText(time);
            newKey.setCallbackData(time);
            rowInline.add(newKey);
            rowsInlinee.add(rowInline);
        }

        keyboardMarkup.setKeyboard(rowsInlinee);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(reply);
        sendMessage.setReplyMarkup(keyboardMarkup);

        sendMessage(sendMessage);
    }
    private void bookCommandReceived(long chatID, String firstName, String lastName){
        SendMessage message = new SendMessage(String.valueOf(chatID), "Enter start time and end time in the " +
                "24 hr format: \nFor example : 12:00 13:00");
        sendMessage(message);
    }
    private void sendMessage(long chatID, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        }catch (TelegramApiException e){

        }
    }
    private void sendMessage(SendMessage message){
        try {
            execute(message);
        }catch (TelegramApiException e){

        }
    }
}