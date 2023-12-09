package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentProfileInfo {

    @JsonProperty("group_code")
    private String groupCode;

    @JsonProperty("course")
    private Integer course;

    public StudentProfileInfo(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
