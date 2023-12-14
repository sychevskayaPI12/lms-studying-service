package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static Map<String, ClassType> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(ClassType::getCode, v->v));

    public static ClassType getEnum(String code) {
        return lookup.get(code);
    }
}
