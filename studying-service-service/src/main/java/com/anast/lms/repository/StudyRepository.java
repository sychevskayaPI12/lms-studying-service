package com.anast.lms.repository;

import com.anast.lms.generated.jooq.Tables;
import com.anast.lms.generated.jooq.tables.records.GroupRecord;
import com.anast.lms.generated.jooq.tables.records.ModuleRecord;
import com.anast.lms.model.Course;
import com.anast.lms.model.CourseModule;
import com.anast.lms.model.DisciplineInstance;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static com.anast.lms.generated.jooq.Tables.*;
import static com.anast.lms.generated.jooq.tables.Group.GROUP;

@Repository
public class StudyRepository {

    private final DSLContext context;

    public StudyRepository(DSLContext context) {
        this.context = context;
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

        Integer teacherId = context.select(TEACHER.ID)
                .from(TEACHER)
                .where(TEACHER.LOGIN.eq(teacherLogin)).fetchAny().component1();
        List<Integer> disciplineIds = context.select(TEACHER_DISCIPLINE_LINK.DISCIPLINE_ID)
                .from(TEACHER_DISCIPLINE_LINK)
                .where(TEACHER_DISCIPLINE_LINK.TEACHER_ID.eq(teacherId))
                .fetch().getValues(TEACHER_DISCIPLINE_LINK.DISCIPLINE_ID);

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

    public List<String> getSpecialties() {
        return context.selectFrom(SPECIALTY)
                .fetch()
                .getValues(SPECIALTY.CODE);
    }

    public Course getCourseById(Integer id) {
        return context.selectFrom(COURSE
                .leftJoin(DISCIPLINE).on(COURSE.DISCIPLINE_ID.eq(DISCIPLINE.ID))
                .leftJoin(DISCIPLINE_DESCRIPTOR).on(DISCIPLINE.DISCIPLINE_DESCR_ID.eq(DISCIPLINE_DESCRIPTOR.ID)))
                .where(COURSE.ID.eq(id))
                .fetchAny(this::mapCourseRecord);
    }

    public List<CourseModule> getCourseModules(Integer courseId) {
        return context.selectFrom(MODULE).where(MODULE.COURSE_ID.eq(courseId))
                .orderBy(MODULE.MODULE_ORDER)
                .fetch(this::mapModule);
    }

    private Course mapCourseRecord(Record r) {

        String stageName = context.selectFrom(STAGE)
                .where(STAGE.CODE.eq(r.getValue(DISCIPLINE.STAGE_CODE)))
                .fetchAny().getTitle();
        String studyFormName = context.selectFrom(STUDY_FORM)
                .where(STUDY_FORM.CODE.eq(r.getValue(DISCIPLINE.STUDY_FORM)))
                .fetchAny().getDescription();

        DisciplineInstance disciplineInstance = new DisciplineInstance(
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
                studyFormName);

        Course course = new Course(
                r.getValue(COURSE.ID),
                r.getValue(COURSE.START_DATE),
                r.getValue(COURSE.END_DATE),
                disciplineInstance
        );

        List<String> teacherLogins = context.selectFrom(TEACHER
                .leftJoin(TEACHER_DISCIPLINE_LINK).on(TEACHER_DISCIPLINE_LINK.TEACHER_ID.eq(TEACHER.ID)))
                .where(TEACHER_DISCIPLINE_LINK.DISCIPLINE_ID.eq(course.getDiscipline().getId()))
                .fetch().getValues(TEACHER.LOGIN);

        course.setTeacherLogins(teacherLogins);
        return course;
    }

    private CourseModule mapModule(ModuleRecord record) {
        return new CourseModule(
                record.getId(),
                record.getTitle(),
                record.getContent(),
                record.getModuleOrder());
    }
}
