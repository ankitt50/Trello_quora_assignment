package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBuisnessService;
import com.upgrad.quora.service.business.AuthTokenService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    AnswerBuisnessService service;

    @Autowired
    QuestionService questionBusinessService;

    @Autowired
    UserService userService;

    @Autowired
    AuthTokenService authTokenService;

    @PostMapping(path = "question/{questionId}/answer/create",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader(name = "authorization") final String authToken ,
        @PathVariable("questionId") final String questionUuid,
        AnswerRequest request) throws AuthorizationFailedException, InvalidQuestionException {

        String token = getToken(authToken);

        UserEntity userEntity = authTokenService.checkAuthentication(token, "createAnswer");

//        UserEntity userEntity = null;
//        try {
//            String[] authTokenArray = authToken.split("Bearer ");
//            userEntity = questionBusinessService.checkAuthToken(authTokenArray[1],
//                "User is signed out.Sign in first to post an answer");
//        }
//        catch (ArrayIndexOutOfBoundsException exc) {
//            userEntity = questionBusinessService.checkAuthToken(authToken,
//                "User is signed out.Sign in first to post an answer");
//        }

        QuestionEntity questionEntity = questionBusinessService.getQuestionByUuid(questionUuid, "The question entered is invalid");

        AnswerEntity answer = new AnswerEntity();
        answer.setUuid(UUID.randomUUID().toString());
        answer.setAns(request.getAnswer());
        answer.setDate(LocalDateTime.now());
        answer.setUser(userEntity);
        answer.setQuestion(questionEntity);

        service.createAnswer(answer);

        AnswerResponse response = new AnswerResponse().id(answer.getUuid()).status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(response,HttpStatus.CREATED);
    }


    @PutMapping(path = "answer/edit/{answerId}",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@RequestHeader(name = "authorization") final String authToken ,
        @PathVariable("answerId") final String answerUuid,
        AnswerEditRequest request) throws AuthorizationFailedException, AnswerNotFoundException {

        String token = getToken(authToken);

        UserEntity loggedInUser = authTokenService.checkAuthentication(token, "editAnswerContent");

//        UserEntity loggedInUser = null;
//        try {
//            String[] authTokenArray = authToken.split("Bearer ");
//            loggedInUser = questionBusinessService.checkAuthToken(authTokenArray[1],
//                "User is signed out.Sign in first to edit an answer");
//        }
//        catch (ArrayIndexOutOfBoundsException exc) {
//            loggedInUser = questionBusinessService.checkAuthToken(authToken,
//                "User is signed out.Sign in first to edit an answer");
//        }

        AnswerEntity answerEntity = service.answerById(answerUuid);

        if (answerEntity.getUser().getUUID().compareTo(loggedInUser.getUUID()) != 0) {
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }

        answerEntity.setAns(request.getContent());
        service.editAnswerContent(answerEntity);

        AnswerEditResponse response = new AnswerEditResponse().id(answerUuid).status("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(response,HttpStatus.OK);
    }


    @DeleteMapping(path = "answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader(name = "authorization") final String authToken ,
        @PathVariable("answerId") final String answerUuid) throws AuthorizationFailedException, AnswerNotFoundException {

        String token = getToken(authToken);

        UserEntity loggedInUser = authTokenService.checkAuthentication(token, "deleteAnswer");

//        UserEntity loggedInUser = null;
//        try {
//            String[] authTokenArray = authToken.split("Bearer ");
//            loggedInUser = questionBusinessService.checkAuthToken(authTokenArray[1],
//                "User is signed out.Sign in first to delete an answer");
//        }
//        catch (ArrayIndexOutOfBoundsException exc) {
//            loggedInUser = questionBusinessService.checkAuthToken(authToken,
//                "User is signed out.Sign in first to delete an answer");
//        }


        AnswerEntity answerEntity = service.answerById(answerUuid);

        if (loggedInUser.getRole().compareTo("admin") != 0) {
            if (answerEntity.getUser().getUUID().compareTo(loggedInUser.getUUID()) != 0) {
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
            }
        }

        service.deleteAnswer(answerEntity);

        return new ResponseEntity<AnswerDeleteResponse>(new AnswerDeleteResponse().
            status("ANSWER DELETED").id(answerEntity.getUuid()),
            HttpStatus.OK);
    }

    @GetMapping(path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@RequestHeader("authorization") final String authToken,
        @PathVariable("questionId") final String questionUuid) throws AuthorizationFailedException, InvalidQuestionException {

        String token = getToken(authToken);

        UserEntity loggedInUser = authTokenService.checkAuthentication(token, "getAllAnswersToQuestion");

//        UserEntity loggedInUser = null;
//        try {
//            String[] authTokenArray = authToken.split("Bearer ");
//            loggedInUser = questionBusinessService.checkAuthToken(authTokenArray[1],
//                "User is signed out.Sign in first to get the answers");
//        }
//        catch (ArrayIndexOutOfBoundsException exc) {
//            loggedInUser = questionBusinessService.checkAuthToken(authToken,
//                "User is signed out.Sign in first to get the answers");
//        }

        List<AnswerEntity> answers = service.getAllAnswersToQuestion(questionUuid);
        List<AnswerDetailsResponse> responseList = new ArrayList<AnswerDetailsResponse>();
        for (AnswerEntity answer:
            answers) {
            AnswerDetailsResponse response = new AnswerDetailsResponse();
            response.id(answer.getUuid()).
                answerContent(answer.getAns()).
                questionContent(answer.getQuestion().getContent());
            responseList.add(response);
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(responseList,HttpStatus.OK);

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