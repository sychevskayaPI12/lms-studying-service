package com.anast.lms.repository;

import com.anast.lms.generated.jooq.Tables;
import com.anast.lms.generated.jooq.tables.records.*;
import com.anast.lms.model.*;
import com.anast.lms.model.profile.*;
import com.anast.lms.model.course.*;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.anast.lms.generated.jooq.Tables.*;
import static com.anast.lms.generated.jooq.tables.Group.GROUP;

@Repository
public class StudyRepository {

    private final DSLContext context;

    public StudyRepository(DSLContext context) {
        this.context = context;
    }


    public StudentProfileInfo getStudentInfo(String login) {
        return context.selectFrom(STUDENT).where(STUDENT.LOGIN.eq(login))
                .fetchAny(this::mapStudentRecord);
    }

    public TeacherProfileInfo getTeacherInfo(String login) {
        return context.selectFrom(TEACHER).where(TEACHER.LOGIN.eq(login))
                .fetchAny(this::mapTeacherInfo);
    }


    public GroupRecord getGroup(String groupCode) {
        return context.selectFrom(Tables.GROUP).where(GROUP.CODE.eq(groupCode)).fetchAny();
    }

    /**
     * Выборка курсов для студента
     */
    public List<Course> getStudentCourses(GroupRecord group, int semester, Boolean searchActive) {

        Condition condition = DSL.trueCondition();
        condition = condition.and(COURSE.IS_TEMPLATE.eq(false));
        condition = condition.and(DISCIPLINE.SPECIALTY_CODE.eq(group.getSpecialtyCode()));
        condition = condition.and(DISCIPLINE.STAGE_CODE.eq(group.getStageCode()));
        condition = condition.and(DISCIPLINE.STUDY_FORM.eq(group.getStudyFormCode()));

        if(searchActive != null && searchActive) {
            condition = condition.and(DISCIPLINE.SEMESTER.eq((short) semester));
        }

        return getCourses(condition, searchActive);
    }

    /**
     * Выборка курсов для преподавателя
     */
    public List<Course> getTeacherCourses(String teacherLogin, String specialty,
                                          String form, String stage, Boolean searchActive) {

        List<Integer> disciplineIds = getDisciplinesByTeacherId(teacherLogin);

        Condition condition = DSL.trueCondition();
        condition = condition.and(DISCIPLINE.ID.in(disciplineIds));

        if(specialty != null) {
            condition = condition.and(DISCIPLINE.SPECIALTY_CODE.containsIgnoreCase(specialty));
        }
        if(form != null) {
            condition = condition.and(DISCIPLINE.STUDY_FORM.containsIgnoreCase(form));
        }
        if(stage != null) {
            condition = condition.and(DISCIPLINE.STAGE_CODE.containsIgnoreCase(stage));
        }

        return getCourses(condition, searchActive);


    }

    private List<Course> getCourses(Condition condition, Boolean searchActive) {
        //если null, ищем все
        if(searchActive != null) {
            if(searchActive) {
                condition = condition.and(COURSE.START_DATE.le(LocalDate.now()));
                condition = condition.and(COURSE.END_DATE.ge(LocalDate.now()));
            } else {
                condition = condition.and(COURSE.END_DATE.lessThan(LocalDate.now()));
            }
        }

        return context.selectFrom(COURSE
                .leftJoin(DISCIPLINE).on(COURSE.DISCIPLINE_ID.eq(DISCIPLINE.ID))
                .leftJoin(DISCIPLINE_DESCRIPTOR).on(DISCIPLINE.DISCIPLINE_DESCR_ID.eq(DISCIPLINE_DESCRIPTOR.ID)))
                .where(condition)
                .fetch(this::mapCourseRecord);
    }

