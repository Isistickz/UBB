package com.IsraelAdewuyi.UBB.universitybookingbot.Service;

import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Room;
import com.IsraelAdewuyi.UBB.universitybookingbot.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public Room getRoomByRoomName(String roomName) {
        return roomRepository.findByRoomName(roomName);
    }
}

