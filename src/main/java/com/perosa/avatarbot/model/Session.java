package com.perosa.avatarbot.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Session {

    private String id;
    private LocalDateTime startDate = LocalDateTime.now(ZoneId.of("CET"));
    private List<String> tags = new ArrayList<>();

    public Session() {
    }

    public Session(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addToTags(String tag) {
        this.tags.add(tag);
    }

    @Override
    public String toString() {
        return "Session[" +
                "id:" + id +
                ", tags:" + tags +
                "]";
    }
}
