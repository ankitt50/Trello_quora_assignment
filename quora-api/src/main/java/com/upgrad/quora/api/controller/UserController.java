package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(name = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signUp(SignupUserRequest request) throws SignUpRestrictedException {
        UserEntity alreadyExistingUser = userService.getUserByUsername(request.getUserName());
        if (alreadyExistingUser == null) {
            throw new SignUpRestrictedException("SGR-001","Try any other Username, " +
                    "this Username has already been taken");
        }
        else {
            UserEntity userWithSameEmail = userService.getUserByEmail(request.getEmailAddress());
            if (userWithSameEmail == null) {
                throw new SignUpRestrictedException("SGR-002","This user has" +
                        " already been registered, try with any other emailId");
            }
            else {
                UserEntity newUser = new UserEntity();
                newUser.setFirstName(request.getFirstName());
                newUser.setLastName(request.getLastName());
                newUser.setUserName(request.getUserName());
                newUser.setEmail(request.getEmailAddress());
                newUser.setCountry(request.getCountry());
                newUser.setAboutMe(request.getAboutMe());
                newUser.setDob(request.getDob());
                newUser.setRole("nonadmin");
                newUser.setContactNumber(request.getContactNumber());
                newUser.setPassword(request.getPassword());

                UserEntity registeredUser = userService.signupUser(newUser);

                SignupUserResponse response = new SignupUserResponse().
                        id(registeredUser.getUUID()).
                        status("USER SUCCESSFULLY REGISTERED");

                return new ResponseEntity<SignupUserResponse>(response, HttpStatus.CREATED);

            }
        }

    }
}
