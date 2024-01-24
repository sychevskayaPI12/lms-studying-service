package com.anast.lms.service;

import com.anast.lms.generated.jooq.tables.records.GroupRecord;
import com.anast.lms.model.*;
import com.anast.lms.model.course.Course;
import com.anast.lms.model.profile.*;
import com.anast.lms.repository.StudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudyService {

    private final StudyRepository repository;
    private final ProfileService profileService;


    public StudyService(StudyRepository repository, ProfileService profileService) {
        this.repository = repository;
        this.profileService = profileService;
    }

    public int calcStudentCourse(String groupCode) {
        GroupRecord groupRecord = repository.getGroup(groupCode);
        short year = groupRecord.getEntryYear();
        int currentYear = LocalDate.now().getYear();
        return currentYear - year + getAdditionalSemCoef();
    }

    public DisciplineInstance getDisciplineInstance(Integer id) {
        return repository.getDisciplineInstanceById(id);
    }

    public List<DisciplineInstance> getTeacherDisciplines(String login) {
        return repository.getDisciplinesByTeacherId(login);
    }

    public List<Course> getStudentCourses(String groupCode, Boolean isActive) {
        GroupRecord group = repository.getGroup(groupCode);
        int studentCourseNum = calcStudentCourse(groupCode);
        int currentStudentSemester = studentCourseNum * 2 - getAdditionalSemCoef();

        List<Course> courses = repository.getStudentCourses(group, currentStudentSemester, isActive);

        Set<String> logins = new HashSet<>();
        courses.forEach(c -> logins.addAll(c.getDiscipline().getTeacherLogins()));
        Map<String, UserProfile> teachersMap = getTeachersMap(logins);

       courses.forEach(course -> {
           course.getDiscipline().getTeacherLogins().forEach( login ->
                   course.getDiscipline().getTeachers().add(teachersMap.get(login).getUserProfileInfo())
           );
       });


        return courses;
    }

    public List<Course> getTeacherCourses(String teacherLogin, String specialty,
                                          String form, String stage, Boolean searchActive) {
        return repository.getTeacherCourses(teacherLogin, specialty, form, stage, searchActive);

    }

    public Course getCourseById(Integer id) {
        Course course = repository.getCourseById(id);
        //информация о преподавателях
        course.getDiscipline().getTeacherLogins().forEach(login ->
                course.getDiscipline().getTeachers().add(getUserProfile(login).getUserProfileInfo()));
        return course;
    }

    public List<String> getSpecialties() {
        return repository.getSpecialties();
    }

    public List<Stage> getStages() {
        return repository.getStages();
    }

    public List<StudyForm> getStudyForms() {
        return repository.getStudyForms();
    }

    public List<String> getGroups(String specialty, String stage, String studyForm, Integer currentCourseNum) {

        short entryYear = calcEntryYearFromCourseNumber(currentCourseNum);
        return repository.getGroups(specialty, stage, studyForm, entryYear);
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
                item.getDiscipline().getTeachers().add(getUserProfile(login).getUserProfileInfo())));

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
                DisciplineInstance discipline = item.getDiscipline();
                short entryYear = calcEntryYearFromSemesterNumber(discipline.getSemester());
                List<String> groups = repository.getGroups(discipline, entryYear);
                item.setGroups(String.join(", ", groups));
            }
        }
        Map<Short, List<SchedulerItem>> itemsWeekMap = items.stream()
                .collect(Collectors.groupingBy(SchedulerItem::getDayOfWeek));

        return new WeekScheduler(itemsWeekMap);
    }


    private Map<String, UserProfile> getTeachersMap(Set<String> logins) {
        Map<String, UserProfile> teachersMap = new HashMap<>();
        for (String login : logins) {
            teachersMap.put(login, getUserProfile(login));
        }
        return teachersMap;
    }

    public UserProfile getUserProfile(String login) {
        UserProfileInfo profileInfo = profileService.getUserProfileInfo(login);

        TeacherProfileInfo teacherInfo = getTeacherInfo(login);
        StudentProfileInfo studentInfo = getStudentInfo(login);

        return new UserProfile(profileInfo, studentInfo, teacherInfo);
    }

    @Transactional
    public void saveUserProfile(UserProfile userProfile) {
        String login = userProfile.getUserProfileInfo().getLogin();

        if(userProfile.getTeacherInfo() != null) {
            repository.saveTeacher(userProfile.getTeacherInfo(), login);
        }
        if(userProfile.getStudentInfo() != null) {
            repository.saveStudent(userProfile.getStudentInfo(), login);
        }
    }

    @Transactional
    public void deleteUserProfile(String login) {
        repository.deleteStudent(login);
        repository.deleteTeacher(login);
    }

    public List<Department> getDepartments() {
        return repository.getDepartments();
    }

    public List<FacultyPosition> getFacultyPositions() {
        return repository.getFacultyPositions();
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

    private short calcEntryYearFromCourseNumber(Integer courseNum) {
        return (short) (LocalDate.now().getYear() - courseNum + getAdditionalSemCoef());
    }

    private short calcEntryYearFromSemesterNumber(short semester) {
        short courseNum = (short) Math.round(semester / 2.0);
        return (short) (LocalDate.now().getYear() - courseNum);
    }

    @Transactional
    public TeacherProfileInfo getTeacherInfo(String login) {
        TeacherProfileInfo teacherProfileInfo = repository.getTeacherInfo(login);
        if(teacherProfileInfo != null) {
            teacherProfileInfo.setPositions(repository.getTeacherPositions(login));
        }
        return teacherProfileInfo;
    }

    public StudentProfileInfo getStudentInfo(String login) {
        StudentProfileInfo studentInfo = repository.getStudentInfo(login);
        if(studentInfo != null) {
            studentInfo.setCourse(calcStudentCourse(studentInfo.getGroupCode()));
        }
        return studentInfo;
    }
}
