package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.constants.Headers;
import dev.leonlatsch.oliviabackend.dto.Response;
import dev.leonlatsch.oliviabackend.dto.UserDTO;
import dev.leonlatsch.oliviabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

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

    @RequestMapping(method = RequestMethod.GET, value = "get/{uid}")
    public ResponseEntity<Response> get(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @PathVariable("uid") String uid) {
        return createResponseEntity(userService.get(accessToken, uid));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public ResponseEntity<Response> get(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.get(accessToken));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/public-key/get/{uid}")
    public ResponseEntity<Response> getPublicKey(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("uid") String uid) {
        return createResponseEntity(userService.getPublicKey(accessToken, uid));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/public-key/update")
    public ResponseEntity<Response> updatePublicKey(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @RequestHeader(Headers.PUBLIC_KEY) String publicKey) {
        return createResponseEntity(userService.updatePublicKey(accessToken, publicKey));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity<Response> delete(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.deleteUser(accessToken));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/username/{username}")
    public ResponseEntity<Response> checkUsername(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("username") String username) {
        return createResponseEntity(userService.isUsernameFree(accessToken, username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/email/{email}")
    public ResponseEntity<Response> checkEmail(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("email") String email) {
        return createResponseEntity(userService.isEmailFree(accessToken, email));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update", produces = "application/json")
    public ResponseEntity<Response> update(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody UserDTO user) {
        return createResponseEntity(userService.updateUser(accessToken, user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/{username}")
    public ResponseEntity<Response> searchUsersByUsername(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("username") String username) {
        return createResponseEntity(userService.search(accessToken, username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/top100/{username}")
    public ResponseEntity<Response> searchUsersTop100ByUsername(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("username") String username) {
        return createResponseEntity(userService.search(accessToken, username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/profile-pic/{uid}")
    public ResponseEntity<Response> loadProfilePic(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("uid") String uid) {
        return createResponseEntity(userService.loadProfilePic(accessToken, uid));
    }

    @ExceptionHandler
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
