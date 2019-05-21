package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.service.UserService;
import de.leonlatsch.oliviabackend.transfer.TransferUser;
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
    public Collection<TransferUser> getAllUsers() {
        Collection<TransferUser> users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getByUid/{uid}")
    public TransferUser getUserByUid(@PathVariable("uid") int uid) {
        TransferUser transferUser =  userService.getUserByUid(uid);
        if (transferUser == null) {
            throw new NoContentException();
        } else {
            return transferUser;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getByEmail/{email}")
    public TransferUser getUserByEmail(@PathVariable("email" ) String email) {
        TransferUser transferUser =  userService.getUserByEmail(email);
        if (transferUser == null) {
            throw new NoContentException();
        } else {
            return transferUser;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register", produces = "application/json")
    public String createUser(@RequestBody User user) {
        return createJsonMessage(userService.createUser(user));
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
    public String chekcEmail(@PathVariable("email") String email) {
        return createJsonMessage(userService.isEmailFree(email));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update", produces = "application/json")
    public String updateUser(@RequestBody TransferUser user) {
        return createJsonMessage(userService.updateUser(user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/{username}")
    public Collection<TransferUser> searchUsersByUsername(@PathVariable("username") String username) {
        Collection<TransferUser> users = userService.getUserByUsername(username);
        if (users == null | users.isEmpty()) {
            throw new NoContentException();
        } else {
            return users;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth", produces = "application/json")
    public String authUserByEmail(@RequestBody User user) {
        // Special case because there is no passwordHash in a Transfer user
        return createJsonMessage(userService.authUserByEmail(user.getEmail(), user.getPasswordHash()));
    }

    @ExceptionHandler
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    private String createJsonMessage(String message) {
        String jsonMessage = "{\"message\": \"${message}\"}";
        jsonMessage = jsonMessage.replace("${message}", message);
        return jsonMessage;
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "No content to return")
    private class NoContentException extends RuntimeException {}
}
