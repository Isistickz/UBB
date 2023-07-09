package com.IsraelAdewuyi.UBB.universitybookingbot.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;


@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Room room;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Booking(Long id, Student student, Room room, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.student = student;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Booking() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getUser() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", student=" + student +
                ", room=" + room +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}

