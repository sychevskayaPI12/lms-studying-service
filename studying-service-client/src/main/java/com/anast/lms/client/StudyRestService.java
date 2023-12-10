package com.anast.lms.client;

import com.anast.lms.model.Course;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public interface StudyRestService {

    @GetMapping("/study/{group_code}/courses")
    List<Course> getStudentCourser(@PathVariable("group_code") String groupCode,
                           @RequestParam(value = "is_active", required = false) Boolean isActive);
}
