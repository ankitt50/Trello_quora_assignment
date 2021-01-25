package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerBuisnessService {

    @Autowired
    QuestionService questionBusinessService;

    @Autowired
    AnswerDao answerDao;


    @Transactional
    public AnswerEntity createAnswer(AnswerEntity answer) {
        return answerDao.createAnswer(answer);
    }

    @Transactional
    public AnswerEntity answerById(String uuid) throws AnswerNotFoundException {
        AnswerEntity answer = answerDao.answerById(uuid);
        if (answer != null) {
            return answer;
        }
        else {
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
    }

    @Transactional
    public AnswerEntity editAnswerContent(AnswerEntity answerEntity) {
        return answerDao.editAnswerContent(answerEntity);
    }

    @Transactional
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        return answerDao.deleteAnswer(answerEntity);
    }

    @Transactional
    public List<AnswerEntity> getAllAnswersToQuestion(String questionUuid) throws InvalidQuestionException {
        QuestionEntity question = questionBusinessService.getQuestionByUuid(questionUuid,
            "The question with entered uuid whose details are to be seen does not exist");

        return question.getAnswers();
    }
}