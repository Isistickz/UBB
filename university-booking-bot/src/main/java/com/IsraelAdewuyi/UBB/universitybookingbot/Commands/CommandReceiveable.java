package com.IsraelAdewuyi.UBB.universitybookingbot.Commands;

public interface CommandReceiveable<T, S> {
    void executeCommand(T t, S s);
}
