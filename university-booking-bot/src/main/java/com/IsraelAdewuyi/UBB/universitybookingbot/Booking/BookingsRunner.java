package com.IsraelAdewuyi.UBB.universitybookingbot.Booking;

import com.IsraelAdewuyi.UBB.universitybookingbot.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class BookingsRunner implements CommandLineRunner {
//    @Autowired
//    private BookingJpaRepository bookingJpaRepository;

    @Autowired
    private BookingRepository bookingJpaRepository;
    @Override
    public void run(String... args) throws Exception {
//        bookingJpaRepository.save(new Booking(1, 301, 23, 26));
////        bookingJpaRepository.save(new Booking(2, 301, 23, 68));

//        System.out.println(bookingJpaRepository.countConflictingBookings(301l, 24l, 25l));

    }
}
