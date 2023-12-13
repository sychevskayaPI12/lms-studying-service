package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseModule {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("order")
    private short order;

    public CourseModule() {
    }

    public CourseModule(Integer id, String title, String content, short order) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.order = order;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public short getOrder() {
        return order;
    }

    public void setOrder(short order) {
        this.order = order;
    }
}
