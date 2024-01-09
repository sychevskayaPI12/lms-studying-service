package com.anast.lms.controller;

import com.anast.lms.client.StudyRestService;
import com.anast.lms.model.*;
import com.anast.lms.service.StudyService;
import org.springframework.http.*;
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
    public List<Stage> getStages() {
        return studyService.getStages();
    }

    @Override
    public List<StudyForm> getStudyForms() {
        return studyService.getStudyForms();
    }

    @Override
    public List<String> getGroups(String specialty, String stage, String studyForm, Integer currentCourseNum) {
        return studyService.getGroups(specialty, stage, studyForm, currentCourseNum);
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

    @Override
    public void updateCourseModules(Integer courseId, ModulesUpdateRequest modulesUpdateRequest) {
        studyService.updateCourseModules(modulesUpdateRequest, courseId);
    }

    @Override
    public ResponseEntity<byte[]> getFileData(ModuleResource resource) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", resource.getDisplayFileName());

        return new ResponseEntity<>(studyService.getFileDataBytes(resource), headers, HttpStatus.OK);
    }

    @Override
    public void uploadFileData(byte[] fileData, String fileName) {

        CustomMultipartFile multipartFile = new CustomMultipartFile(fileData, fileName);
        studyService.uploadFileData(multipartFile);
    }
}
