package com.upgrad.quora.service.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_auth")
public class UserAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "uuid")
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "expires_at")
    private LocalDateTime expiryTime;

    @Column(name = "login_at")
    private LocalDateTime loginTime;

    @Column(name = "logout_at")
    private LocalDateTime LogoutTime;

    public UserAuthEntity() {

    }

    public UserAuthEntity(String uuid, int userId, String accessToken, LocalDateTime expiryTime, LocalDateTime loginTime, LocalDateTime logoutTime) {
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.expiryTime = expiryTime;
        this.loginTime = loginTime;
        LogoutTime = logoutTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return LogoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        LogoutTime = logoutTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserAuthEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiryTime=" + expiryTime +
                ", loginTime=" + loginTime +
                ", LogoutTime=" + LogoutTime +
                '}';
    }
}
