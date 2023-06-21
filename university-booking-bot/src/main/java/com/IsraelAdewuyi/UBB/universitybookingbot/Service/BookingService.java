package com.IsraelAdewuyi.UBB.universitybookingbot.Service;

import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Room;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import com.IsraelAdewuyi.UBB.universitybookingbot.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    @Autowired
    private RoomService roomService;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    /*
        public List<Room> getAvailableRooms(int startTime, int endTime) {

        List<Booking> conflictingBookings = bookingRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(endTime, startTime);

        // Assuming you have a Room object associated with each Booking,
        // you can collect the available rooms by filtering out the conflicting bookings
        List<Room> allRooms = roomService.getAllRooms(); // Implement this method to retrieve all rooms
        List<Room> availableRooms = allRooms.stream()
                .filter(room -> conflictingBookings.stream()
                        .noneMatch(booking -> booking.getRoom().getId().equals(room.getId())))
                .collect(Collectors.toList());

        return availableRooms;
    }
    */
    public List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> conflictingBookings = bookingRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(endTime, startTime);

        // Assuming you have a Room object associated with each Booking,
        // you can collect the available rooms by filtering out the conflicting bookings
        List<Room> allRooms = roomService.getAllRooms(); // Implement this method to retrieve all rooms
        List<Room> availableRooms = allRooms.stream()
                .filter(room -> conflictingBookings.stream()
                        .noneMatch(booking -> booking.getRoom().getId().equals(room.getId())))
                .collect(Collectors.toList());

        return availableRooms;
    }

    public List<Booking> getBookingsByStudent(Student student) {
        return bookingRepository.findByStudent(student);
    }
}
