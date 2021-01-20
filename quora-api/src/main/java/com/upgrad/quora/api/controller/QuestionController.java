package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.AuthTokenService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

  // This method adds functionality to post a new question on quora app
  @PostMapping(path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader(name = "authorization") final String authToken, final QuestionRequest questionRequest)
      throws AuthorizationFailedException {

    String [] bearerToken = authToken.split("Bearer ");   // Request header is of the form: Bearer <JWT token>

    UserEntity userEntity = authTokenService.checkAuthentication(bearerToken[1], "createQuestion");

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

}
