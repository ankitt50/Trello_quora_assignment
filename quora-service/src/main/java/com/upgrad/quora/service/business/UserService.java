package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    public UserEntity getUserByUsername(String username) {
        UserEntity userEntity =  userDao.getUserByUsername(username) ;
        return userEntity;
    }

    public UserEntity getUserByEmail(String email) {
        UserEntity userEntity = userDao.getUserByEmail(email);
        return userEntity;
    }
    public UserEntity signupUser(UserEntity user) {
        String[] array = passwordCryptographyProvider.encrypt(user.getPassword());
        user.setPassword(array[1]);
        user.setSalt(array[0]);
        userDao.signupUser(user);
        return user;
    }
}
