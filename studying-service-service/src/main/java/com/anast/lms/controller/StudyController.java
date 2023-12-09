package com.anast.lms.controller;

import com.anast.lms.client.StudyRestService;
import com.anast.lms.service.StudyService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudyController implements StudyRestService {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @Override
    public void getStudentCourser(String groupCode, Boolean isActive) {
        studyService.getStudentCourses(groupCode, isActive);
    }
}
