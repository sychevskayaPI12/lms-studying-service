package com.anast.lms.service;

import com.anast.lms.model.*;
import com.anast.lms.model.profile.UserProfileInfo;
import com.anast.lms.service.external.user.UserServiceClient;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserServiceClient userServiceClient;

    public ProfileService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    /**
     * Получение основной информации о пользователе
     * @return
     */
    public UserProfileInfo getUserProfileInfo(String login) {
        UserDetail userDetail = userServiceClient.getUserDetail(login);
        return new UserProfileInfo(login, userDetail.getFullName(), userDetail.getMail());
    }
}
