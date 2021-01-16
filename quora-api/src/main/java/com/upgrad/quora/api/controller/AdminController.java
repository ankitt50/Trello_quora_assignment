package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    com.upgrad.quora.service.business.AdminBuisnessService adminBuisnessService;

    @DeleteMapping(path = "admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable(name = "userId") final String uuid,
                                                         @RequestHeader("authorization") final String authToken) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity = adminBuisnessService.checkAuthToken(authToken);

        if (userEntity.getRole().compareTo("admin") == 0) {
            UserEntity deletedUser = adminBuisnessService.deleteUser(uuid);
            return new ResponseEntity<UserDeleteResponse>(new UserDeleteResponse().id(deletedUser.getUUID()).status("USER SUCCESSFULLY DELETED"), HttpStatus.OK);

        }
        else {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }

    }
}
