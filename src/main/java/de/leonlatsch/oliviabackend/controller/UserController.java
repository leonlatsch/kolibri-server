package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.constants.Headers;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.service.UserService;
import de.leonlatsch.oliviabackend.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static de.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "get/all")
    public ResponseEntity<Response> getAllUsers(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.getAllUsers(accessToken));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public ResponseEntity<Response> get(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.get(accessToken));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/publicKey/get/{uid}")
    public ResponseEntity<Response> getPublicKey(@PathVariable("uid") int uid) {
        return createResponseEntity(userService.getPublicKey(uid));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/publicKey/update")
    public ResponseEntity<Response> updatePublicKey(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @RequestHeader(Headers.PUBLIC_KEY) String publicKey) {
        return createResponseEntity(userService.updatePublicKey(accessToken, publicKey));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity<Response> delete(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.deleteUser(accessToken));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/username/{username}")
    public ResponseEntity<Response> checkUsername(@PathVariable("username") String username) {
        return createResponseEntity(userService.isUsernameFree(username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/email/{email}")
    public ResponseEntity<Response> checkEmail(@PathVariable("email") String email) {
        return createResponseEntity(userService.isEmailFree(email));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update", produces = "application/json")
    public ResponseEntity<Response> update(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody UserDTO user) {
        return createResponseEntity(userService.updateUser(accessToken, user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/{username}")
    public ResponseEntity<Response> searchUsersByUsername(@PathVariable("username") String username) {
        return createResponseEntity(userService.search(username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/top100/{username}")
    public ResponseEntity<Response> searchUsersTop100ByUsername(@PathVariable("username") String username) {
        return createResponseEntity(userService.searchTop100(username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/profilePic")
    public ResponseEntity<Response> loadProfilePic(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.loadProfilePic(accessToken));
    }

    @ExceptionHandler
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
