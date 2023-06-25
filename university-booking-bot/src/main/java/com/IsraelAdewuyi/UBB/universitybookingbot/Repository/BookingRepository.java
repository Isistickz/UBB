package com.IsraelAdewuyi.UBB.universitybookingbot.Repository;

import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
//    List<Booking> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(int endTime, int startTime);
    List<Booking> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> findByStudent(Student student);

    @Query("SELECT b FROM Booking b WHERE date(b.startTime) = :date")
    List<Booking> getBookingsByDate(LocalDate date);
}
