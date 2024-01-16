package com.anast.lms.model.course;

import com.anast.lms.model.course.CourseModule;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModulesUpdateRequest {

    @JsonProperty("modules")
    private List<CourseModule> modules = new ArrayList<>();

    @JsonProperty("deleted_modules")
    private Set<Integer> deletedModulesId = new HashSet<>();

    @JsonProperty("deleted_resources")
    private Set<Integer> deletedResources = new HashSet<>();

    @JsonProperty("deleted_tasks")
    private Set<Integer> deletedTasks = new HashSet<>();

    public ModulesUpdateRequest(List<CourseModule> modules, Set<Integer> deletedModulesId,
                                Set<Integer> deletedResources, Set<Integer> deletedTasks) {
        this.modules = modules;
        this.deletedModulesId = deletedModulesId;
        this.deletedResources = deletedResources;
        this.deletedTasks = deletedTasks;
    }

    public ModulesUpdateRequest() {
    }

    public List<CourseModule> getModules() {
        return modules;
    }

    public void setModules(List<CourseModule> modules) {
        this.modules = modules;
    }

    public Set<Integer> getDeletedModulesId() {
        return deletedModulesId;
    }

    public void setDeletedModulesId(Set<Integer> deletedModulesId) {
        this.deletedModulesId = deletedModulesId;
    }

    public Set<Integer> getDeletedResources() {
        return deletedResources;
    }

    public void setDeletedResources(Set<Integer> deletedResources) {
        this.deletedResources = deletedResources;
    }

    public Set<Integer> getDeletedTasks() {
        return deletedTasks;
    }

    public void setDeletedTasks(Set<Integer> deletedTasks) {
        this.deletedTasks = deletedTasks;
    }
}
