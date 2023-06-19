package com.IsraelAdewuyi.UBB.universitybookingbot.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Room {
    @Id
    long roomId;
    long capacity;
}
