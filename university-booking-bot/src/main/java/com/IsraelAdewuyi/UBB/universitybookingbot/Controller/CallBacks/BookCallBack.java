package com.IsraelAdewuyi.UBB.universitybookingbot.Controller.CallBacks;

import com.IsraelAdewuyi.UBB.universitybookingbot.Controller.BookingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookCallBack {
    private final BookingController bookingController;

    @Autowired
    public BookCallBack(BookingController bookingController) {
        this.bookingController = bookingController;
    }

    public BookingController getBookingController() {
        return bookingController;
    }

//    bookingController.getAllBookings();

}
