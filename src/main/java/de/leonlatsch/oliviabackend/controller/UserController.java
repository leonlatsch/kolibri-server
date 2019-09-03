package de.leonlatsch.oliviabackend.controller;

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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<UserDTO> getAllUsers() {
        Collection<UserDTO> users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getByUid/{uid}")
    public UserDTO getUserByUid(@PathVariable("uid") int uid) {
        UserDTO userDTO =  userService.getUserByUid(uid);
        if (userDTO == null) {
            throw new NoContentException();
        } else {
            return userDTO;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getByEmail/{email}")
    public UserDTO getUserByEmail(@PathVariable("email" ) String email) {
        UserDTO userDTO =  userService.getUserByEmail(email);
        if (userDTO == null) {
            throw new NoContentException();
        } else {
            return userDTO;
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{uid}")
    public String deleteUser(@PathVariable("uid") int uid) {
        return createJsonMessage(userService.deleteUser(uid));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkUsername/{username}")
    public String checkUsername(@PathVariable("username") String username) {
        return createJsonMessage(userService.isUsernameFree(username));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkEmail/{email}")
    public String checkEmail(@PathVariable("email") String email) {
        return createJsonMessage(userService.isEmailFree(email));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update", produces = "application/json")
    public String updateUser(@RequestBody UserDTO user) {
        return createJsonMessage(userService.updateUser(user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/{username}")
    public Collection<UserDTO> searchUsersByUsername(@PathVariable("username") String username) {
        Collection<UserDTO> users = userService.getUserByUsername(username);
        if (users == null | users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/top100/{username}")
    public Collection<UserDTO> searchUsersTop100ByUsername(@PathVariable("username") String username) {
        Collection<UserDTO> users = userService.getUserTop100(username);
        if (users == null | users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getProfilePic/{uid}")
    public ProfilePicDTO loadProfilePic(@PathVariable int uid) {
        return userService.loadProfilePic(uid);
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
