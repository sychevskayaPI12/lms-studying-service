package com.anast.lms.client;

import com.anast.lms.model.RequestState;
import com.anast.lms.model.profile.RegistrationRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface ModerationRestService {

    @GetMapping("/moderation/registration")
    List<RegistrationRequest> getRegistrationRequests(@RequestParam(value = "state") Short state);

    @GetMapping("/moderation/registration/{request_id}/accept")
    void acceptRegistrationRequest(@PathVariable("request_id") Integer requestId);

    @GetMapping("/moderation/registration/{request_id}/decline")
    void declineRegistrationRequest(@PathVariable("request_id") Integer requestId);

    @GetMapping("/moderation/registration/{login}/create")
    void createRegistrationRequest(@PathVariable("login")String login);
}
