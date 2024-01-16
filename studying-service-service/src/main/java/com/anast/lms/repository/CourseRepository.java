package com.anast.lms.repository;

import com.anast.lms.generated.jooq.tables.records.ModuleRecord;
import com.anast.lms.generated.jooq.tables.records.ModuleResourceRecord;
import com.anast.lms.generated.jooq.tables.records.TaskRecord;
import com.anast.lms.model.course.CourseModule;
import com.anast.lms.model.course.ModuleResource;
import com.anast.lms.model.course.Task;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.anast.lms.generated.jooq.Tables.*;
import static com.anast.lms.generated.jooq.Tables.TASK_RESOURCE_LINK;

@Repository
public class CourseRepository {

    private final DSLContext context;

    public CourseRepository(DSLContext context) {
        this.context = context;
    }


    public List<CourseModule> getCourseModules(Integer courseId) {
        return context.selectFrom(MODULE).where(MODULE.COURSE_ID.eq(courseId))
                .orderBy(MODULE.MODULE_ORDER)
                .fetch(this::mapModule);
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

    public Integer createCourseModule(CourseModule module, Integer courseId) {
        return context.insertInto(MODULE)
                .set(MODULE.TITLE, module.getTitle())
                .set(MODULE.MODULE_ORDER, module.getOrder())
                .set(MODULE.CONTENT, module.getContent())
                .set(MODULE.COURSE_ID, courseId)
                .returningResult(MODULE.ID)
                .fetchOne().component1();
    }

    public void updateCourseModule(CourseModule module) {
        context.update(MODULE)
                .set(MODULE.TITLE, module.getTitle())
                .set(MODULE.MODULE_ORDER, module.getOrder())
                .set(MODULE.CONTENT, module.getContent())
                .where(MODULE.ID.eq(module.getId()))
                .execute();
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
}
