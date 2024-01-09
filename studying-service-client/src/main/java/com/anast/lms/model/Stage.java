package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stage {

    @JsonProperty("code")
    private String code;

    @JsonProperty("title")
    private String title;

    public Stage(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public Stage() {
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
