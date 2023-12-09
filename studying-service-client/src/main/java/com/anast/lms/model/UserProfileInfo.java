package com.anast.lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfileInfo {

    @JsonProperty("login")
    private String login;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("mail")
    private String mail;

    @JsonProperty("student_info")
    private StudentProfileInfo studentInfo;

    @JsonProperty("teacher_info")
    private TeacherProfileInfo teacherInfo;

    public UserProfileInfo(String login, String fullName, String mail, StudentProfileInfo studentInfo, TeacherProfileInfo teacherInfo) {
        this.login = login;
        this.fullName = fullName;
        this.mail = mail;
        this.studentInfo = studentInfo;
        this.teacherInfo = teacherInfo;
    }

    public UserProfileInfo() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
