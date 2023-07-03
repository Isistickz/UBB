package com.IsraelAdewuyi.UBB.universitybookingbot.Repository;

import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Booking;
import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStartTimeLessThanAndEndTimeGreaterThan(LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> findByStudent(Student student);

    @Query("SELECT b FROM Booking b WHERE date(b.startTime) = :date")
    List<Booking> getBookingsByDate(LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.room.roomName = :roomName AND date(b.startTime) = :date")
    List<Booking> getBookingsByRoomNameAndDate(@Param("roomName") String roomName, @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.room.roomName = :roomName AND ((b.startTime > :startTime AND b.startTime < :endTime) OR (b.endTime > :startTime AND b.endTime < :endTime))")
    List<Booking> findConflictingBookings(@Param("roomName") String roomName, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);



}
