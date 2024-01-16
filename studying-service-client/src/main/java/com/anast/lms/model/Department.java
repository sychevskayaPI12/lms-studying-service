package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Department {

    @JsonProperty("id")
    private Integer departmentId;

    @JsonProperty("abbreviation")
    private String abbreviation;

    @JsonProperty("short_title")
    private String shortTitle;

    @JsonProperty("full_title")
    private String fullTitle;

    public Department(Integer departmentId, String abbreviation, String shortTitle, String fullTitle) {
        this.departmentId = departmentId;
        this.abbreviation = abbreviation;
        this.shortTitle = shortTitle;
        this.fullTitle = fullTitle;
    }

    public Department() {
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }
}
