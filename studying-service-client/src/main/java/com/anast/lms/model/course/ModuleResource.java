package com.anast.lms.model.course;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;

public class ModuleResource {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("display_file_name")
    private String displayFileName;

    public ModuleResource(Integer id, String fileName, String displayFileName) {
        this.id = id;
        this.fileName = fileName;
        this.displayFileName = displayFileName;
    }
    public ModuleResource() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDisplayFileName() {
        return displayFileName;
    }

    public void setDisplayFileName(String displayFileName) {
        this.displayFileName = displayFileName;
    }

}
