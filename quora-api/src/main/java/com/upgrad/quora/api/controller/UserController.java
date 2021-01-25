package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthTokenService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthTokenService authTokenService;

    // register a new user
    @PostMapping(path = "user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signUp(SignupUserRequest request)
            throws SignUpRestrictedException {
        UserEntity alreadyExistingUser = userService.getUserByUsername(request.getUserName());
        if (alreadyExistingUser != null) { // throw an error if username already exists in DB
            throw new SignUpRestrictedException("SGR-001","Try any other Username, " +
                    "this Username has already been taken");
        }
        else {
            UserEntity userWithSameEmail = userService.getUserByEmail(request.getEmailAddress());
            if (userWithSameEmail != null) { // throw error if email ID has already been used
                throw new SignUpRestrictedException("SGR-002","This user has" +
                        " already been registered, try with any other emailId");
            }
            else {
                // create and populate new user entity:
                UserEntity newUser = new UserEntity();
                newUser.setUUID(UUID.randomUUID().toString());
                newUser.setFirstName(request.getFirstName());
                newUser.setLastName(request.getLastName());
                newUser.setUserName(request.getUserName());
                newUser.setEmail(request.getEmailAddress());
                newUser.setCountry(request.getCountry());
                newUser.setAboutMe(request.getAboutMe());
                newUser.setDob(request.getDob());
                newUser.setRole("nonadmin"); // default user role
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

    // sign in user and return JWT token
    @PostMapping(path = "user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signIn(@RequestHeader(name = "authorization") final String authorization)
            throws AuthenticationFailedException {

        String encodedString = getSignInToken(authorization); // extract base64 encoded username and password

        byte[] array = Base64.getDecoder().decode(encodedString); // decode username and password
        String usrPsw = new String(array);
        String username = "", password = "";
        try {
            String[] usrPswArray = usrPsw.split(":"); // split username and password
            username = usrPswArray[0];
            password = usrPswArray[1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new AuthenticationFailedException("ATH-002", "Password failed"); // throw error if authentication String is incorrect
        }

        UserEntity userWithUsername = userService.getUserByUsername(username);
        if (userWithUsername == null) {
            throw new AuthenticationFailedException("ATH-001","This username does not exist"); // throw error if username does not exist
        }
        else {
            if (userService.isPasswordCorrect(password, userWithUsername)) {
                UserAuthEntity authEntity = new UserAuthEntity();
                authEntity.setUuid(userWithUsername.getUUID());
                authEntity.setUser(userWithUsername);
                UserAuthEntity userAuthEntity = userService.saveLoginInfo(authEntity, password);
                UserEntity userEntity = userAuthEntity.getUser();


                HttpHeaders headers = new HttpHeaders();
                headers.add("access-token","Bearer "+userAuthEntity.getAccessToken()); // add JWT token in header and return

                return new ResponseEntity<SigninResponse>(new SigninResponse().id(userEntity.getUUID()).message("SIGNED IN SUCCESSFULLY"),headers, HttpStatus.OK);
            }
            else {
                throw new AuthenticationFailedException("ATH-002","Password failed"); // throw error if password is wrong
            }
        }
    }

    // signout user and update log out time in DB
    @PostMapping(path = "user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signOut(@RequestHeader(name = "authorization") final String authToken)
        throws SignOutRestrictedException, AuthorizationFailedException {

        String token = getToken(authToken); 
        UserEntity userEntity =  authTokenService.checkAuthToken(token);
        return new ResponseEntity<SignoutResponse>(new SignoutResponse().id(userEntity.getUUID()).message("SIGNED OUT SUCCESSFULLY"), HttpStatus.OK);
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

    // this method extracts the token from the base64 encoded authentication String
    private String getSignInToken(String authToken) {
        String token;
        if (authToken.startsWith("Basic ")) {
            String [] basicToken = authToken.split("Basic ");
            token = basicToken[1];
        } else {
            token = authToken;
        }
        return token;
    }
}
