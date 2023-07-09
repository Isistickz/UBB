package com.IsraelAdewuyi.UBB.universitybookingbot.Controller;

import com.IsraelAdewuyi.UBB.universitybookingbot.Controller.BookingController;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Room;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.BookingService;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.RoomService;
import com.IsraelAdewuyi.UBB.universitybookingbot.Service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookingControllerTest {
    private BookingController bookingController;

    @Mock
    private StudentService studentService;

    @Mock
    private RoomService roomService;

    @Mock
    private BookingService bookingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bookingController = new BookingController(studentService, roomService, bookingService);
    }

    @Test
    public void testGetAllUsers() {
        // Mock data
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "John", "Doe", "john.doe@example.com", "johndoe"));
        students.add(new Student(2L, "Jane", "Smith", "jane.smith@example.com", "janesmith"));
        when(studentService.getAllstudents()).thenReturn(students);

        // Perform the method under test
        List<Student> result = bookingController.getAllUsers();

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        // You can add more assertions to verify the result
    }

    // Add more test methods for other controller methods

    @Test
    public void testGetAllRooms() {
        // Mock data
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1L, "301", 24));
        rooms.add(new Room(2L, "303", 30));
        when(roomService.getAllRooms()).thenReturn(rooms);

        // Perform the method under test
        List<Room> result = bookingController.getAllRooms();

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        // You can add more assertions to verify the result
    }

    @Test
    public void testGetAllBookings() {
        // Mock data
        List<Booking> bookings = new ArrayList<>();
        Student newStudent = new Student(3L,
                "Israel", "Adewuyi", "i.adewuyi@innopolis.university", "AdewuyiIsrael");
        Student student2 = new Student(1L,
                "Arthur", "Gubaidullin", "a.gubaidullin@innopolis.university", "VIP33R");

        Room room = new Room(1L, "301", 24);
        Room room2 = new Room(1L, "310", 24);

        bookings.add(new Booking(1L, newStudent, room, LocalDateTime.now(), LocalDateTime.now().plusHours(2) ));
        bookings.add(new Booking(2L, student2, room2, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));

        when(bookingService.getAllBookings()).thenReturn(bookings);

        // Perform the method under test
        List<Booking> result = bookingController.getAllBookings();

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        // You can add more assertions to verify the result
    }
}
