package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;

    public UserEntity signupUser(UserEntity userEntity) {
            entityManager.persist(userEntity);
            return userEntity;
    }

    public UserEntity getUserByUsername(String username) {
        try {
            return entityManager.createNamedQuery("UserName", UserEntity.class).
                    setParameter("username", username).getSingleResult();
        }
        catch (NoResultException exception) {
            return null;
        }

    }

    public UserEntity getUserByEmail(String email) {
        try {
            return entityManager.createNamedQuery("UserEmail", UserEntity.class).
                    setParameter("email", email).getSingleResult();
        }
        catch (NoResultException exception) {
            return null;
        }
    }

    public UserAuthEntity saveLoginInfo(UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public UserEntity getUserByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("UserId", UserEntity.class)
                .setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

}