    private List<Integer> getDisciplinesByTeacherId(String teacherLogin) {
        Integer teacherId = context.select(TEACHER.ID)
                .from(TEACHER)
                .where(TEACHER.LOGIN.eq(teacherLogin)).fetchAny().component1();
        return context.select(TEACHER_DISCIPLINE_LINK.DISCIPLINE_ID)
                .from(TEACHER_DISCIPLINE_LINK)
                .where(TEACHER_DISCIPLINE_LINK.TEACHER_ID.eq(teacherId))
                .fetch().getValues(TEACHER_DISCIPLINE_LINK.DISCIPLINE_ID);
    }

    public List<String> getSpecialties() {
        return context.selectFrom(SPECIALTY)
                .fetch()
                .getValues(SPECIALTY.CODE);
    }

    public List<Stage> getStages() {
        return context.selectFrom(STAGE)
                .fetch(this::mapSage);
    }

    public List<StudyForm> getStudyForms() {
        return context.selectFrom(STUDY_FORM)
                .fetch(this::mapStudyForm);
    }

    public Course getCourseById(Integer id) {
        return context.selectFrom(COURSE
                .leftJoin(DISCIPLINE).on(COURSE.DISCIPLINE_ID.eq(DISCIPLINE.ID))
                .leftJoin(DISCIPLINE_DESCRIPTOR).on(DISCIPLINE.DISCIPLINE_DESCR_ID.eq(DISCIPLINE_DESCRIPTOR.ID)))
                .where(COURSE.ID.eq(id))
                .fetchAny(this::mapCourseRecord);
    }


    /**
     * Расписание студента на день недели. Если день не передан, то на неделю
     */
    public List<SchedulerItem> getSchedulerItems(GroupRecord group, Short dayOfWeek) {

        Condition condition = DSL.trueCondition();
        condition = condition.and(DISCIPLINE.SPECIALTY_CODE.eq(group.getSpecialtyCode()))
                .and(DISCIPLINE.STAGE_CODE.eq(group.getStageCode()))
                .and(DISCIPLINE.STUDY_FORM.eq(group.getStudyFormCode()));
        if(dayOfWeek != null) {
            condition = condition.and(STATIC_SCHEDULE.DAY_OF_WEEK.eq(dayOfWeek));
        }
        return context.selectFrom(STATIC_SCHEDULE
                .leftJoin(STUDY_CLASS).on(STATIC_SCHEDULE.CLASS_ID.eq(STUDY_CLASS.ID))
                .leftJoin(DISCIPLINE).on(STUDY_CLASS.DISCIPLINE_ID.eq(DISCIPLINE.ID))
                .leftJoin(DISCIPLINE_DESCRIPTOR).on(DISCIPLINE_DESCRIPTOR.ID.eq(DISCIPLINE.DISCIPLINE_DESCR_ID)))
                .where(condition)
                .fetch(this::mapScheduleItem);
    }

    public List<SchedulerItem> getSchedulerItems(String teacherLogin, Short dayOfWeek) {

        List<Integer> disciplineIds = getDisciplinesByTeacherId(teacherLogin);

        Condition condition = DSL.trueCondition();
        condition = condition.and(DISCIPLINE.ID.in(disciplineIds));

        if(dayOfWeek != null) {
            condition = condition.and(STATIC_SCHEDULE.DAY_OF_WEEK.eq(dayOfWeek));
        }
        return context.selectFrom(STATIC_SCHEDULE
                .leftJoin(STUDY_CLASS).on(STATIC_SCHEDULE.CLASS_ID.eq(STUDY_CLASS.ID))
                .leftJoin(DISCIPLINE).on(STUDY_CLASS.DISCIPLINE_ID.eq(DISCIPLINE.ID))
                .leftJoin(DISCIPLINE_DESCRIPTOR).on(DISCIPLINE_DESCRIPTOR.ID.eq(DISCIPLINE.DISCIPLINE_DESCR_ID)))
                .where(condition)
                .fetch(this::mapScheduleItem);
    }

    /**
     * Получть список групп потока
     * @param discipline - экземпляр дисциплины. содержит параметры для поиска групп потока
     * @return список кодов групп
     */
    public List<String> getGroups(DisciplineInstance discipline, short year) {
        return getGroups(discipline.getSpecialty(), discipline.getStageCode(),
                discipline.getStudyFormCode(), year);
    }

