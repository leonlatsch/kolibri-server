package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.constants.Headers;
import de.leonlatsch.oliviabackend.dto.PublicUserDTO;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.dto.ProfilePicDTO;
import de.leonlatsch.oliviabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Collection<UserDTO> getAllUsers(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        Collection<UserDTO> users = userService.getAllUsers(accessToken);
        if (users == null || users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public UserDTO get(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        UserDTO userDTO =  userService.get(accessToken);
        if (userDTO == null) {
            throw new NoContentException();
        } else {
            return userDTO;
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public String delete(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createJsonMessage(userService.deleteUser(accessToken));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/username/{username}")
    public String checkUsername(@PathVariable("username") String username) {
        return createJsonMessage(userService.isUsernameFree(username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/check/email/{email}")
    public String checkEmail(@PathVariable("email") String email) {
        return createJsonMessage(userService.isEmailFree(email));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update", produces = "application/json")
    public String update(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody UserDTO user) {
        return createJsonMessage(userService.updateUser(accessToken, user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/{username}")
    public Collection<PublicUserDTO> searchUsersByUsername(@PathVariable("username") String username) {
        Collection<PublicUserDTO> users = userService.search(username);
        if (users == null | users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/top100/{username}")
    public Collection<PublicUserDTO> searchUsersTop100ByUsername(@PathVariable("username") String username) {
        Collection<PublicUserDTO> users = userService.searchTop100(username);
        if (users == null | users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/profilePic")
    public ProfilePicDTO loadProfilePic(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken) {
        return userService.loadProfilePic(accessToken);
    }

    @ExceptionHandler
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    private String createJsonMessage(String message, String key) {
        String jsonMessage = "{\"${key}\": \"${message}\"}";
        jsonMessage = jsonMessage.replace("${message}", message);
        jsonMessage = jsonMessage.replace("${key}", key);
        return jsonMessage;
    }

    private String createJsonMessage(String message) {
        String defaultKey = "message";
        return createJsonMessage(message, defaultKey);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "No content to return")
    private class NoContentException extends RuntimeException {}
}
