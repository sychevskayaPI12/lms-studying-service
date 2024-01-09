package com.anast.lms.client;

import com.anast.lms.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
public interface StudyRestService {

    @GetMapping("/study/{group_code}/courses/student")
    List<Course> getStudentCourses(@PathVariable("group_code") String groupCode,
                                   @RequestParam(value = "is_active", required = false) Boolean isActive);

    @GetMapping("/study/{teacher_login}/courses/teacher")
    List<Course> getTeacherCourses(@PathVariable("teacher_login") String teacherLogin,
                                   @RequestParam(value = "specialty", required = false) String specialty,
                                   @RequestParam(value = "form", required = false) String form,
                                   @RequestParam(value = "stage", required = false) String stage,
                                   @RequestParam(value = "is_active", required = false) Boolean searchActive);

    @GetMapping("/study/specialties")
    List<String> getSpecialties();

    @GetMapping("/study/stages")
    List<Stage> getStages();

    @GetMapping("/study/study_form")
    List<StudyForm> getStudyForms();

    @GetMapping("/study/groups")
    List<String> getGroups(@RequestParam("specialty") String specialty,
                           @RequestParam("stage") String stage,
                           @RequestParam("study_form") String studyForm,
                           @RequestParam("course_num") Integer currentCourseNum);

    @GetMapping("/study/{id}/course")
    CourseFullInfoResponse getCourseFullInfo(@PathVariable("id") Integer id);

    @GetMapping("/study/scheduler/{group_code}/student")
    WeekScheduler getStudentScheduler(@PathVariable("group_code") String groupCode,
                                      @RequestParam("is_current_day") Boolean isCurrentDay);

    @GetMapping("/study/scheduler/{login}/teacher")
    WeekScheduler getTeacherScheduler(@PathVariable("login") String login,
                                      @RequestParam("is_current_day") Boolean isCurrentDay);

    @PostMapping("/study/{id}/course/modules")
    void updateCourseModules(@PathVariable("id") Integer courseId,
                             @RequestBody ModulesUpdateRequest modulesUpdateRequest);

    @PostMapping(value = "/study/file")
    ResponseEntity<byte[]> getFileData(@RequestBody ModuleResource resource);

    @PostMapping("/study/file/upload/bytes")
    void uploadFileData(@RequestBody byte[] fileData, @RequestParam("file_name") String fileName);
}
