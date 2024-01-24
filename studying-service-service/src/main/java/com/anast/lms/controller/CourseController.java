package com.anast.lms.controller;

import com.anast.lms.client.CourseRestService;
import com.anast.lms.model.*;
import com.anast.lms.model.course.*;
import com.anast.lms.service.CourseService;
import com.anast.lms.service.FilesService;
import com.anast.lms.service.StudyService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController implements CourseRestService {

    private final StudyService studyService;
    private final CourseService courseService;
    private final FilesService filesService;

    public CourseController(StudyService studyService, CourseService courseService, FilesService filesService) {
        this.studyService = studyService;
        this.courseService = courseService;
        this.filesService = filesService;
    }

    @Override
    public Course createNewCourse(Integer disciplineId) {
        return courseService.createNewCourse(disciplineId);
    }

    @Override
    public void updateCourseModules(Integer courseId, ModulesUpdateRequest modulesUpdateRequest) {
        courseService.updateCourseModules(modulesUpdateRequest, courseId);
    }

    @Override
    public ResponseEntity<byte[]> getFileData(ModuleResource resource) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", resource.getDisplayFileName());

        return new ResponseEntity<>(filesService.getFileDataBytes(resource), headers, HttpStatus.OK);
    }

    @Override
    public void uploadFileData(byte[] fileData, String fileName) {

        CustomMultipartFile multipartFile = new CustomMultipartFile(fileData, fileName);
        filesService.uploadFileData(multipartFile);
    }

    @Override
    public CourseFullInfoResponse getCourseFullInfo(Integer id) {
        return courseService.getCourseFullInfoById(id);
    }
}
