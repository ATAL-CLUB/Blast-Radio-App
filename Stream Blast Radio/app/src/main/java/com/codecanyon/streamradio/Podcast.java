package com.codecanyon.streamradio;

/**
 * Created by emmanueladeleke on 14/06/16.
 */
public class Podcast {

    /* TODO: Implement date instead of year (and possibly time) */
    private String title;
    private String description;
    private String year;
    private int duration;
    private int playback_count;

    public Podcast() {
    }

    public Podcast(String title, String description, String year, int duration, int playback_count) {
        this.title = title;
        this.description = description;
        this.year = year;
        this.duration = duration;
        this.playback_count = playback_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlayback_count() {
        return playback_count;
    }

    public void setPlayback_count(int playback_count) {
        this.playback_count = playback_count;
    }
}