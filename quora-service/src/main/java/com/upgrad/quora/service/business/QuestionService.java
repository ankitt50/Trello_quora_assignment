package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  @Autowired
  private QuestionDao questionDao;


  @Transactional
  public QuestionEntity createQuestion(QuestionEntity questionEntity) {
    return questionDao.createQuestion(questionEntity);
  }

  public List<QuestionEntity> getAllQuestions() {
    return questionDao.getAllQuestions();
  }
}
