package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(path = "user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signUp(SignupUserRequest request)
            throws SignUpRestrictedException {
        UserEntity alreadyExistingUser = userService.getUserByUsername(request.getUserName());
        if (alreadyExistingUser != null) {
            throw new SignUpRestrictedException("SGR-001","Try any other Username, " +
                    "this Username has already been taken");
        }
        else {
            UserEntity userWithSameEmail = userService.getUserByEmail(request.getEmailAddress());
            if (userWithSameEmail != null) {
                throw new SignUpRestrictedException("SGR-002","This user has" +
                        " already been registered, try with any other emailId");
            }
            else {
                UserEntity newUser = new UserEntity();
                newUser.setUUID(UUID.randomUUID().toString());
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

    @PostMapping(path = "user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signIn(@RequestHeader(name = "authorization") final String authorization)
            throws AuthenticationFailedException {
        String[] stringArray = authorization.split("Basic ");
        byte[] array = Base64.getDecoder().decode(stringArray[1]);
        String usrPsw = new String(array);
        String[] usrPswArray = usrPsw.split(":");
        String username = usrPswArray[0];
        String password = usrPswArray[1];

        UserEntity userWithUsername = userService.getUserByUsername(username);
        if (userWithUsername == null) {
            throw new AuthenticationFailedException("ATH-001","This username does not exist");
        }
        else {
            if (userService.isPasswordCorrect(password, userWithUsername)) {
                UserAuthEntity authEntity = new UserAuthEntity();
                authEntity.setUuid(userWithUsername.getUUID());
                authEntity.setUser(userWithUsername);
                UserEntity userEntity = userService.saveLoginInfo(authEntity, password);
                return new ResponseEntity<SigninResponse>(new SigninResponse().id(userEntity.getUUID()).message("SIGNED IN SUCCESSFULLY"), HttpStatus.OK);
            }
            else {
                throw new AuthenticationFailedException("ATH-002","Password failed");
            }
        }
    }
}
