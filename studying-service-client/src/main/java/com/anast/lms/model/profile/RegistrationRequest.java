package com.anast.lms.model.profile;

import com.anast.lms.model.RequestState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class RegistrationRequest {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("login")
    private String userLogin;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("state")
    private Short state;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("change_date")
    private LocalDateTime changeDate;

    @JsonProperty("user_profile")
    private UserProfile userProfile;

    public RegistrationRequest(Integer id, String userLogin, String displayName, Short state, LocalDateTime createDate,
                               LocalDateTime changeDate, UserProfile userProfile) {
        this.id = id;
        this.userLogin = userLogin;
        this.displayName = displayName;
        this.state = state;
        this.createDate = createDate;
        this.changeDate = changeDate;
        this.userProfile = userProfile;
    }

    public RegistrationRequest(Integer id, String userLogin, String displayName, Short state, LocalDateTime createDate,
                               LocalDateTime changeDate) {
        this.id = id;
        this.userLogin = userLogin;
        this.displayName = displayName;
        this.state = state;
        this.createDate = createDate;
        this.changeDate = changeDate;
    }

    @JsonIgnore
    public RequestState getRequestStateEnum() {
        //todo
        return RequestState.getEnum((short) 0);
    }

    public RegistrationRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }
}
