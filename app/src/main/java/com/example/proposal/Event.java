package com.example.proposal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }


    private String name;
    private String description;
    private LocalDate date;
    private String time;
    private int repeat;

    public Event(String name, String description, LocalDate date, String time, int repeat)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription() { return description;}

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public int getRepeat() { return repeat; }
}