package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudyForm {

    @JsonProperty("code")
    private String code;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    public StudyForm(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }

    public StudyForm() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
