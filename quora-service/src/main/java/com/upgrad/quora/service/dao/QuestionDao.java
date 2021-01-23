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

  public List<QuestionEntity> getAllQuestions() {
    return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
  }

  public QuestionEntity editQuestionContent(String uuid, String updatedContent, UserEntity user)
      throws AuthorizationFailedException {
    QuestionEntity question;
    try {
      question = entityManager.createNamedQuery("getQuestionByUuidAndUserId", QuestionEntity.class)
          .setParameter("uuid", uuid).setParameter("user", user).getSingleResult();
    } catch (NoResultException exception) {
      throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
    }

    question.setContent(updatedContent);

    entityManager.merge(question);

    return question;
  }
}
