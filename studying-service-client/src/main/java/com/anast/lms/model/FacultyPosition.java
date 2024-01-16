package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacultyPosition {

    @JsonProperty("code")
    private String code;

    @JsonProperty("title")
    private String title;

    public FacultyPosition(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public FacultyPosition() {
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
}
