package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.AuthTokenService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class QuestionController {

  @Autowired
  AuthTokenService authTokenService;

  @Autowired
  QuestionService questionService;

  @Autowired
  UserService userService;

  // This method adds functionality to post a new question on the quora app
  @PostMapping(path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader(name = "authorization") final String authToken, final QuestionRequest questionRequest)
      throws AuthorizationFailedException {

    String token = getToken(authToken);

    UserEntity userEntity = authTokenService.checkAuthentication(token, "createQuestion");

    QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setUUID(UUID.randomUUID().toString());
    questionEntity.setContent(questionRequest.getContent());
    questionEntity.setDate(LocalDateTime.now());
    questionEntity.setUser(userEntity);

    QuestionEntity createdQuestion = questionService.createQuestion(questionEntity);

    QuestionResponse questionResponse = new QuestionResponse();
    questionResponse.setId(createdQuestion.getUUID());
    questionResponse.setStatus("QUESTION CREATED");

    return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

  }

  // method to handle requests to get all questions
  @GetMapping(path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(
      @RequestHeader(name = "authorization") final String authToken)
      throws AuthorizationFailedException {

    String token = getToken(authToken);

    authTokenService.checkAuthentication(token, "getAllQuestions");

    List<QuestionEntity> questions = questionService.getAllQuestions();

    List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();

    for (QuestionEntity question : questions) {
      QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
      questionDetailsResponse.setId(question.getUUID());
      questionDetailsResponse.setContent(question.getContent());
      questionDetailsResponseList.add(questionDetailsResponse);
    }

    return new ResponseEntity<>(questionDetailsResponseList, HttpStatus.OK);
  }

  // method to handle edit question requests
  @PutMapping(path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionEditResponse> editQuestionContent(@PathVariable String questionId, @RequestHeader(name = "authorization") final String authToken,
      final QuestionEditRequest questionEditRequest)
      throws AuthorizationFailedException, InvalidQuestionException {

    String token = getToken(authToken);

    UserEntity userEntity = authTokenService.checkAuthentication(token, "editQuestionContent");

    QuestionEntity question = questionService.editQuestionContent(questionId, questionEditRequest.getContent(), userEntity);

    QuestionEditResponse questionEditResponse = new QuestionEditResponse();
    questionEditResponse.setId(question.getUUID());
    questionEditRequest.setContent(question.getContent());

    return new ResponseEntity<>(questionEditResponse, HttpStatus.OK);
  }

  // method to handle question delete requests
  @DeleteMapping(path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable String questionId, @RequestHeader(name = "authorization") final String authToken)
      throws AuthorizationFailedException, InvalidQuestionException {

    String token = getToken(authToken);

    UserEntity userEntity = authTokenService.checkAuthentication(token, "deleteQuestion");
    QuestionEntity question = questionService.deleteQuestion(questionId, userEntity);

    QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
    questionDeleteResponse.setId(question.getUUID());
    questionDeleteResponse.setStatus("QUESTION DELETED");

    return new ResponseEntity<>(questionDeleteResponse, HttpStatus.OK);
  }

  // method to get all the questions posted by a particular user
  @GetMapping(path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(
      @PathVariable String userId,
      @RequestHeader(name = "authorization") final String authToken)
      throws AuthorizationFailedException, UserNotFoundException {

    String token = getToken(authToken);

    authTokenService.checkAuthentication(token, "getAllQuestionsByUser");

    UserEntity user = userService.getUserByUuid(userId);

    List<QuestionEntity> questions = questionService.getAllQuestionsByUser(user);

    List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();

    for (QuestionEntity question : questions) { // converting list of QuestionEntity to list of QuestionDetailsResponse
      QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
      questionDetailsResponse.setId(question.getUUID());
      questionDetailsResponse.setContent(question.getContent());
      questionDetailsResponseList.add(questionDetailsResponse);
    }

    return new ResponseEntity<>(questionDetailsResponseList, HttpStatus.OK);
  }

  // this method extracts the token from the JWT token string sent in the Request Header
  private String getToken(String authToken) {
    String token;
    if (authToken.startsWith("Bearer ")) {
      String [] bearerToken = authToken.split("Bearer ");
      token = bearerToken[1];
    } else {
      token = authToken;
    }
    return token;
  }
}
