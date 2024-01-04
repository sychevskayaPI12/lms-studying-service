package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TaskType {

    laboratory("lab", "Лабораторная работа"),
    practical("practical", "Практическая работа"),
    control("control", "Контрольная работа"),
    course_project("course", "Курсовой проект")
    ;

    @JsonIgnore
    private static final Map<String, TaskType> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(TaskType::getCode, v -> v));

    @JsonIgnore
    public static TaskType getTaskTypeEnum(String code) {
        return lookup.get(code);
    }

    @JsonProperty("code")
    private String code;

    @JsonProperty("title")
    private String title;

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

    TaskType(String code, String title) {
        this.code = code;
        this.title = title;
    }
}
