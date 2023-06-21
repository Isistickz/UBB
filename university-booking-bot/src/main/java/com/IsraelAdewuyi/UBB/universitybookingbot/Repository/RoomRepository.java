package com.IsraelAdewuyi.UBB.universitybookingbot.Repository;

import com.IsraelAdewuyi.UBB.universitybookingbot.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomName(String roomName);
}
