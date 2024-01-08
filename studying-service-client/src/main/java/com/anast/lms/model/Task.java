package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private String taskType;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("deadline")
    private LocalDate deadLine;

    @JsonProperty("resources")
    private List<ModuleResource> resources = new ArrayList<>();

    @JsonIgnore
    public TaskType getTaskTypeEnum() {
        return TaskType.getTaskTypeEnum(this.taskType);
    }

    public Task(Integer id, String taskType, String title, String description, LocalDate deadLine, List<ModuleResource> resources) {
        this.id = id;
        this.taskType = taskType;
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.resources = resources;
    }

    public Task() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
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

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public List<ModuleResource> getResources() {
        return resources;
    }

    public void setResources(List<ModuleResource> resources) {
        this.resources = resources;
    }
}
