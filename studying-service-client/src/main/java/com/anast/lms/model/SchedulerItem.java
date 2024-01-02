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
    private Short number;

    @JsonProperty("day_of_week")
    private Short dayOfWeek;

    public SchedulerItem(DisciplineInstance discipline, String classRoom, String groups, ClassType classType, Short number, Short dayOfWeek) {
        this.discipline = discipline;
        this.classRoom = classRoom;
        this.groups = groups;
        this.classType = classType;
        this.number = number;
        this.dayOfWeek = dayOfWeek;
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

    public Short getNumber() {
        return number;
    }

    public void setNumber(Short number) {
        this.number = number;
    }

    public Short getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Short dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
