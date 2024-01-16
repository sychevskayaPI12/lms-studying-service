package com.anast.lms.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TeacherProfileInfo {

    @JsonProperty("degree")
    private String degree;

    @JsonProperty("positions")
    private List<TeacherFacultyPosition> positions;

    public TeacherProfileInfo(String degree, List<TeacherFacultyPosition> positions) {
        this.degree = degree;
        this.positions = positions;
    }

    public TeacherProfileInfo() {
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public List<TeacherFacultyPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<TeacherFacultyPosition> positions) {
        this.positions = positions;
    }
}
