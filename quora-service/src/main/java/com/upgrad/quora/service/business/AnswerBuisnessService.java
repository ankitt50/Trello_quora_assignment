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

// This class contains the methods to implement the business logic for the Answers in the database.
@Service
public class AnswerBuisnessService {

    @Autowired
    QuestionService questionBusinessService;

    @Autowired
    AnswerDao answerDao;

    // This method creates an answer based on the Answer Entity details received.
    @Transactional
    public AnswerEntity createAnswer(AnswerEntity answer) {
        return answerDao.createAnswer(answer);
    }

    // This method lokos for the answer using the given UUID and throws an exception if answer is not found.
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

    // This method edits the answer for the answer etnity received.
    @Transactional
    public AnswerEntity editAnswerContent(AnswerEntity answerEntity) {
        return answerDao.editAnswerContent(answerEntity);
    }

    // This method deletes the answer for the given answer etnity received.
    @Transactional
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        return answerDao.deleteAnswer(answerEntity);
    }

    // This method gets all the answers for the given UUID of the question and throws an exception if the question is not found.
    @Transactional
    public List<AnswerEntity> getAllAnswersToQuestion(String questionUuid) throws InvalidQuestionException {
        QuestionEntity question = questionBusinessService.getQuestionByUuid(questionUuid,
            "The question with entered uuid whose details are to be seen does not exist");

        return question.getAnswers();
    }
}