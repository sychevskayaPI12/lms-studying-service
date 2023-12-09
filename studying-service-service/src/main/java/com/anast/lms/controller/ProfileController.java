package com.anast.lms.controller;

import com.anast.lms.client.ProfileRestService;
import com.anast.lms.model.UserProfileInfo;
import com.anast.lms.service.ProfileService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController implements ProfileRestService {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public UserProfileInfo getUserProfileInfo(String login) {
        return profileService.getUserProfileInfo(login);
    }
}
