package com.fiuba.tallerii.lincedin.model.user;

public class Recommendation {
    private String description;
    private String from;

    public Recommendation(String description, String from) {
        this.description = description;
        this.from = from;
    }

    public String getDescription() {
        return description;
    }

    public String getFrom() {
        return from;
    }
}
