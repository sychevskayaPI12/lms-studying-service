package com.anast.lms.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

    @JsonProperty("user_info")
    private UserProfileInfo userProfileInfo;

    @JsonProperty("student_info")
    private StudentProfileInfo studentInfo;

    @JsonProperty("teacher_info")
    private TeacherProfileInfo teacherInfo;

    public UserProfile(UserProfileInfo userProfileInfo, StudentProfileInfo studentInfo, TeacherProfileInfo teacherInfo) {
        this.userProfileInfo = userProfileInfo;
        this.studentInfo = studentInfo;
        this.teacherInfo = teacherInfo;
    }

    public UserProfile() {
    }

    public UserProfileInfo getUserProfileInfo() {
        return userProfileInfo;
    }

    public void setUserProfileInfo(UserProfileInfo userProfileInfo) {
        this.userProfileInfo = userProfileInfo;
    }

    public StudentProfileInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentProfileInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public TeacherProfileInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherProfileInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }
}
