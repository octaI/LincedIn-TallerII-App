package com.fiuba.tallerii.lincedin.events;

public class RecommendationPostedEvent {
    public String message;
    public RecommendationPostedEvent() {}
    public RecommendationPostedEvent(String message) {
        this.message = message;
    }
}
