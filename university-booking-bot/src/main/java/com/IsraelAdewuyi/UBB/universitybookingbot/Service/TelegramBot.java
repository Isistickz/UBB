package com.IsraelAdewuyi.UBB.universitybookingbot.Service;

import com.IsraelAdewuyi.UBB.universitybookingbot.Actions.ValidateTimeSlot;
import com.IsraelAdewuyi.UBB.universitybookingbot.Configuration.BotConfiguration;
import com.IsraelAdewuyi.UBB.universitybookingbot.Controller.BookingController;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Room;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfiguration botConfiguration;

    @Autowired
    private BookingController bookingController;

    public TelegramBot(BotConfiguration botConfiguration) {
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


            String key = callbackData.substring(0, 5);
            SendMessage response = new SendMessage();
            response.setChatId(chatId);

            /*
                This section is playing with the message in the callbackquery.

                SendMessage play = new SendMessage();
                play.setChatId(chatId);
                play.setText(update.getCallbackQuery().getData());
                sendMessage(play);
             */


            switch (key) {
                case "BO_TM":
                    Student student = bookingController.getStudentByFirstName(
                            update.getCallbackQuery().getFrom().getFirstName());

                    String roomName = callbackData.substring(6, 9);
                    Long updid = Long.valueOf(update.getCallbackQuery().getId());
                    Room room = bookingController.getRoomByRoomName(roomName);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    LocalDateTime startTime = LocalDateTime.parse(
                            callbackData.substring(10, 26),
                            formatter);

                    LocalDateTime endTime = LocalDateTime.parse(
                            callbackData.substring(27),
                            formatter
                    );


                    Booking booking = new Booking(updid, student, room, startTime, endTime);
                    Booking newBooking = bookingController.createBooking(booking);
                    response.setText("You have successfully booked Room " + room.getRoomName());
            }

            sendMessage(response);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatID = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            String lastName = update.getMessage().getChat().getLastName();


            Student student = bookingController.getStudentByFirstName(firstName);

            if (student == null) {
                unvalidatedCommandReceived(chatID);
                return;
            }

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatID, firstName, lastName);
                    break;
                case "/cancel":
                    cancelCommandReceived(chatID, firstName, lastName);
                    break;
                case "/view":
                    viewCommandReceived(chatID, firstName, lastName);
                    break;
                case "/book_by_time":
                    bookCommandReceived(chatID, firstName, lastName);
                    break;
                case "/book_by_room":
                    
                    break;
                default:
                    boolean validTimeSlot = validateTimeSlot(messageText);
                    if (validTimeSlot) {
                        String[] parts = messageText.split(" - ");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                        LocalDateTime startDateTime = LocalDateTime.parse(parts[0], formatter);
                        LocalDateTime endDateTime = LocalDateTime.parse(parts[1], formatter);

                        String formattedDateTime = startDateTime.format(formatter);
                        String formattedEndTime = endDateTime.format(formatter);


                        List<Room> result = bookingController.getAvailableRooms(
                                startDateTime,
                                endDateTime);

                        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlinee = new ArrayList<>();

                        for (Room room : result) {
                            List<InlineKeyboardButton> rowInline = new ArrayList<>();
                            InlineKeyboardButton newKey = new InlineKeyboardButton();
                            String roomName = room.getRoomName() + " x" + room.getCapacity();
                            newKey.setText(roomName);
                            newKey.setCallbackData("BO_TM "
                                    + room.getRoomName()
                                    + " "
                                    + formattedDateTime
                                    + " "
                                    + formattedEndTime
                            );
                            rowInline.add(newKey);
                            rowsInlinee.add(rowInline);
                        }

                        keyboardMarkup.setKeyboard(rowsInlinee);

                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(chatID);
                        sendMessage.setText("These are the available rooms: ");
                        sendMessage.setReplyMarkup(keyboardMarkup);

                        sendMessage(sendMessage);
                    } else {
                        String messageReply = "You have entered an unrecognized command.";
                        SendMessage message = new SendMessage(String.valueOf(chatID), messageReply);
                        sendMessage(message);
                    }
            }

        }
    }

    private boolean validateTimeSlot(String timeslot) {
        return ValidateTimeSlot.validateTimeDuration(timeslot);
    }

    private void startCommandReceived(long chatID, String firstName, String lastName) {
        String answer = "Hi, " + firstName + " " + lastName + ", nice to meet you! " +
                "\nHow can I help you??" +
                "\nPress /book_by_time to book any room, but at your desired time" +
                "\nPress /book_by_room to book your fav room" +
                "\nPress /view to view all your current bookings" +
                "\nPress /cancel to cancel an existing booking";


        sendMessage(chatID, answer);

    }

    private void cancelCommandReceived(long chatID, String firstName, String lastName) {
        String reply = firstName + ", are you sure you want to cancel your room reservation? If yes, pick a time slot";

//        List<Booking> bookings =  bookingService.getBookingsByRoomId(301l);

//        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInlinee = new ArrayList<>();
//
//        for(Booking book : bookings){
//            List<InlineKeyboardButton> rowInline = new ArrayList<>();
//            InlineKeyboardButton newKey = new InlineKeyboardButton();
//            String time = book.getStartTime() + " - " +  book.getEndTime() + " at " + book.getRoomId();
//            newKey.setText(time);
//            newKey.setCallbackData(time);
//            rowInline.add(newKey);
//            rowsInlinee.add(rowInline);
//        }
//
//        keyboardMarkup.setKeyboard(rowsInlinee);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(reply);
//        sendMessage.setReplyMarkup(keyboardMarkup);

        sendMessage(sendMessage);
    }

    private void viewCommandReceived(long chatID, String firstName, String lastName) {
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
            newKey.setCallbackData("trash");
            rowInline.add(newKey);
            rowsInlinee.add(rowInline);
        }

        keyboardMarkup.setKeyboard(rowsInlinee);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText("These are your bookings; ");
        sendMessage.setReplyMarkup(keyboardMarkup);

        sendMessage(sendMessage);
    }

    private void bookCommandReceived(long chatID, String firstName, String lastName) {
        SendMessage message = new SendMessage(String.valueOf(chatID),
                "Please enter the date and time in the format \n'YYYY-MM-DD HH:MM - YYYY-MM-DD HH:MM'");
        sendMessage(message);
    }

    private void unvalidatedCommandReceived(long chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(
                "We do not know each other yet. Contact @AdewuyiIsrael to get access");
        sendMessage(sendMessage);
    }

    private void sendMessage(long chatID, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }
}