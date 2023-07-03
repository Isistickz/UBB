package com.IsraelAdewuyi.UBB.universitybookingbot.Controller;

import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Room;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.BookingService;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.RoomService;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.StudentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RestController
@RequestMapping("/")
public class BookingController {
    private final StudentService studentService;
    private final RoomService roomService;
    private final BookingService bookingService;

    @Autowired
    public BookingController(StudentService studentService, RoomService roomService, BookingService bookingService) {
        this.studentService = studentService;
        this.roomService = roomService;
        this.bookingService = bookingService;

        roomService.saveRoom(new Room(2l, "301", 24));
        roomService.saveRoom(new Room(4l, "303", 30));
        roomService.saveRoom(new Room(5l, "304", 25));
        roomService.saveRoom(new Room(6l, "305", 25));
        roomService.saveRoom(new Room(7l, "312", 30));
        roomService.saveRoom(new Room(8l, "313", 60));
        roomService.saveRoom(new Room(9l, "314", 34));
        roomService.saveRoom(new Room(20l, "318", 30));
        roomService.saveRoom(new Room(10l, "320", 28));

        studentService.saveStudent(new Student(3L,
                "Israel", "Adewuyi", "i.adewuyi@innopolis.university", "AdewuyiIsrael"));
        studentService.saveStudent(new Student(4L,
                "Arthur", "Gubaidullin", "a.gubaidullin@innopolis.university", "V11P3R"));
//        studentService.saveStudent(new Student(5L,
//                "Rodion", "isARussian", "fuckoff"));
        studentService.saveStudent(new Student(6L,
                "Apollinaria", "Chernikova", "a.chernikova@innopolis.university", "Apollinaria2004"));
        studentService.saveStudent(new Student(7L,
                "dinara", "murtazina", "d.murtazina@innopolis.university", "cucumur"));
        studentService.saveStudent(new Student(8L,
                "Chulpan", "Valiullina", "c.valiullina@innopolis.university", "Chehmet"));
        studentService.saveStudent(new Student(9L,
                "Руфина","Gafiatullina", "r.gafiatullina@innopolis.university", "R_ufina" ));
        studentService.saveStudent(new Student(9L,
                "AbdelRahman", "Abounegm", "a.abounegm@innopolis.university", "aabounegm"));
        studentService.saveStudent(new Student(10L,
                "Milyausha", "isArussian", "m.isARussian@innopolis.university", "mili_sham"));
    }

    @GetMapping("/users")
    public List<Student> getAllUsers() {
        return studentService.getAllstudents();
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.saveBooking(booking);
    }

    @DeleteMapping("/bookings/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @GetMapping("/tryout")
    public String justToSee() {
        return "This API endpoint works";
    }

    @GetMapping("/available-rooms")
    public List<Room> getAvailableRooms(@PathVariable LocalDateTime startTime, @PathVariable LocalDateTime endTime) {
        return bookingService.getAvailableRooms(startTime, endTime);
    }

    @GetMapping("/{firstName}")
    public Student getStudentByFirstName(@PathVariable String firstName) {
        return studentService.getStudentByFirstName(firstName);
    }

//    @GetMapping("/{id}")
//    public Student getStudentByFirstName(@PathVariable String firstName) {
//        return studentService.getStudentByFirstName(firstName);
//    }

    @GetMapping("/rooms/{roomName}")
    public Room getRoomByRoomName(@PathVariable String roomName) {
        return roomService.getRoomByRoomName(roomName);
    }

    @GetMapping("/students/{studentId}")
    public List<Booking> getBookingsByStudent(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId); // Assuming you have a method to retrieve a student by ID
        return bookingService.getBookingsByStudent(student);
    }

    public List<Booking> getBookingsByDate(LocalDate date){
        return bookingService.getBookingsByDate(date);
    }

    public List<Booking> getBookingsByRoomAndDate(String roomName, LocalDate date){
        return bookingService.getBookingsByRoomAndDate(roomName, date);
    }

    public Student getStudentByTelegramID(String telegramID){
        return studentService.getStudentWithTelegramID(telegramID);
    }

    public boolean isBookingUnique(String roomName, LocalDateTime startTime, LocalDateTime endTime){
        return bookingService.isBookingConflict(roomName, startTime, endTime);
    }
}
