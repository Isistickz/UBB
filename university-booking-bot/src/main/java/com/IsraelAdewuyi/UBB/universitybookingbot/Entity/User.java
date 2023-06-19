package com.IsraelAdewuyi.UBB.universitybookingbot.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private long userId;
    private long userName;
    private long firstName;
}
