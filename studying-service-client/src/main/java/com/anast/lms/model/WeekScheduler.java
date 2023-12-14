package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class WeekScheduler {

    @JsonProperty("week_classes")
    private Map<Short, List<SchedulerItem>> weekClasses;

    @JsonProperty("is_even_week")
    private Boolean isEvenWeek;

    public WeekScheduler() {
    }

    public WeekScheduler(Map<Short, List<SchedulerItem>> weekClasses, Boolean isEvenWeek) {
        this.weekClasses = weekClasses;
        this.isEvenWeek = isEvenWeek;
    }

    public WeekScheduler(Map<Short, List<SchedulerItem>> weekClasses) {
        this.weekClasses = weekClasses;
    }

    public Map<Short, List<SchedulerItem>> getWeekClasses() {
        return weekClasses;
    }

    public void setWeekClasses(Map<Short, List<SchedulerItem>> weekClasses) {
        this.weekClasses = weekClasses;
    }

    public Boolean getEvenWeek() {
        return isEvenWeek;
    }

    public void setEvenWeek(Boolean evenWeek) {
        isEvenWeek = evenWeek;
    }

}
