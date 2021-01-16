package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AdminDao {

    @PersistenceContext
    EntityManager entityManager;

    public UserEntity deleteUser(String uuid) {
        try {
            UserEntity userEntity = entityManager.createNamedQuery("UserId", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
            entityManager.remove(userEntity);
            return userEntity;
        }
        catch (NoResultException exc) {
            return null;
        }
    }
}
