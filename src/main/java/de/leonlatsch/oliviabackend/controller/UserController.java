package de.leonlatsch.oliviabackend.controller;

import com.fasterxml.jackson.databind.BeanProperty;
import de.leonlatsch.oliviabackend.constants.Headers;
import de.leonlatsch.oliviabackend.dto.PublicUserDTO;
import de.leonlatsch.oliviabackend.dto.StdResponse;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.dto.ProfilePicDTO;
import de.leonlatsch.oliviabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "get/all")
    public ResponseEntity<Collection<UserDTO>> getAllUsers(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        Collection<UserDTO> users = userService.getAllUsers(accessToken);
        if (users == null || users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public ResponseEntity<UserDTO> get(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        UserDTO userDTO =  userService.get(accessToken);
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity<StdResponse> delete(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createStdResponse(userService.deleteUser(accessToken));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/username/{username}")
    public ResponseEntity<StdResponse> checkUsername(@PathVariable("username") String username) {
        return createStdResponse(userService.isUsernameFree(username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/email/{email}")
    public ResponseEntity<StdResponse> checkEmail(@PathVariable("email") String email) {
        return createStdResponse(userService.isEmailFree(email));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update", produces = "application/json")
    public ResponseEntity<StdResponse> update(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody UserDTO user) {
        return createStdResponse(userService.updateUser(accessToken, user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/{username}")
    public ResponseEntity<Collection<PublicUserDTO>> searchUsersByUsername(@PathVariable("username") String username) {
        Collection<PublicUserDTO> users = userService.search(username);
        if (users == null | users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/top100/{username}")
    public ResponseEntity<Collection<PublicUserDTO>> searchUsersTop100ByUsername(@PathVariable("username") String username) {
        Collection<PublicUserDTO> users = userService.searchTop100(username);
        if (users == null | users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/profilePic")
    public ResponseEntity<ProfilePicDTO> loadProfilePic(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken) {
        return new ResponseEntity<>(userService.loadProfilePic(accessToken), HttpStatus.OK);
    }

    @ExceptionHandler
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    private ResponseEntity<StdResponse> createStdResponse(String message, HttpStatus httpStatus) {
        return new ResponseEntity<StdResponse>(new StdResponse(message), httpStatus);
    }

    private ResponseEntity<StdResponse> createStdResponse(String message) {
        return createStdResponse(message, HttpStatus.OK);
    }
}
