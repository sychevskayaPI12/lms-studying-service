package com.anast.lms.model.profile;

import com.anast.lms.model.Department;
import com.anast.lms.model.FacultyPosition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TeacherFacultyPosition {

    @JsonProperty("position")
    private FacultyPosition position;

    @JsonProperty("department")
    private Department department;

    public TeacherFacultyPosition(FacultyPosition position, Department department) {
        this.position = position;
        this.department = department;
    }

    public TeacherFacultyPosition() {
    }

    @Override
    public String toString() {
        //todo
        return "";
    }

    public FacultyPosition getPosition() {
        return position;
    }

    public void setPosition(FacultyPosition position) {
        this.position = position;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
