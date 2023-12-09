package com.anast.lms.client;

import com.anast.lms.model.UserProfileInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ProfileRestService {

    @GetMapping("/study/profile/{login}")
    UserProfileInfo getUserProfileInfo(@PathVariable("login") String login);
}
