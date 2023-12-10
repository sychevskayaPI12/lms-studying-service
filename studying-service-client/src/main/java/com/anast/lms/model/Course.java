package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Course {

    @JsonProperty("id")
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("discipline")
    private DisciplineInstance discipline;

    @JsonProperty("teachers_login")
    private List<String> teacherLogins;

    @JsonProperty("teachers")
    private Map<String, String> teachers;

    public Course() { }

    public Course(Integer id, LocalDate startDate, LocalDate endDate, DisciplineInstance discipline) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discipline = discipline;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DisciplineInstance getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineInstance discipline) {
        this.discipline = discipline;
    }

    public List<String> getTeacherLogins() {
        return teacherLogins;
    }

    public void setTeacherLogins(List<String> teacherLogins) {
        this.teacherLogins = teacherLogins;
    }

    public Map<String, String> getTeachers() {
        return teachers;
    }

    public void setTeachers(Map<String, String> teachers) {
        this.teachers = teachers;
    }
}
