package com.IsraelAdewuyi.UBB.universitybookingbot.Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CommandReceived<T, S> {
    SendMessage executeCommand(T t, S s);
}
