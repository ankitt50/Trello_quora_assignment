package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao {

  @PersistenceContext
  EntityManager entityManager;


  public QuestionEntity createQuestion(QuestionEntity questionEntity) {
    entityManager.persist(questionEntity);
    return questionEntity;
  }
}
