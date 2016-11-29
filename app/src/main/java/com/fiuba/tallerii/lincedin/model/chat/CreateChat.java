package com.fiuba.tallerii.lincedin.model.chat;

import java.util.List;

public class CreateChat {

    private List<String> participants;

    public CreateChat(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
