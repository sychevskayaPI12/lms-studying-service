package com.anast.lms.repository;

import com.anast.lms.generated.jooq.Tables;
import com.anast.lms.generated.jooq.tables.records.*;
import com.anast.lms.model.*;
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

    public List<CourseModule> getCourseModules(Integer courseId) {
        return context.selectFrom(MODULE).where(MODULE.COURSE_ID.eq(courseId))
                .orderBy(MODULE.MODULE_ORDER)
                .fetch(this::mapModule);
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
    public List<String> getGroups(DisciplineInstance discipline) {
        //todo вычислить год поступления по семестру, добавить
        return getGroups(discipline.getSpecialty(), discipline.getStageCode(),
                discipline.getStudyFormCode(), (short) 2021);
    }

    public List<String> getGroups(String specialty, String stage, String studyForm, short year) {
        return context.selectFrom(GROUP)
                .where(GROUP.SPECIALTY_CODE.eq(specialty)
                        .and(GROUP.STAGE_CODE.eq(stage))
                        .and(GROUP.STUDY_FORM_CODE.eq(studyForm)))
                //todo год поступления
                .fetch().getValues(GROUP.CODE);
    }

    public TeacherProfileInfo getTeacherProfileInfo(String login) {
        TeacherRecord record = context.selectFrom(TEACHER).where(TEACHER.LOGIN.eq(login)).fetchAny();
        return new TeacherProfileInfo(record.getDegree());
    }

    public List<ModuleResource> getModuleResources(Integer moduleId) {
        return context.selectFrom(MODULE_RESOURCE)
                .where(MODULE_RESOURCE.ID.in(
                        context.select(MODULE_RESOURCE_LINK.RESOURCE_ID).from(MODULE_RESOURCE_LINK)
                        .where(MODULE_RESOURCE_LINK.MODULE_ID.eq(moduleId))
                ))
                .fetch(this::mapModuleResource);
    }

    public List<Task> getModuleTasks(Integer moduleId) {
        return context.selectFrom(TASK)
                .where(TASK.ID.in(
                        context.select(MODULE_TASK_LINK.TASK_ID).from(MODULE_TASK_LINK)
                        .where(MODULE_TASK_LINK.MODULE_ID.eq(moduleId))
                ))
                .fetch(this::mapTask);
    }

    public List<ModuleResource> getTaskResources(Integer taskId) {
        return context.selectFrom(MODULE_RESOURCE)
                .where(MODULE_RESOURCE.ID.in(
                        context.select(TASK_RESOURCE_LINK.RESOURCE_ID).from(TASK_RESOURCE_LINK)
                                .where(TASK_RESOURCE_LINK.TASK_ID.eq(taskId))
                ))
                .fetch(this::mapModuleResource);
    }

    public void updateCourseModule(CourseModule module) {
        context.update(MODULE)
                .set(MODULE.TITLE, module.getTitle())
                .set(MODULE.MODULE_ORDER, module.getOrder())
                .set(MODULE.CONTENT, module.getContent())
                .where(MODULE.ID.eq(module.getId()))
                .execute();
    }

    public Integer createCourseModule(CourseModule module, Integer courseId) {
        return context.insertInto(MODULE)
                .set(MODULE.TITLE, module.getTitle())
                .set(MODULE.MODULE_ORDER, module.getOrder())
                .set(MODULE.CONTENT, module.getContent())
                .set(MODULE.COURSE_ID, courseId)
                .returningResult(MODULE.ID)
                .fetchOne().component1();
    }

    public void deleteModuleResources(Integer moduleId) {

        List<Integer> resourcesId = context
                .select(MODULE_RESOURCE_LINK.RESOURCE_ID)
                .from(MODULE_RESOURCE_LINK)
                .where(MODULE_RESOURCE_LINK.MODULE_ID.eq(moduleId))
                .fetch(MODULE_RESOURCE_LINK.RESOURCE_ID);

        context.deleteFrom(MODULE_RESOURCE_LINK)
                .where(MODULE_RESOURCE_LINK.MODULE_ID.eq(moduleId))
                .execute();
        context.deleteFrom(MODULE_RESOURCE)
                .where(MODULE_RESOURCE.ID.in(resourcesId))
                .execute();
    }

    public void deleteResources(Set<Integer> ids) {
        context.deleteFrom(MODULE_RESOURCE_LINK)
                .where(MODULE_RESOURCE_LINK.RESOURCE_ID.in(ids))
                .execute();
        context.deleteFrom(MODULE_RESOURCE)
                .where(MODULE_RESOURCE.ID.in(ids))
                .execute();
    }

    public void deleteModules(Set<Integer> ids) {
        context.deleteFrom(MODULE)
                .where(MODULE.ID.in(ids))
                .execute();
    }

    /**
     * Сохранение зпаисей о ресурсах модуля
     */
    public void createModuleResources(List<ModuleResource> resources, Integer moduleId) {
        for(ModuleResource resource : resources) {
            createModuleResource(resource, moduleId);
        }
    }

    /**
     * Сохранение зпаисей о ресурсах задачи
     */
    public void createTaskResources(List<ModuleResource> resources, Integer taskId) {
        for(ModuleResource resource : resources) {
            createTaskResource(resource, taskId);
        }
    }

    public void createModuleResource(ModuleResource resource, Integer moduleId) {

        Integer resourceId = context.insertInto(MODULE_RESOURCE)
                .set(MODULE_RESOURCE.DISPLAY_FILE_NAME, resource.getDisplayFileName())
                .set(MODULE_RESOURCE.STORE_FILE_NAME, resource.getFileName())
                .returningResult(MODULE_RESOURCE.ID)
                .fetchOne().component1();

        context.insertInto(MODULE_RESOURCE_LINK)
                .set(MODULE_RESOURCE_LINK.MODULE_ID, moduleId)
                .set(MODULE_RESOURCE_LINK.RESOURCE_ID, resourceId)
                .execute();
    }

    public void createTaskResource(ModuleResource resource, Integer taskId) {
        Integer resourceId = context.insertInto(MODULE_RESOURCE)
                .set(MODULE_RESOURCE.DISPLAY_FILE_NAME, resource.getDisplayFileName())
                .set(MODULE_RESOURCE.STORE_FILE_NAME, resource.getFileName())
                .returningResult(MODULE_RESOURCE.ID)
                .fetchOne().component1();

        context.insertInto(TASK_RESOURCE_LINK)
                .set(TASK_RESOURCE_LINK.TASK_ID, taskId)
                .set(TASK_RESOURCE_LINK.RESOURCE_ID, resourceId)
                .execute();
    }

    public void deleteTaskResources(Set<Integer> tasksIds) {

        List<Integer> resourcesIds =
                context.select(TASK_RESOURCE_LINK.RESOURCE_ID)
                .from(TASK_RESOURCE_LINK)
                 .where(TASK_RESOURCE_LINK.TASK_ID.in(tasksIds))
                .fetch(TASK_RESOURCE_LINK.RESOURCE_ID);

        context.deleteFrom(TASK_RESOURCE_LINK)
                .where(TASK_RESOURCE_LINK.TASK_ID.in(tasksIds))
                .execute();

        context.deleteFrom(MODULE_RESOURCE)
                .where(MODULE_RESOURCE.ID.in(resourcesIds))
                .execute();
    }

    public void deleteTasks(Set<Integer> tasksIds) {
        context.deleteFrom(MODULE_TASK_LINK)
                .where(MODULE_TASK_LINK.TASK_ID.in(tasksIds))
                .execute();
        context.deleteFrom(TASK)
                .where(TASK.ID.in(tasksIds))
                .execute();
    }

    public Integer createTask(Task task, Integer moduleId) {
        Integer taskId = context.insertInto(TASK)
                .set(TASK.TITLE, task.getTitle())
                .set(TASK.DESCRIPTION, task.getDescription())
                .set(TASK.DEADLINE, task.getDeadLine())
                .set(TASK.TYPE_CODE, task.getTaskType())
                .returningResult(TASK.ID)
                .fetchOne().component1();

        context.insertInto(MODULE_TASK_LINK)
                .set(MODULE_TASK_LINK.TASK_ID, taskId)
                .set(MODULE_TASK_LINK.MODULE_ID, moduleId)
                .execute();

        return taskId;
    }

    public void updateTask(Task task) {
        context.update(TASK)
                .set(TASK.TITLE, task.getTitle())
                .set(TASK.DESCRIPTION, task.getDescription())
                .set(TASK.DEADLINE, task.getDeadLine())
                .set(TASK.TYPE_CODE, task.getTaskType())
                .where(TASK.ID.eq(task.getId()))
                .execute();
    }

    public void deleteModuleTasks(Integer moduleId) {
        List<Integer> tasksId = context.select(MODULE_TASK_LINK.TASK_ID)
                .from(MODULE_TASK_LINK)
                .where(MODULE_TASK_LINK.MODULE_ID.eq(moduleId))
                .fetch(MODULE_TASK_LINK.TASK_ID);
        Set<Integer> tasksIdSet = new HashSet<>(tasksId);
        deleteTaskResources(tasksIdSet);
        deleteTasks(tasksIdSet);
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

    private CourseModule mapModule(ModuleRecord record) {
        List<ModuleResource> moduleResources = getModuleResources(record.getId());
        List<Task> tasks = getModuleTasks(record.getId());
        return new CourseModule(
                record.getId(),
                record.getTitle(),
                record.getContent(),
                record.getModuleOrder(),
                moduleResources,
                tasks);
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

    private ModuleResource mapModuleResource(ModuleResourceRecord record) {
        return new ModuleResource(
                record.getId(),
                record.getStoreFileName(),
                record.getDisplayFileName()
        );
    }

    private Task mapTask(TaskRecord record) {
        List<ModuleResource> taskResources = getTaskResources(record.getId());
        return new Task(
              record.getId(),
              record.getTypeCode(),
              record.getTitle(),
              record.getDescription(),
              record.getDeadline(),
              taskResources
        );
    }

    private Stage mapSage(StageRecord stageRecord) {
        return new Stage(stageRecord.getCode(), stageRecord.getTitle());
    }

    private StudyForm mapStudyForm(StudyFormRecord record) {
        return new StudyForm(record.getCode(), record.getTitle(), record.getDescription());
    }
}