    public List<String> getGroups(String specialty, String stage, String studyForm, short year) {
        return context.selectFrom(GROUP)
                .where(GROUP.SPECIALTY_CODE.eq(specialty)
                        .and(GROUP.STAGE_CODE.eq(stage))
                        .and(GROUP.STUDY_FORM_CODE.eq(studyForm))
                        .and(GROUP.ENTRY_YEAR.eq(year)))
                .fetch().getValues(GROUP.CODE);
    }

    public TeacherProfileInfo getTeacherProfileInfo(String login) {
        //todo degrees
        TeacherRecord record = context.selectFrom(TEACHER).where(TEACHER.LOGIN.eq(login)).fetchAny();
        return new TeacherProfileInfo();
    }

    private Course mapCourseRecord(Record r) {
        return new Course(
                r.getValue(COURSE.ID),
                r.getValue(COURSE.START_DATE),
                r.getValue(COURSE.END_DATE),
                mapDisciplineInstance(r)
        );
    }

    private DisciplineInstance mapDisciplineInstance(Record r) {
        String stageName = context.selectFrom(STAGE)
                .where(STAGE.CODE.eq(r.getValue(DISCIPLINE.STAGE_CODE)))
                .fetchAny().getTitle();
        String studyFormName = context.selectFrom(STUDY_FORM)
                .where(STUDY_FORM.CODE.eq(r.getValue(DISCIPLINE.STUDY_FORM)))
                .fetchAny().getDescription();

        List<String> teacherLogins = context.selectFrom(TEACHER
                .leftJoin(TEACHER_DISCIPLINE_LINK).on(TEACHER_DISCIPLINE_LINK.TEACHER_ID.eq(TEACHER.ID)))
                .where(TEACHER_DISCIPLINE_LINK.DISCIPLINE_ID.eq(r.getValue(DISCIPLINE_DESCRIPTOR.ID)))
                .fetch().getValues(TEACHER.LOGIN);

        return new DisciplineInstance(
                r.getValue(DISCIPLINE.ID),
                r.getValue(DISCIPLINE_DESCRIPTOR.ID),
                r.getValue(DISCIPLINE_DESCRIPTOR.TITLE),
                r.getValue(DISCIPLINE_DESCRIPTOR.DESCRIPTION),
                r.getValue(DISCIPLINE.SPECIALTY_CODE),
                r.getValue(DISCIPLINE.SEMESTER),
                r.getValue(DISCIPLINE.IS_EXAMINATION),
                r.getValue(DISCIPLINE.STAGE_CODE),
                stageName,
                r.getValue(DISCIPLINE.STUDY_FORM),
                studyFormName,
                teacherLogins);
    }

    private SchedulerItem mapScheduleItem(Record record) {
        return new SchedulerItem(
                mapDisciplineInstance(record),
                record.getValue(STATIC_SCHEDULE.CLASSROOM),
                record.getValue(STUDY_CLASS.GROUP_CODE),
                ClassType.getEnum(record.getValue(STUDY_CLASS.CLASS_TYPE_CODE)),
                record.getValue(STATIC_SCHEDULE.LESSON_NUMBER),
                record.getValue(STATIC_SCHEDULE.DAY_OF_WEEK));
    }


    private Stage mapSage(StageRecord stageRecord) {
        return new Stage(stageRecord.getCode(), stageRecord.getTitle());
    }

    private StudyForm mapStudyForm(StudyFormRecord record) {
        return new StudyForm(record.getCode(), record.getTitle(), record.getDescription());
    }

    private StudentProfileInfo mapStudentRecord(StudentRecord record) {
        return new StudentProfileInfo(record.getGroupCode());
    }
    private TeacherProfileInfo mapTeacherInfo(TeacherRecord record) {
        //todo
        return new TeacherProfileInfo();
    }
}
