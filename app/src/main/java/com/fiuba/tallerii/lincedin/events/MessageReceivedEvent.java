package com.fiuba.tallerii.lincedin.events;

public class MessageReceivedEvent {
    private String message;

    public MessageReceivedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
