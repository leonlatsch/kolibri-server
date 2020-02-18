package dev.leonlatsch.kolibriserver.controller;

import dev.leonlatsch.kolibriserver.constants.Headers;
import dev.leonlatsch.kolibriserver.model.dto.Container;
import dev.leonlatsch.kolibriserver.model.dto.UserDTO;
import dev.leonlatsch.kolibriserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Rest Controller to manage user accounts
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint to get all users.</br>
     * This is a admin feature.
     *
     * @param accessToken The admin access token
     * @return A {@link Container} with a list of all registered users
     */
    @RequestMapping(method = RequestMethod.GET, value = "get/all")
    public ResponseEntity<Container> getAllUsers(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.getAllUsers(accessToken));
    }

    /**
     * Endpoint to get the a user
     *
     * @param accessToken A valid access token
     * @param uid         The uid of the user to get
     * @return A {@link Container} with the requested user
     */
    @RequestMapping(method = RequestMethod.GET, value = "get/{uid}")
    public ResponseEntity<Container> get(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @PathVariable("uid") String uid) {
        return createResponseEntity(userService.get(accessToken, uid));
    }

    /**
     * Endpoint to get the own user.</br>
     *
     * @param accessToken The access token of the user
     * @returnA {@link Container} with the own user
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public ResponseEntity<Container> get(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.get(accessToken));
    }

    /**
     * Endpoint to get a public key of a user.
     *
     * @param accessToken A valid access token
     * @param uid         The uid of the user to get
     * @return A {@link Container} with the public key in base64
     */
    @RequestMapping(method = RequestMethod.GET, value = "/public-key/get/{uid}")
    public ResponseEntity<Container> getPublicKey(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("uid") String uid) {
        return createResponseEntity(userService.getPublicKey(accessToken, uid));
    }

    /**
     * Endpoint to update the own public key.</br>
     * Used when you login at a new device
     *
     * @param accessToken The access token of the user to update
     * @param publicKey   The base64 public key
     * @return An empty {@link Container}
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/public-key/update")
    public ResponseEntity<Container> updatePublicKey(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @RequestHeader(Headers.PUBLIC_KEY) String publicKey) {
        return createResponseEntity(userService.updatePublicKey(accessToken, publicKey));
    }

    /**
     * Endpoint to delete the own user.
     *
     * @param accessToken The access token of the user to delete
     * @return An empty {@link Container}
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity<Container> delete(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken) {
        return createResponseEntity(userService.deleteUser(accessToken));
    }

    /**
     * Endpoint to check if a username is already taken.
     *
     * @param accessToken A valid access token
     * @param username    The username to check
     * @return A {@link Container} with FREE, TAKEN or TAKEN_BY_YOU.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/check/username/{username}")
    public ResponseEntity<Container> checkUsername(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("username") String username) {
        return createResponseEntity(userService.isUsernameFree(accessToken, username));
    }

    /**
     * Endpoint to check if an email address is already taken.
     *
     * @param accessToken A valid access token
     * @param email       The email address to check
     * @return A {@link Container} with FREE, TAKEN or TAKEN_BY_YOU
     */
    @RequestMapping(method = RequestMethod.GET, value = "/check/email/{email}")
    public ResponseEntity<Container> checkEmail(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("email") String email) {
        return createResponseEntity(userService.isEmailFree(accessToken, email));
    }

    /**
     * Endpoint to update the own user.
     *
     * @param accessToken The access token of the user to update
     * @param user        A {@link UserDTO} with the new values
     * @return An empty {@link Container}
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/update", produces = "application/json")
    public ResponseEntity<Container> update(@RequestHeader(value = Headers.ACCESS_TOKEN) String accessToken, @RequestBody UserDTO user) {
        return createResponseEntity(userService.updateUser(accessToken, user));
    }

    /**
     * Endpoint to search for other users with a username.
     *
     * @param accessToken A valid access token
     * @param username    The username search query
     * @return A {@link Container} with a list of matching users
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search/{username}")
    public ResponseEntity<Container> searchUsersByUsername(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("username") String username) {
        return createResponseEntity(userService.search(accessToken, username));
    }

    /**
     * Endpoint to get the pull profile picture of a user.
     *
     * @param accessToken A valid access token
     * @param uid         The uid of the user to get the profile picture from
     * @return A {@link Container} with the full profile picture in base64
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get/profile-pic/{uid}")
    public ResponseEntity<Container> loadProfilePic(@RequestHeader(Headers.ACCESS_TOKEN) String accessToken, @PathVariable("uid") String uid) {
        return createResponseEntity(userService.loadProfilePic(accessToken, uid));
    }

    @ExceptionHandler
    public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
