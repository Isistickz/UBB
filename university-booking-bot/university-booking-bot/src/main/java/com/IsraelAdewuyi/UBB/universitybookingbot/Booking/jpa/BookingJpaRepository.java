package com.IsraelAdewuyi.UBB.universitybookingbot.Booking.jpa;

import com.IsraelAdewuyi.UBB.universitybookingbot.Booking.Booking;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class BookingJpaRepository {
    @Autowired
    private EntityManager entityManager;

    public void insert(Booking booking){
        entityManager.merge(booking);
    }

    public Booking findByID(long id){
        return entityManager.find(Booking.class, id);
    }
}
