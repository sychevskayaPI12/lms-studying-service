package com.anast.lms.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TeacherProfileInfo {

    @JsonProperty("positions")
    private List<TeacherFacultyPosition> positions = new ArrayList<>();

    public TeacherProfileInfo(List<TeacherFacultyPosition> positions) {
        this.positions = positions;
    }

    public TeacherProfileInfo() {
    }

    public List<TeacherFacultyPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<TeacherFacultyPosition> positions) {
        this.positions = positions;
    }
}
