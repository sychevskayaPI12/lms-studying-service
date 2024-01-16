package com.anast.lms.client;

import com.anast.lms.model.course.CourseFullInfoResponse;
import com.anast.lms.model.course.ModuleResource;
import com.anast.lms.model.course.ModulesUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public interface CourseRestService {

    @PostMapping("/course/{id}/modules")
    void updateCourseModules(@PathVariable("id") Integer courseId,
                             @RequestBody ModulesUpdateRequest modulesUpdateRequest);

    @PostMapping(value = "/course/file")
    ResponseEntity<byte[]> getFileData(@RequestBody ModuleResource resource);

    @PostMapping("/course/file/upload")
    void uploadFileData(@RequestBody byte[] fileData, @RequestParam("file_name") String fileName);

    @GetMapping("/course/{id}/full")
    CourseFullInfoResponse getCourseFullInfo(@PathVariable("id") Integer id);
}
