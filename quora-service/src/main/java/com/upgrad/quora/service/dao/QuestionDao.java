package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

  public QuestionEntity getQuestionByUuid(String uuid) throws InvalidQuestionException {
    try {
      return entityManager.createNamedQuery("getQuestionByUuid", QuestionEntity.class)
          .setParameter("uuid", uuid).getSingleResult();
    } catch (NoResultException exception) {
      throw new InvalidQuestionException("QUES-001", "'Entered question uuid does not exist");
    }
  }

  public QuestionEntity deleteQuestion(String uuid, UserEntity user)
      throws InvalidQuestionException, AuthorizationFailedException {
    QuestionEntity question = getQuestionByUuid(uuid);
    if (question.getUser().equals(user) || user.getRole().equals("admin")) {
      entityManager.remove(question);
      return question;
    } else {
      throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
    }
  }

  @Transactional
  public QuestionEntity editQuestionContent(String uuid, String updatedContent, UserEntity user)
      throws AuthorizationFailedException, InvalidQuestionException {
    QuestionEntity question = getQuestionByUuid(uuid);

    if (question.getUser() != user && user.getRole() != "admin") {
      // throw error if the user is not an admin or does not own the question
      throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
    }

    question.setContent(updatedContent);
    entityManager.merge(question);
    return question;
  }

  public List<QuestionEntity> getAllQuestionsByUser(UserEntity user) {
    return entityManager.createNamedQuery("getAllQuestionsByUser", QuestionEntity.class).setParameter("user", user).getResultList();
  }
}
