package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional
    public UserEntity getUserByUsername(String username) {
        UserEntity userEntity =  userDao.getUserByUsername(username) ;
        return userEntity;
    }

    @Transactional
    public UserEntity getUserByEmail(String email) {
        UserEntity userEntity = userDao.getUserByEmail(email);
        return userEntity;
    }

    @Transactional
    public UserEntity signupUser(UserEntity user) {
        String[] array = passwordCryptographyProvider.encrypt(user.getPassword());
        user.setPassword(array[1]);
        user.setSalt(array[0]);
        userDao.signupUser(user);
        return user;
    }
}
