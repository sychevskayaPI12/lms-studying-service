package com.anast.lms.client;

import com.anast.lms.model.Course;
import com.anast.lms.model.CourseFullInfoResponse;
import com.anast.lms.model.WeekScheduler;
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

    @GetMapping("/study/{id}/course")
    CourseFullInfoResponse getCourseFullInfo(@PathVariable("id") Integer id);

    @GetMapping("/study/scheduler/{group_code}/student")
    WeekScheduler getStudentScheduler(@PathVariable("group_code") String groupCode,
                                      @RequestParam("is_current_day") Boolean isCurrentDay);

    @GetMapping("/study/scheduler/{login}/teacher")
    WeekScheduler getTeacherScheduler(@PathVariable("login") String login,
                                      @RequestParam("is_current_day") Boolean isCurrentDay);
}
