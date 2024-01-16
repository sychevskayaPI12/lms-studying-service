package com.anast.lms.service;

import com.anast.lms.model.course.*;
import com.anast.lms.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudyService studyService;

    public CourseService(CourseRepository courseRepository, StudyService studyService) {
        this.courseRepository = courseRepository;
        this.studyService = studyService;
    }

    public CourseFullInfoResponse getCourseFullInfoById(Integer id) {
        Course course = studyService.getCourseById(id);
        List<CourseModule> modules = courseRepository.getCourseModules(id);
        return new CourseFullInfoResponse(course, modules);
    }

    /**
     * Обновление информации о модулях курса: редактирование и удаление существующих, добавление новых
     *
     */
    @Transactional
    public void updateCourseModules(ModulesUpdateRequest modulesUpdateRequest, Integer courseId) {

        for(CourseModule module : modulesUpdateRequest.getModules()) {

            if(module.getId() == null) {
                Integer moduleId = courseRepository.createCourseModule(module, courseId);
                module.setId(moduleId);
            } else {
                courseRepository.updateCourseModule(module);
            }

            //ресурсы
            List<ModuleResource> newModuleResources = module.getResources()
                    .stream().filter(r -> r.getId() == null).collect(Collectors.toList());
            courseRepository.createModuleResources(newModuleResources, module.getId());

            //задачи
            updateOrCreateTasks(module.getModuleTasks(), module.getId());

        }

        //удаление указанных ресурсов и задач
        courseRepository.deleteResources(modulesUpdateRequest.getDeletedResources());
        deleteTasks(modulesUpdateRequest.getDeletedTasks());

        //удаление модулей с его доченими сущностями
        modulesUpdateRequest.getDeletedModulesId().forEach(id -> {
            courseRepository.deleteModuleResources(id);
            courseRepository.deleteModuleTasks(id);
        });
        courseRepository.deleteModules(modulesUpdateRequest.getDeletedModulesId());
    }

    private void updateOrCreateTasks(List<Task> tasks, Integer moduleId) {
        tasks.forEach( task -> {
            if(task.getId() == null) {
                Integer taskId = courseRepository.createTask(task, moduleId);
                task.setId(taskId);
            } else {
                courseRepository.updateTask(task);
            }

            //ресурсы
            List<ModuleResource> newTaskResources = task.getResources()
                    .stream().filter(r -> r.getId() == null).collect(Collectors.toList());
            courseRepository.createTaskResources(newTaskResources, task.getId());
        });
    }

    private void deleteTasks(Set<Integer> tasksToDeleteId) {
        //удалить ресурсы
        courseRepository.deleteTaskResources(tasksToDeleteId);
        //удалить сами задачи
        courseRepository.deleteTasks(tasksToDeleteId);
    }

    private void deleteFiles(List<String> fileNames) {
        //todo
        //удалим чтобы не копились, но это не критично пока
    }

}
