package com.anast.lms.service;

import com.anast.lms.generated.jooq.tables.records.GroupRecord;
import com.anast.lms.model.Course;
import com.anast.lms.model.CourseFullInfoResponse;
import com.anast.lms.model.CourseModule;
import com.anast.lms.model.UserDetail;
import com.anast.lms.repository.StudyRepository;
import com.anast.lms.service.external.user.UserServiceClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudyService {

    private final StudyRepository repository;
    private final UserServiceClient userServiceClient;

    public StudyService(StudyRepository repository, UserServiceClient userServiceClient) {
        this.repository = repository;
        this.userServiceClient = userServiceClient;
    }

    public int calcStudentCourse(String groupCode) {
        GroupRecord groupRecord = repository.getGroup(groupCode);
        short year = groupRecord.getEntryYear();
        int currentYear = LocalDate.now().getYear();
        return currentYear - year + getAdditionalSemCoef();
    }

    public List<Course> getStudentCourses(String groupCode, Boolean isActive) {
        GroupRecord group = repository.getGroup(groupCode);
        int studentCourseNum = calcStudentCourse(groupCode);
        int currentStudentSemester = studentCourseNum * 2 - getAdditionalSemCoef();

        List<Course> courses = repository.getStudentCourses(group, currentStudentSemester, isActive);

        Set<String> logins = new HashSet<>();
        courses.forEach(c -> logins.addAll(c.getTeacherLogins()));
        Map<String, UserDetail> teachersMap = getTeachersMap(logins);

        courses.forEach(course -> {
            Map<String, String> teacherInfo = new HashMap<>();
            course.getTeacherLogins().forEach(l -> {
                teacherInfo.put(l, teachersMap.get(l).getFullName());
            });
            course.setTeachers(teacherInfo);
        });

        return courses;
    }

    public List<Course> getTeacherCourses(String teacherLogin, String specialty,
                                          String form, String stage, Boolean searchActive) {
        return repository.getTeacherCourses(teacherLogin, specialty, form, stage, searchActive);

    }

    public List<String> getSpecialties() {
        return repository.getSpecialties();
    }

    public CourseFullInfoResponse getCourseFullInfoById(Integer id) {

        Course course = repository.getCourseById(id);
        Map<String, UserDetail> teachersMap = getTeachersMap(new HashSet<>(course.getTeacherLogins()));
        course.setTeachers(
                teachersMap.entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey, e-> e.getValue().getFullName())
                )
        );
        List<CourseModule> modules = repository.getCourseModules(id);
        return new CourseFullInfoResponse(course, modules);
    }


    private Map<String, UserDetail> getTeachersMap(Set<String> logins) {
        Map<String, UserDetail> teachersMap = new HashMap<>();
        for (String login : logins) {
            teachersMap.put(login, userServiceClient.getUserDetail(login));
        }
        return teachersMap;
    }

    private int getAdditionalSemCoef() {
        int currSemNum = getCurrentSemester();
        return currSemNum == 1 ? 1 : 0;
    }

    private int getCurrentSemester() {
        LocalDate currDate = LocalDate.now();
        if(currDate.isAfter(LocalDate.of(currDate.getYear(), Month.AUGUST, 1))
                && currDate.isBefore(LocalDate.of(currDate.getYear(), Month.DECEMBER, 31)))  {
            return 1;
        } else {
            return 2;
        }
    }
}
