package com.anast.lms.service;

import com.anast.lms.model.RequestState;
import com.anast.lms.model.UserDetail;
import com.anast.lms.model.profile.RegistrationRequest;
import com.anast.lms.repository.ModeratorRepository;
import com.anast.lms.service.external.user.UserServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModerationService {

    private final UserServiceClient userServiceClient;
    private final ModeratorRepository moderatorRepository;
    private final StudyService studyService;

    public ModerationService(UserServiceClient userServiceClient, ModeratorRepository moderatorRepository, StudyService studyService) {
        this.userServiceClient = userServiceClient;
        this.moderatorRepository = moderatorRepository;
        this.studyService = studyService;
    }

    public List<RegistrationRequest> getRegistrationRequests(Short state) {
        List<RegistrationRequest> requests = moderatorRepository.getRegistrationRequests(state);
        requests.forEach(r -> {
            r.setUserProfile(studyService.getUserProfile(r.getUserLogin()));
        });

        return requests;
    }

    @Transactional
    public void acceptRegistrationRequest(Integer requestId) {
        RegistrationRequest request = moderatorRepository.getRegistrationRequest(requestId);
        userServiceClient.confirmAccount(request.getUserLogin());

        moderatorRepository.updateRegistrationRequest(requestId, RequestState.accepted);
    }

    @Transactional
    public void declineRegistrationRequest(Integer requestId) {
        RegistrationRequest request = moderatorRepository.getRegistrationRequest(requestId);
        studyService.deleteUserProfile(request.getUserLogin());
        userServiceClient.deleteUser(request.getUserLogin());
        moderatorRepository.updateRegistrationRequest(requestId, RequestState.declined);

    }

    public void createRegistrationRequest(String login) {
        UserDetail detail = userServiceClient.getUserDetail(login);
        moderatorRepository.createRegistrationRequest(login, detail.getFullName());
    }


}
