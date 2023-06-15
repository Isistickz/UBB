package com.IsraelAdewuyi.UBB.universitybookingbot.Booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Booking {
    @Id
    long id;
    @Column(name = "room_id")
    long roomId;
    @Column(name = "startTime")
    long startTime;
    @Column(name = "endTime")
    long endTime;

    public Booking(){

    }

    public Booking(long id, long room_id, long startTime, long endTime) {
        this.id = id;
        this.roomId = room_id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long room_id) {
        this.roomId = room_id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", room_id=" + roomId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
