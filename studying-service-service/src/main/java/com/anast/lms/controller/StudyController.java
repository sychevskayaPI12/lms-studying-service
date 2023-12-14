package com.anast.lms.controller;

import com.anast.lms.client.StudyRestService;
import com.anast.lms.model.Course;
import com.anast.lms.model.CourseFullInfoResponse;
import com.anast.lms.model.WeekScheduler;
import com.anast.lms.service.StudyService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudyController implements StudyRestService {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @Override
    public List<Course> getStudentCourses(String groupCode, Boolean isActive) {
        return studyService.getStudentCourses(groupCode, isActive);
    }

    @Override
    public List<Course> getTeacherCourses(String teacherLogin, String specialty, String form, String stage,
                                          Boolean searchActive) {
        return studyService.getTeacherCourses(teacherLogin, specialty, form, stage, searchActive);
    }

    @Override
    public List<String> getSpecialties() {
        return studyService.getSpecialties();
    }

    @Override
    public CourseFullInfoResponse getCourseFullInfo(Integer id) {
        return studyService.getCourseFullInfoById(id);
    }

    @Override
    public WeekScheduler getStudentScheduler(String groupCode, Boolean isCurrentDay) {
        return studyService.getStudentScheduler(groupCode, isCurrentDay);
    }

    @Override
    public WeekScheduler getTeacherScheduler(String login, Boolean isCurrentDay) {
        return studyService.getTeacherSchedule(login, isCurrentDay);
    }
}
