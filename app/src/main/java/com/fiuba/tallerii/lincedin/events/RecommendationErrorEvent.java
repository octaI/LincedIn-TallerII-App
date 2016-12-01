package com.fiuba.tallerii.lincedin.events;

public class RecommendationErrorEvent {
    public String message;
    public RecommendationErrorEvent() {}
    public RecommendationErrorEvent(String message) {
        this.message = message;
    }
}
