package com.anast.lms.service;

import com.anast.lms.generated.jooq.tables.records.GroupRecord;
import com.anast.lms.model.*;
import com.anast.lms.repository.StudyRepository;
import com.anast.lms.service.external.user.UserServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudyService {

    private final StudyRepository repository;
    private final UserServiceClient userServiceClient;

    @Value("${upload.path}")
    private String dirPath;

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
        courses.forEach(c -> logins.addAll(c.getDiscipline().getTeacherLogins()));
        Map<String, UserProfileInfo> teachersMap = getTeachersMap(logins);

       courses.forEach(course -> {
           course.getDiscipline().getTeacherLogins().forEach( login ->
                   course.getDiscipline().getTeachers().add(teachersMap.get(login))
           );
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

        //информация о преподавателях
        course.getDiscipline().getTeacherLogins().forEach(login ->
                course.getDiscipline().getTeachers().add(getTeacherProfileInfo(login)));

        List<CourseModule> modules = repository.getCourseModules(id);

        //получение файлов
        modules.forEach(module -> {
            module.getResources().forEach(res -> {
                File file = new File(dirPath + "\\" + res.getFileName());
                res.setFile(file);
            });
        });
        return new CourseFullInfoResponse(course, modules);
    }

    /**
     * Получить список занятий студента, сгруппированный по дням недели
     *
     * @param groupCode группа студента
     * @param isCurrentDay признак, искать на сегодня или на неделю
     * @return список занятий, сгруппированный по дням недели
     */
    public WeekScheduler getStudentScheduler(String groupCode, Boolean isCurrentDay) {
        GroupRecord group = repository.getGroup(groupCode);
        Short dayOfWeek = isCurrentDay ? (short) LocalDate.now().getDayOfWeek().getValue() : null;

        List<SchedulerItem> items = repository.getSchedulerItems(group, dayOfWeek);
        //информация о преподавателях
        items.forEach(item -> item.getDiscipline().getTeacherLogins().forEach(login ->
                item.getDiscipline().getTeachers().add(getTeacherProfileInfo(login))));

        Map<Short, List<SchedulerItem>> itemsWeekMap = items.stream()
                .collect(Collectors.groupingBy(SchedulerItem::getDayOfWeek));
        return new WeekScheduler(itemsWeekMap);
    }

    /**
     * Получить список занятий преподавателя, сгруппированный по дням недели
     *
     * @param login логин преподавателя
     * @param isCurrentDay признак, искать на сегодня или на неделю
     * @return список занятий, сгруппированный по дням недели
     */
    public WeekScheduler getTeacherSchedule(String login, Boolean isCurrentDay) {
        Short dayOfWeek = isCurrentDay ? (short) LocalDate.now().getDayOfWeek().getValue() : null;
        List<SchedulerItem> items = repository.getSchedulerItems(login, dayOfWeek);

        //если отдельная группа не указана, наполняем списком групп направления
        for (SchedulerItem item : items) {
            if (item.getGroups() == null || item.getGroups().isEmpty()) {
                List<String> groups = repository.getGroups(item.getDiscipline());
                item.setGroups(String.join(", ", groups));
            }
        }
        Map<Short, List<SchedulerItem>> itemsWeekMap = items.stream()
                .collect(Collectors.groupingBy(SchedulerItem::getDayOfWeek));

        return new WeekScheduler(itemsWeekMap);
    }

    private Map<String, UserProfileInfo> getTeachersMap(Set<String> logins) {
        Map<String, UserProfileInfo> teachersMap = new HashMap<>();
        for (String login : logins) {
            teachersMap.put(login, getTeacherProfileInfo(login));
        }
        return teachersMap;
    }

    private UserProfileInfo getTeacherProfileInfo(String login) {
        UserDetail userDetail = userServiceClient.getUserDetail(login);
        return buildTeacherProfileInfo(userDetail);
    }

    private UserProfileInfo buildTeacherProfileInfo(UserDetail userDetail) {
        return new UserProfileInfo(
                userDetail.getLogin(),
                userDetail.getFullName(),
                userDetail.getMail(),
                null,
                repository.getTeacherProfileInfo(userDetail.getLogin()));
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
