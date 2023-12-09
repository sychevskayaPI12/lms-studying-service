package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Discipline {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    public Discipline(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Discipline() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
