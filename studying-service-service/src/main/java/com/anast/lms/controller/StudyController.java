package com.anast.lms.controller;

import com.anast.lms.client.StudyRestService;
import com.anast.lms.model.Course;
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
    public List<Course> getStudentCourser(String groupCode, Boolean isActive) {
        return studyService.getStudentCourses(groupCode, isActive);
    }
}
