package com.IsraelAdewuyi.UBB.universitybookingbot.Service;

import com.IsraelAdewuyi.UBB.universitybookingbot.Booking.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Booking.springdatajpa.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    int runningID = 1;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    public List<Booking> getBookingsByRoomId(Long room_Id) {
        return bookingRepository.findByRoomId(room_Id);
    }

    public boolean isRoomAvailable(long room, long startTime, long endTime) {
        int conflictingBookings = bookingRepository.countConflictingBookings(room, startTime, endTime);
        return conflictingBookings == 0;
    }

    public void bookRoom(long room, long startTime, long endTime){
        bookingRepository.save(new Booking(runningID, 301, startTime, endTime));
        runningID++;
    }

    public boolean cancelBooking(long startTime, long endTime) {
        Booking bookings = bookingRepository.findByStartTimeAndEndTime(startTime, endTime);
        bookingRepository.delete(bookings);
        return bookings != null;
    }

    public void deleteByTime(long startTime, long endTime){
        bookingRepository.deleteByStartTime(startTime);
    }
}
