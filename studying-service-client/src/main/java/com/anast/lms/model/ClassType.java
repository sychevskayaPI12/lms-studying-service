package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ClassType {

    lecture("lect", "Лекционное занятие"),
    practical("pract", "Практическое заятие"),
    laboratory("lab", "Лабораторное занятие"),
    consultation("cons", "Консультация"),
    examination("exam", "Зачетное занятие")
    ;

    @JsonProperty("code")
    private String code;

    @JsonProperty("title")
    private String title;

    ClassType(String code, String title) {
        this.code = code;
        this.title = title;
    }

    ClassType() {
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}
