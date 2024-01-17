package com.anast.lms.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentProfileInfo {

    @JsonProperty("group_code")
    private String groupCode;

    public StudentProfileInfo(String groupCode, Integer course) {
        this.groupCode = groupCode;
        this.course = course;
    }

    @JsonProperty("course")
    private Integer course;

    public StudentProfileInfo(String groupCode) {
        this.groupCode = groupCode;
    }

    public StudentProfileInfo() {
    }

    @Override
    public String toString() {
        return String.format("Студент %s курса. Группа %s", getCourse(), getGroupCode());
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }
}
