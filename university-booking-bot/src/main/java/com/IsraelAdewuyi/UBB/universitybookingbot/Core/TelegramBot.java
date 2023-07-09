package com.IsraelAdewuyi.UBB.universitybookingbot.Core;

import com.IsraelAdewuyi.UBB.universitybookingbot.Actions.ValidateTimeSlot;
import com.IsraelAdewuyi.UBB.universitybookingbot.Configuration.BotConfiguration;
import com.IsraelAdewuyi.UBB.universitybookingbot.Controller.BookingController;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Room;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import com.IsraelAdewuyi.UBB.universitybookingbot.Errors.TimeSlotErrors;
import com.IsraelAdewuyi.UBB.universitybookingbot.Tools.FormScheduleImageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private HashMap<Long, String> listOfCommands = new HashMap<>();
    private HashMap<Long, String> listOfRoomCommands = new HashMap<>();
    final BotConfiguration botConfiguration;
    SendMessage lastSentMessage;

    @Autowired
    protected BookingController bookingController;

    @Autowired
    protected FormScheduleImageGenerator formScheduleImageGenerator;

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

            String firstName = update.getCallbackQuery().getFrom().getFirstName();
            String lastName = update.getCallbackQuery().getFrom().getLastName();


            String key = callbackData.substring(0, 5);
            SendMessage response = new SendMessage();
            response.setChatId(chatId);


            switch (key) {
                case "BO_TM":
//                    Student student = bookingController.getStudentByTelegramID(telegramID);
                    Student student = bookingController.getStudentByTelegramID(
                            update.getCallbackQuery().getFrom().getUserName()
                    );


                    String roomName = callbackData.substring(6, 9);
                    Long updid = Long.valueOf(update.getCallbackQuery().getId());
                    Room room = bookingController.getRoomByRoomName(roomName);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");

                    LocalDateTime startTime = LocalDateTime.parse(
                            callbackData.substring(10, 26),
                            formatter);

                    LocalDateTime endTime = LocalDateTime.parse(
                            callbackData.substring(27),
                            formatter
                    );

                    boolean isAvailable = bookingController.isBookingUnique(
                            roomName,
                            startTime,
                            endTime);


                    Booking booking = new Booking(updid, student, room, startTime, endTime);

                    if(isAvailable){
                        Booking newBooking = bookingController.createBooking(booking);

                        String textToSend = "You have successfully booked Room " + room.getRoomName();
                        sendReplyWithUpdatedKeyboard(callbackQuery, textToSend);
                        viewCommandReceived(chatId, student.getTelegramID(), student.getLastName());
                    }
                    else{
                        sendMessage(chatId, "The time you entered conflicts with " +
                                "other bookings.");
                    }

                    break;
                case "BO_RM":
                    String roomToBeBooked = callbackData.substring(6);
                    listOfRoomCommands.put(chatId, roomToBeBooked);
                    SendMessage message = new SendMessage();
                    sendReplyWithUpdatedKeyboard(callbackQuery, "\"Enter the date \\nFormat is MM DD\"");
                    break;
                case "CANCL":
                    Long value = Long.parseLong(callbackData.substring(6));
                    bookingController.deleteBooking(value);
                    sendReplyWithUpdatedKeyboard(callbackQuery, "Booking successfully cancelled");
                    viewCommandReceived(chatId, firstName, lastName);
                    break;
                default:
                    System.out.println("Fuck off");

            }
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatID = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            String lastName = update.getMessage().getChat().getLastName();
            String telegramID = update.getMessage().getChat().getUserName();


            Student student = bookingController.getStudentByTelegramID(telegramID);


            if (student == null) {
                unvalidatedCommandReceived(chatID);
                return;
            }

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatID, firstName, lastName);
                    break;
                case "/cancel":
                    cancelCommandReceived(chatID, telegramID, lastName);
                    break;
                case "/view":
                    viewCommandReceived(chatID, telegramID, lastName);
                    break;
                case "/book_by_time":
                    listOfCommands.remove(chatID);
                    listOfRoomCommands.remove(chatID);
                    bookCommandReceived(chatID, firstName, lastName);
                    break;
                case "/book_by_room":
                    listOfRoomCommands.remove(chatID);
                    listOfCommands.remove(chatID);
                    bookByRoomCommandRecived(chatID);
                    break;
                default:
                    String datePattern = "\\d{2} \\d{2}";
                    String timePattern = "\\d{2}:\\d{2} \\d{2}:\\d{2}";

                    Pattern timeRegex = Pattern.compile(timePattern);
                    Pattern dateRegex = Pattern.compile(datePattern);

                    Matcher timeMatcher = timeRegex.matcher(messageText);
                    Matcher dateMatcher = dateRegex.matcher(messageText);

                    if (dateMatcher.matches()) {
                        if(listOfRoomCommands.containsKey(chatID)){
                            String newText = "2023-";
                            String[] text = messageText.split(" ");
                            newText += text[0];
                            newText += '-';
                            newText += text[1];
//                            newText.charAt(2) = '-';
                            LocalDate date = LocalDate.parse(newText);
                            listOfCommands.put(chatID, messageText);
                            try {
                                // Call the formScheduleImage function
                                FileInputStream imageStream = formScheduleImageGenerator.formScheduleImage(
                                        date, listOfRoomCommands.get(chatID)
                                );

                                // Create a SendPhoto object with the necessary parameters
                                SendPhoto sendPhoto = new SendPhoto();
                                sendPhoto.setChatId(chatID);
                                sendPhoto.setPhoto(new InputFile(imageStream, "schedule.png"));
                                sendPhoto.setCaption("Enter the start and end time \nFormat is HH:MM HH:MM");

                                // Send the photo message
                                Message sentMessage = execute(sendPhoto);

                                // Process the sent message as needed
                            } catch (IOException | TelegramApiException e) {
                                e.printStackTrace(); // Handle the exceptions appropriately
                            }
                        }
                        else{
                            listOfCommands.put(chatID, messageText);
                            SendMessage newMessage = new SendMessage();
                            newMessage.setChatId(chatID);
                            newMessage.setText("Enter the start and end time \nFormat is HH:MM HH:MM");
                            sendMessage(newMessage);
                        }

                    }
                    else if (timeMatcher.matches()) {
                        if (listOfRoomCommands.containsKey(chatID)) {
                            System.out.println("I got to the first");
                            String[] time = messageText.split(" ");
                            if (time.length != 2) {
                                invalidCommandReceived(chatID);
                                listOfRoomCommands.remove(chatID);
                                return;
                            }

                            String startDate = "2023 " + listOfCommands.get(chatID) + " " + time[0];
                            String endDate = "2023 " + listOfCommands.get(chatID) + " " + time[1];

//                            String startDate = listOfCommands.get(chatID) + " " + time[0];
//                            String endDate = listOfCommands.get(chatID) + " " + time[1];

                            listOfCommands.remove(chatID);
                            System.out.println("This is the time: " + startDate + "  " + endDate);

                            String timeDuration = startDate + " - " + endDate;
                            System.out.println(timeDuration);

                            String validTimeReport = validateTimeSlot(timeDuration);

                            TimeSlotErrors pastStartTime = new TimeSlotErrors(new DefaultBotOptions(),
                                    botConfiguration.getToken());

                            switch (validTimeReport) {
                                case "curTime":
                                    try {
                                        pastStartTime.pastStartTime(chatID);
                                    } catch (TelegramApiException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "startTimeBeforeEndTime":
                                    try {
                                        pastStartTime.startTimeBeforeEndTime(chatID);
                                    } catch (TelegramApiException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "valid":
                                    String[] parts = timeDuration.split(" - ");
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");

                                    LocalDateTime startDateTime = LocalDateTime.parse(parts[0], formatter);
                                    LocalDateTime endDateTime = LocalDateTime.parse(parts[1], formatter);

                                    String formattedDateTime = startDateTime.format(formatter);
                                    String formattedEndTime = endDateTime.format(formatter);

//                                    boolean isAvailable = bookingService.isRoomAvailable()

                                    boolean isAvailable = bookingController.isBookingUnique(
                                            listOfRoomCommands.get(chatID),
                                            startDateTime,
                                            endDateTime);

//                                    System.out.println("Room is " + listOfRoomCommands.get(chatID) + " " + isAvailable);


                                    Long updid = Long.valueOf(update.getUpdateId());
                                    Room room = bookingController.getRoomByRoomName(listOfRoomCommands.get(chatID));
                                    Student student1 = bookingController.getStudentByTelegramID(
                                            update.getMessage().getChat().getUserName()
                                    );
//                                    Student student1 = bookingController.getStudentByFirstName(
//                                            update.getMessage().getChat().getFirstName());

                                    if(isAvailable){
                                        Booking booking = new Booking(updid, student1, room, startDateTime, endDateTime);
                                        Booking newBooking = bookingController.createBooking(booking);
                                        SendMessage response = new SendMessage();
                                        response.setChatId(chatID);
                                        response.setText("You have successfully booked Room " + room.getRoomName());
                                        sendMessage(response);
                                        viewCommandReceived(chatID, update.getMessage().getChat().getFirstName(),
                                                update.getMessage().getChat().getLastName());
                                    }
                                    else{
                                        sendMessage(chatID, "The time you entered conflicts with " +
                                                "other bookings.");
                                    }

                                    break;
                                default:
                                    invalidCommandReceived(chatID);

                            }
                        }
                        else {
                            String[] time = messageText.split(" ");
                            System.out.println("I got to the second " + time.length);
                            if (time.length != 2) {
                                invalidCommandReceived(chatID);
                                return;
                            }


                            String startDate = "2023 " + listOfCommands.get(chatID) + " " + time[0];
                            String endDate = "2023 " + listOfCommands.get(chatID) + " " + time[1];

                            listOfCommands.remove(chatID);

                            String timeDuration = startDate + " - " + endDate;
                            System.out.println(timeDuration);

                            String validTimeReport = validateTimeSlot(timeDuration);

                            TimeSlotErrors pastStartTime = new TimeSlotErrors(new DefaultBotOptions(),
                                    botConfiguration.getToken());

                            switch (validTimeReport) {
                                case "curTime":
                                    try {
                                        pastStartTime.pastStartTime(chatID);
                                    } catch (TelegramApiException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "startTimeBeforeEndTime":
                                    try {
                                        pastStartTime.startTimeBeforeEndTime(chatID);
                                    } catch (TelegramApiException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case "valid":
                                    String[] parts = timeDuration.split(" - ");
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");

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
//                                    newKey.setSwitchInlineQueryCurrentChat("That is messed up");
                                        rowInline.add(newKey);
                                        rowsInlinee.add(rowInline);
                                    }

                                    keyboardMarkup.setKeyboard(rowsInlinee);

                                    SendMessage sendMessage = new SendMessage();
                                    sendMessage.setChatId(chatID);
                                    sendMessage.setText("These are the available rooms: ");
                                    sendMessage.setReplyMarkup(keyboardMarkup);

                                    sendMessage(sendMessage);
                                    break;
                                default:
                                    invalidCommandReceived(chatID);

                            }
                        }
                    }
                    else {
                        invalidCommandReceived(chatID);
                    }
            }
        }
    }

    private String validateTimeSlot(String timeslot) {
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

        Student student = bookingController.getStudentByTelegramID(firstName);
        long id = student.getId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");

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

        sendMessage(sendMessage);
    }

    private void viewCommandReceived(long chatID, String firstName, String lastName) {
        Student student = bookingController.getStudentByTelegramID(firstName);
        long id = student.getId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm");

        List<Booking> bookingList = bookingController.getBookingsByStudent(id);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlinee = new ArrayList<>();
//        rowsInlinee.one_time_keyboard = true;

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
        sendMessage.setText("These are your bookings: ");
        sendMessage.setReplyMarkup(keyboardMarkup);

        sendMessage(sendMessage);
    }

    private void bookCommandReceived(long chatID, String firstName, String lastName) {
        SendMessage message = new SendMessage(String.valueOf(chatID),
                "Please enter the date in the format \n'MM DD'");

        sendMessage(message);
    }

    private void bookByRoomCommandRecived(long chatID) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowOfButtons = new ArrayList<>();
        List<InlineKeyboardButton> singleRow = new ArrayList<>();
        List<Room> roomList = bookingController.getAllRooms();
        int count = 3;
        for (Room room : roomList) {
            count++;
            InlineKeyboardButton newRow = new InlineKeyboardButton();
            newRow.setText(room.getRoomName());
            newRow.setCallbackData("BO_RM " + room.getRoomName());
            singleRow.add(newRow);

            if (count % 3 == 0 || count == roomList.size()) {
                rowOfButtons.add(singleRow);
                singleRow = new ArrayList<InlineKeyboardButton>();
            }

        }

