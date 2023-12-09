package com.anast.lms.service;

import com.anast.lms.model.StudentProfileInfo;
import com.anast.lms.model.TeacherProfileInfo;
import com.anast.lms.model.UserDetail;
import com.anast.lms.model.UserProfileInfo;
import com.anast.lms.repository.ProfileRepository;
import com.anast.lms.service.external.user.UserServiceClient;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserServiceClient userServiceClient;
    private final ProfileRepository profileRepository;

    public ProfileService(UserServiceClient userServiceClient, ProfileRepository profileRepository) {
        this.userServiceClient = userServiceClient;
        this.profileRepository = profileRepository;
    }

    public UserProfileInfo getUserProfileInfo(String login) {

        UserDetail userDetail = userServiceClient.getUserDetail(login);

        UserProfileInfo profileInfo = new UserProfileInfo();
        profileInfo.setLogin(login);
        profileInfo.setFullName(userDetail.getFullName());
        profileInfo.setMail(userDetail.getMail());

        TeacherProfileInfo teacherInfo = profileRepository.getTeacherInfo(login);
        profileInfo.setTeacherInfo(teacherInfo);

        StudentProfileInfo studentInfo = profileRepository.getStudentInfo(login);
        //todo calc course
        profileInfo.setStudentInfo(studentInfo);

        return profileInfo;
    }
}
