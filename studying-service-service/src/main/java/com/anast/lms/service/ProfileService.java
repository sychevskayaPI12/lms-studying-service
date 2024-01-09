package com.anast.lms.service;

import com.anast.lms.model.StudentProfileInfo;
import com.anast.lms.model.TeacherProfileInfo;
import com.anast.lms.model.UserDetail;
import com.anast.lms.model.UserProfileInfo;
import com.anast.lms.repository.ProfileRepository;
import com.anast.lms.service.external.user.UserServiceClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserServiceClient userServiceClient;
    private final ProfileRepository profileRepository;
    private final StudyService studyService;

    public ProfileService(UserServiceClient userServiceClient, ProfileRepository profileRepository, StudyService studyService) {
        this.userServiceClient = userServiceClient;
        this.profileRepository = profileRepository;
        this.studyService = studyService;
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
        if(studentInfo != null) {
            studentInfo.setCourse(studyService.calcStudentCourse(studentInfo.getGroupCode()));
        }
        profileInfo.setStudentInfo(studentInfo);

        return profileInfo;
    }

    @Transactional
    public void saveProfileInfo(UserProfileInfo profileInfo) {
        if(profileInfo.getStudentInfo() != null) {
            profileRepository.saveStudentProfile(profileInfo.getStudentInfo(), profileInfo.getLogin());
        }
        if(profileInfo.getTeacherInfo() != null) {
            profileRepository.saveTeacherInfo(profileInfo.getTeacherInfo(), profileInfo.getLogin());
        }
    }
}
