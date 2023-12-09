package com.anast.lms.client;

import org.springframework.web.bind.annotation.*;


@RestController
public interface StudyRestService {

    @GetMapping("/study/{group_code}/courses")
    void getStudentCourser(@PathVariable("group_code") String groupCode,
                           @RequestParam(value = "is_active", required = false) Boolean isActive);
}
