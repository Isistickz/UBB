package com.IsraelAdewuyi.UBB.universitybookingbot.Core;

import com.IsraelAdewuyi.UBB.universitybookingbot.Configuration.BotConfiguration;
import com.IsraelAdewuyi.UBB.universitybookingbot.Controller.BookingController;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.BookingService;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.RoomService;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.StudentService;
import com.IsraelAdewuyi.UBB.universitybookingbot.Tools.FormScheduleImageGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class TelegramBotTest {
    private TelegramBot telegramBot;

    @Mock
    private BotConfiguration botConfiguration;

    @Mock
    private BookingController bookingController;

    @Mock
    private StudentService studentService;

    @Mock
    private RoomService roomService;

    @Mock
    private BookingService bookingService;

    @Mock
    private FormScheduleImageGenerator formScheduleImageGenerator;

    private Update createTextMessageUpdate(long chatId, String firstName, String lastName, String userName,
                                           String text) {
        Update update = new Update();
        Message message = new Message();
        message.setChat(createChat(chatId, firstName, lastName, userName));

        message.setText(text);
        update.setMessage(message);
        return update;
    }

    private SendMessage createSendMessageResponse(long chatId, String text) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        return response;
    }

    private Chat createChat(long chatId, String firstName, String lastName, String userName) {
        Chat chat = new Chat();
        chat.setId(chatId);
        chat.setFirstName(firstName);
        chat.setLastName(lastName);
        chat.setUserName(userName);
        return chat;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        telegramBot = new TelegramBot(botConfiguration);
        bookingController = new BookingController(studentService, roomService, bookingService);
        telegramBot.bookingController = bookingController;
        telegramBot.formScheduleImageGenerator = formScheduleImageGenerator;
        when(botConfiguration.getBotName()).thenReturn("TestBot");
        when(botConfiguration.getToken()).thenReturn("test-token");
    }

    @Test
    public void testStartCommandReceived() {
        long chatId = 975270190;
        String firstName = "Adewuyi";
        String lastName = "Israel";
        String telegramID = "AdewuyiIsrael";

        String expectedAnswer = "Hi, " + firstName + " " + lastName + ", nice to meet you! " +
                "\nHow can I help you??" +
                "\nPress /book_by_time to book any room, but at your desired time" +
                "\nPress /book_by_room to book your fav room" +
                "\nPress /view to view all your current bookings" +
                "\nPress /cancel to cancel an existing booking";

        Update update = createTextMessageUpdate(chatId, firstName, lastName, telegramID, "/start");
        SendMessage expectedResponse = createSendMessageResponse(chatId, expectedAnswer);
        Student student = new Student(3L,
                "Israel", "Adewuyi", "i.adewuyi@innopolis.university", "AdewuyiIsrael");
        when(bookingController.getStudentByTelegramID(anyString())).thenReturn(student);

        telegramBot.onUpdateReceived(update);

        // Assert that the response message matches the expected response
        assertEquals(expectedResponse, telegramBot.getLastSentMessage());
    }


