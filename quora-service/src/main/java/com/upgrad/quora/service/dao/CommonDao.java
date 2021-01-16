package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CommonDao {

    @PersistenceContext
    EntityManager entityManager;

    public UserEntity getUserDetails(String uuid) {
        try {
            return entityManager.createNamedQuery("UserId", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch (NoResultException exc) {
            return null;
        }
    }
}
