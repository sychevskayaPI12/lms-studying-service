package com.anast.lms.client;

import com.anast.lms.model.UserProfileInfo;
import org.springframework.web.bind.annotation.*;

@RestController
public interface ProfileRestService {

    @GetMapping("/study/profile/{login}")
    UserProfileInfo getUserProfileInfo(@PathVariable("login") String login);

    @PostMapping("/study/profile/")
    void saveProfileInfo(@RequestBody UserProfileInfo profileInfo);
}