//        @Test
//    public void testCancelCommandReceived() {
//        long chatId = 123456789L;
//        String firstName = "John";
//        String lastName = "Doe";
//        long bookingId = 1L;
//
//        Student student = new Student(1L, "John Doe", "john.doe@example.com", "johnny");
//        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2),
//                student, new Room(1L, "Room 1", 10));
//        List<Booking> bookingList = new ArrayList<>();
//        bookingList.add(booking);
//
//        Update update = createTextMessageUpdate(chatId, firstName, lastName, "/cancel");
//        when(bookingController.getStudentByTelegramID(anyString())).thenReturn(student);
//        when(bookingController.getBookingsByStudent(anyLong())).thenReturn(bookingList);
//
//        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
//        inlineKeyboardButton.setText(booking.getStartTime().toString() + " at " + booking.getRoom().getRoomName());
//        inlineKeyboardButton.setCallbackData("CANCL " + booking.getId());
//
//        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        rowInline.add(inlineKeyboardButton);
//        rowsInline.add(rowInline);
//        keyboardMarkup.setKeyboard(rowsInline);
//
//        SendMessage expectedResponse = createSendMessageResponse(chatId, firstName + ", are you sure you want to cancel your room reservation? If yes, pick a time slot");
//        expectedResponse.setReplyMarkup(keyboardMarkup);
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//    }
//
//    @Test
//    public void testViewCommandReceived() {
//        long chatId = 123456789L;
//        String firstName = "John";
//        String lastName = "Doe";
//        long bookingId = 1L;
//
//        Student student = new Student(1L, "John Doe", "john.doe@example.com", "johnny");
//        Booking booking = new Booking(bookingId, LocalDateTime.now(), LocalDateTime.now().plusHours(2),
//                student, new Room(1L, "Room 1", 10));
//        List<Booking> bookingList = new ArrayList<>();
//        bookingList.add(booking);
//
//        Update update = createTextMessageUpdate(chatId, firstName, lastName, "/view");
//        when(bookingController.getStudentByTelegramID(anyString())).thenReturn(student);
//        when(bookingController.getBookingsByStudent(anyLong())).thenReturn(bookingList);
//
//        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
//        inlineKeyboardButton.setText(booking.getStartTime().toString() + " at " + booking.getRoom().getRoomName());
//        inlineKeyboardButton.setCallbackData("trash");
//
//        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        rowInline.add(inlineKeyboardButton);
//        rowsInline.add(rowInline);
//        keyboardMarkup.setKeyboard(rowsInline);
//
//        SendMessage expectedResponse = createSendMessageResponse(chatId, "These are your bookings: ");
//        expectedResponse.setReplyMarkup(keyboardMarkup);
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//    }
//
//    @Test
//    public void testBookCommandReceived() {
//        long chatId = 123456789L;
//        String firstName = "John";
//        String lastName = "Doe";
//
//        Update update = createTextMessageUpdate(chatId, firstName, lastName, "/book_by_time");
//        SendMessage expectedResponse = createSendMessageResponse(chatId, "Please enter the date in the format \n'MM DD'");
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//    }
//
//    @Test
//    public void testBookByRoomCommandReceived() {
//        longchatId = 123456789L;
//
//        Update update = createTextMessageUpdate(chatId, "John", "Doe", "/book_by_room");
//        List<Room> roomList = new ArrayList<>();
//        roomList.add(new Room(1L, "Room 1", 10));
//        roomList.add(new Room(2L, "Room 2", 20));
//        roomList.add(new Room(3L, "Room 3", 30));
//        when(bookingController.getAllRooms()).thenReturn(roomList);
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowOfButtons = new ArrayList<>();
//        List<InlineKeyboardButton> singleRow1 = new ArrayList<>();
//        List<InlineKeyboardButton> singleRow2 = new ArrayList<>();
//        List<InlineKeyboardButton> singleRow3 = new ArrayList<>();
//
//        InlineKeyboardButton inlineButton1 = new InlineKeyboardButton();
//        inlineButton1.setText("Room 1");
//        inlineButton1.setCallbackData("BO_RM Room 1");
//
//        InlineKeyboardButton inlineButton2 = new InlineKeyboardButton();
//        inlineButton2.setText("Room 2");
//        inlineButton2.setCallbackData("BO_RM Room 2");
//
//        InlineKeyboardButton inlineButton3 = new InlineKeyboardButton();
//        inlineButton3.setText("Room 3");
//        inlineButton3.setCallbackData("BO_RM Room 3");
//
//        singleRow1.add(inlineButton1);
//        singleRow2.add(inlineButton2);
//        singleRow3.add(inlineButton3);
//
//        rowOfButtons.add(singleRow1);
//        rowOfButtons.add(singleRow2);
//        rowOfButtons.add(singleRow3);
//
//        inlineKeyboardMarkup.setKeyboard(rowOfButtons);
//
//        SendMessage expectedResponse = createSendMessageResponse(chatId, "Choose your favorite room: ");
//        expectedResponse.setReplyMarkup(inlineKeyboardMarkup);
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//    }
//
//    @Test
//    public void testUnvalidatedCommandReceived() {
//        long chatId = 123456789L;
//
//        Update update = createTextMessageUpdate(chatId, "John", "Doe", "Test message");
//
//        SendMessage expectedResponse = createSendMessageResponse(chatId,
//                "We do not know each other yet. Contact @AdewuyiIsrael to get access");
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//    }
//
//    @Test
//    public void testInvalidCommandReceived() {
//        long chatId = 123456789L;
//
//        Update update = createTextMessageUpdate(chatId, "John", "Doe", "/invalid_command");
//
//        SendMessage expectedResponse = createSendMessageResponse(chatId, "You have entered an unrecognized command");
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//    }
//
//    @Test
//    public void testCallbackQuery_BO_TM() {
//        long chatId = 123456789L;
//        String firstName = "John";
//        String lastName = "Doe";
//        String roomName = "Room 1";
//        LocalDateTime startTime = LocalDateTime.now();
//        LocalDateTime endTime = startTime.plusHours(2);
//        long updid = 123L;
//
//        Update update = createCallbackQueryUpdate(chatId, firstName, lastName, "BO_TM " + roomName + " " +
//                startTime.format(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm")) + " " +
//                endTime.format(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm")));
//        Student student = new Student(1L, "John Doe", "john.doe@example.com", "johnny");
//        Room room = new Room(1L, roomName, 10);
//        Booking booking = new Booking(updid, student, room, startTime, endTime);
//
//        when(bookingController.getStudentByTelegramID(anyString())).thenReturn(student);
//        when(bookingController.getRoomByRoomName(anyString())).thenReturn(room);
//        when(bookingController.isBookingUnique(anyString(), any(LocalDateTime.class),
//                any(LocalDateTime.class))).thenReturn(true);
//        when(bookingController.createBooking(any(Booking.class))).thenReturn(booking);
//
//        SendMessage expectedResponse =
//                createSendMessageResponse(chatId, "You have successfully booked Room " + roomName);
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//        verify(telegramBot).viewCommandReceived(chatId, firstName, lastName);
//    }
//
//    @Test
//    public void testCallbackQuery_BO_TM_TimeConflict() {
//        long chatId = 123456789L;
//        String firstName = "John";
//        String lastName = "Doe";
//        String roomName = "Room 1";
//        LocalDateTime startTime = LocalDateTime.now();
//        LocalDateTime endTime = startTime.plusHours(2);
//        long updid = 123L;
//
//        Update update = createCallbackQueryUpdate(chatId, firstName, lastName, "BO_TM " + roomName + " " +
//                startTime.format(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm")) + " " +
//                endTime.format(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm")));
//        Student student = new Student(1L, "John Doe", "john.doe@example.com", "johnny");
//        Room room = new Room(1L, roomName, 10);
//        Booking booking = new Booking(updid, student, room, startTime, endTime);
//
//        when(bookingController.getStudentByTelegramID(anyString())).thenReturn(student);
//        when(bookingController.getRoomByRoomName(anyString())).thenReturn(room);
//        when(bookingController.isBookingUnique(anyString(), any(LocalDateTime.class),
//                any(LocalDateTime.class))).thenReturn(false);
//
//        SendMessage expectedResponse =
//                createSendMessageResponse(chatId, "The time you entered conflicts with other bookings.");
//
//        telegramBot.onUpdateReceived(update);
//
//        verify(telegramBot).sendMessage(expectedResponse);
//    }
}
