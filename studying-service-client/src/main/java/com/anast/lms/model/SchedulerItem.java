package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchedulerItem {

    @JsonProperty("discipline")
    private DisciplineInstance discipline;

    @JsonProperty("class_room")
    private String classRoom;

    @JsonProperty("groups")
    private String groups;

    @JsonProperty("class_type")
    private ClassType classType;

    @JsonProperty("number")
    private short number;

    @JsonProperty("teachers")
    private String teachers;

    public SchedulerItem(DisciplineInstance discipline, String classRoom, String groups, ClassType classType, short number, String teachers) {
        this.discipline = discipline;
        this.classRoom = classRoom;
        this.groups = groups;
        this.classType = classType;
        this.number = number;
        this.teachers = teachers;
    }

    public SchedulerItem() {
    }

    public DisciplineInstance getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineInstance discipline) {
        this.discipline = discipline;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

}
