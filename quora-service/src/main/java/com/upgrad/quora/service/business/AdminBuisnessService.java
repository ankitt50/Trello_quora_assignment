package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AdminDao;
import com.upgrad.quora.service.dao.AuthTokenDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBuisnessService {

    @Autowired
    AuthTokenDao authTokenDao;

    @Autowired
    AdminDao adminDao;

    @Transactional
    public UserEntity checkAuthToken(String authToken) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = authTokenDao.checkAuthToken(authToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else {
            if (userAuthEntity.getLogoutTime() == null) {
                return userAuthEntity.getUser();
            }
            else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out");
            }
        }
    }

    @Transactional
    public UserEntity deleteUser(String uuid) throws UserNotFoundException {
        UserEntity userEntity = adminDao.deleteUser(uuid);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }
        else {
            return userEntity;
        }
    }

}
