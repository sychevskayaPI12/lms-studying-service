package com.anast.lms.model;

import com.anast.lms.model.profile.UserProfileInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DisciplineInstance extends Discipline {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("specialty")
    private String specialty;

    @JsonProperty("semester")
    private short semester;

    @JsonProperty("is_examination")
    private boolean isExamination;

    @JsonProperty("stage_code")
    private String stageCode;

    @JsonProperty("stage_name")
    private String stageName;

    @JsonProperty("study_form_code")
    private String studyFormCode;

    @JsonProperty("study_form_short_name")
    private String studyFormShortName;

    @JsonProperty("teachers_login")
    private List<String> teacherLogins = new ArrayList<>();

    @JsonProperty("teachers")
    private List<UserProfileInfo> teachers = new ArrayList<>();

    public DisciplineInstance(Integer id, Integer disciplineId, String title, String description,
                              String specialty, short semester, boolean isExamination, String stageCode,
                              String stageName, String studyFormCode, String studyFormShortName, List<String> teacherLogins) {

        super(disciplineId, title, description);
        this.id = id;
        this.specialty = specialty;
        this.semester = semester;
        this.isExamination = isExamination;
        this.stageCode = stageCode;
        this.stageName = stageName;
        this.studyFormCode = studyFormCode;
        this.studyFormShortName = studyFormShortName;
        this.teacherLogins = teacherLogins;
    }

    public DisciplineInstance() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public short getSemester() {
        return semester;
    }

    public void setSemester(short semester) {
        this.semester = semester;
    }

    public boolean isExamination() {
        return isExamination;
    }

    public void setExamination(boolean examination) {
        isExamination = examination;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public String getStudyFormCode() {
        return studyFormCode;
    }

    public void setStudyFormCode(String studyFormCode) {
        this.studyFormCode = studyFormCode;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStudyFormShortName() {
        return studyFormShortName;
    }

    public void setStudyFormShortName(String studyFormShortName) {
        this.studyFormShortName = studyFormShortName;
    }

    public List<String> getTeacherLogins() {
        return teacherLogins;
    }

    public void setTeacherLogins(List<String> teacherLogins) {
        this.teacherLogins = teacherLogins;
    }

    public List<UserProfileInfo> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<UserProfileInfo> teachers) {
        this.teachers = teachers;
    }
}
