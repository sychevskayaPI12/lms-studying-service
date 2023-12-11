package com.anast.lms.client;

import com.anast.lms.model.Course;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
}
