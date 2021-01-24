package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AuthTokenDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthTokenService {
    @Autowired
    AuthTokenDao authTokenDao;

    private Map<String, String> error;

    @PostConstruct
    private void errorMessageSetup() {
        error = new HashMap<>();
        error.put("createQuestion.ATHR-001", "User has not signed in");
        error.put("createQuestion.ATHR-002", "User is signed out.Sign in first to post a question");
        error.put("getAllQuestions.ATHR-001", "User has not signed in");
        error.put("getAllQuestions.ATHR-002", "User is signed out.Sign in first to get all questions");
        error.put("editQuestionContent.ATHR-001", "User has not signed in");
        error.put("editQuestionContent.ATHR-002", "User is signed out.Sign in first to edit the question");
        error.put("deleteQuestion.ATHR-001", "User has not signed in");
        error.put("deleteQuestion.ATHR-002", "User is signed out.Sign in first to delete a question");
        error.put("getAllQuestionsByUser.ATHR-001", "User has not signed in");
        error.put("getAllQuestionsByUser.ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");

    }

    @Transactional
    public UserEntity checkAuthToken(String authToken) throws SignOutRestrictedException {



        UserAuthEntity userAuthEntity = authTokenDao.checkAuthToken(authToken);
        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        else {
            return authTokenDao.signOutUser(userAuthEntity);
        }
    }

    public UserEntity checkAuthentication(String authToken, String methodName) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = authTokenDao.checkAuthToken(authToken);
        LocalDateTime now = LocalDateTime.now();

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", error.get(methodName + ".ATHR-001"));
        } else if (userAuthEntity.getLogoutTime() != null) {
            throw new AuthorizationFailedException("ATHR-002", error.get(methodName + ".ATHR-002"));
        } else {
            return userAuthEntity.getUser();
        }
    }

}
