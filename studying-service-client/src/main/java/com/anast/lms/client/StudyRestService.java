package com.anast.lms.client;

import com.anast.lms.model.*;
import com.anast.lms.model.course.Course;
import com.anast.lms.model.course.CourseFullInfoResponse;
import com.anast.lms.model.profile.UserProfile;
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

    @GetMapping("/study/stages")
    List<Stage> getStages();

    @GetMapping("/study/study_form")
    List<StudyForm> getStudyForms();

    @GetMapping("/study/groups")
    List<String> getGroups(@RequestParam("specialty") String specialty,
                           @RequestParam("stage") String stage,
                           @RequestParam("study_form") String studyForm,
                           @RequestParam("course_num") Integer currentCourseNum);

    @GetMapping("/study/scheduler/{group_code}/student")
    WeekScheduler getStudentScheduler(@PathVariable("group_code") String groupCode,
                                      @RequestParam("is_current_day") Boolean isCurrentDay);

    @GetMapping("/study/scheduler/{login}/teacher")
    WeekScheduler getTeacherScheduler(@PathVariable("login") String login,
                                      @RequestParam("is_current_day") Boolean isCurrentDay);


    @GetMapping("/study/profile/{login}")
    UserProfile getUserProfileInfo(@PathVariable("login") String login);

    @PostMapping("/study/profile/")
    void saveProfileInfo(@RequestBody UserProfile profileInfo);

    @GetMapping("/study/departments")
    List<Department> getDepartments();

    @GetMapping("/study/faculty_positions")
    List<FacultyPosition> getFacultyPositions();

    @GetMapping("/study/profile/{login}/delete")
    void deleteUserProfile(@PathVariable("login") String login);
}
