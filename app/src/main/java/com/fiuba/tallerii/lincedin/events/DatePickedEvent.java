package com.fiuba.tallerii.lincedin.events;

public class DatePickedEvent {
    public int day;
    public int month;
    public int year;

    public DatePickedEvent(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
