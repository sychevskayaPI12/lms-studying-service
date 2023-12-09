package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TeacherProfileInfo {

    @JsonProperty("degree")
    private String degree;

    public TeacherProfileInfo(String degree) {
        this.degree = degree;
    }

    public TeacherProfileInfo() {
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }


}
