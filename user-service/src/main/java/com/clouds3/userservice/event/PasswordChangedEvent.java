package com.clouds3.userservice.event;

public class PasswordChangedEvent {

    private final Long userId;

    public PasswordChangedEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
