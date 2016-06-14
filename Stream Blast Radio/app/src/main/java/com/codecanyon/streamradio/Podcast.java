package com.codecanyon.streamradio;

/**
 * Created by emmanueladeleke on 14/06/16.
 */
public class Podcast {

    /* TODO: Implement date instead of year (and possibly time) */
    private String title, description, year;

    public Podcast() {
    }

    public Podcast(String title, String description, String year) {
        this.title = title;
        this.description = description;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}