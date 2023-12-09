package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("study_form_code")
    private String studyFormCode;


    //todo teachers

    public DisciplineInstance(Integer id, Integer disciplineId, String title, String description,
                              String specialty, short semester, boolean isExamination, String stageCode,
                              String studyFormCode) {

        super(disciplineId, title, description);
        this.id = id;
        this.specialty = specialty;
        this.semester = semester;
        this.isExamination = isExamination;
        this.stageCode = stageCode;
        this.studyFormCode = studyFormCode;
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
}