//        rowOfButtons.add(singleRow);
        inlineKeyboardMarkup.setKeyboard(rowOfButtons);
        SendMessage replyMessage = new SendMessage();
        replyMessage.setReplyMarkup(inlineKeyboardMarkup);
        replyMessage.setText("Choose your favorite room: ");
        replyMessage.setChatId(chatID);

        sendMessage(replyMessage);
    }

    private void unvalidatedCommandReceived(long chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(
                "We do not know each other yet. Contact @AdewuyiIsrael to get access");
        sendMessage(sendMessage);
    }

    private void invalidCommandReceived(long chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("You have entered an unrecognized command");
        sendMessage.setChatId(chatID);
        sendMessage(sendMessage);
    }

    private void sendReplyWithUpdatedKeyboard(CallbackQuery callbackQuery, String textToSend) {
        String buttonData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

//        // Create an answer to acknowledge the button press
//        AnswerCallbackQuery answer = new AnswerCallbackQuery();
//        answer.setCallbackQueryId(callbackQuery.getId());
//        answer.setText("Button pressed: " + buttonData);
//        answer.setShowAlert(false);
//
//        // Send the answer
//        try {
//            execute(answer);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }

        // Edit the original message to remove the inline keyboard
        EditMessageText editedMessage = new EditMessageText();
        editedMessage.setChatId(chatId);
        editedMessage.setMessageId(messageId);
        editedMessage.setText(textToSend);

        // Send the updated message
        try {
            execute(editedMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(long chatID, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(textToSend);
        boolean value = false;

        try {
            execute(sendMessage);
            lastSentMessage = sendMessage;
            value = true;
        } catch (TelegramApiException e) {
            lastSentMessage = sendMessage;
        }
        return value;
    }

    void sendMessage(SendMessage message) {
        try {
            execute(message);
            lastSentMessage = message;
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }

    }
    public SendMessage getLastSentMessage() {
        return lastSentMessage;
    }
}