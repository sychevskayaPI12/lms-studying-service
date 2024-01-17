package com.anast.lms.controller;

import com.anast.lms.client.ModerationRestService;
import com.anast.lms.model.RequestState;
import com.anast.lms.model.profile.RegistrationRequest;
import com.anast.lms.service.ModerationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ModerationController implements ModerationRestService {

    private final ModerationService moderationService;

    public ModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @Override
    public List<RegistrationRequest> getRegistrationRequests(Short state) {
        return moderationService.getRegistrationRequests(state);
    }

    @Override
    public void acceptRegistrationRequest(Integer requestId) {
        moderationService.acceptRegistrationRequest(requestId);
    }

    @Override
    public void declineRegistrationRequest(Integer requestId) {
        moderationService.declineRegistrationRequest(requestId);
    }

    @Override
    public void createRegistrationRequest(String login) {
        moderationService.createRegistrationRequest(login);
    }
}
