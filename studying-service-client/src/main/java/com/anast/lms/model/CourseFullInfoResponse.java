package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class CourseFullInfoResponse {

    @JsonProperty("course")
    private Course course;

    @JsonProperty("modules")
    private List<CourseModule> modules;

    public CourseFullInfoResponse(Course course, List<CourseModule> modules) {
        this.course = course;
        this.modules = modules;
    }

    public CourseFullInfoResponse() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<CourseModule> getModules() {
        return modules;
    }

    public void setModules(LinkedList<CourseModule> modules) {
        this.modules = modules;
    }
}
