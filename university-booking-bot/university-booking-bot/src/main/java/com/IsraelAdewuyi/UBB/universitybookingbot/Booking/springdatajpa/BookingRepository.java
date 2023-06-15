package com.IsraelAdewuyi.UBB.universitybookingbot.Booking.springdatajpa;

import com.IsraelAdewuyi.UBB.universitybookingbot.Booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE b.roomId = :roomId " +
            "AND b.startTime < :endTime " +
            "AND b.endTime > :startTime")
    int countConflictingBookings(@Param("roomId") Long roomId,
                                 @Param("startTime") Long startTime,
                                 @Param("endTime") Long endTime);


    Booking findByStartTimeAndEndTime(long startTime, long endTime);

    List<Booking> findByRoomId(long room_id);

    @Transactional
    void deleteByStartTime(long startTime);
}
