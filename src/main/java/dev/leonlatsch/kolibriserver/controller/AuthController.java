package dev.leonlatsch.kolibriserver.controller;

import dev.leonlatsch.kolibriserver.constants.Headers;
import dev.leonlatsch.kolibriserver.model.dto.Container;
import dev.leonlatsch.kolibriserver.model.dto.UserDTO;
import dev.leonlatsch.kolibriserver.model.entity.Admin;
import dev.leonlatsch.kolibriserver.service.AdminService;
import dev.leonlatsch.kolibriserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller for handling the authentication
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    /**
     * Endpoint to login with an email address and a password hash.
     *
     * @param dto containing email and password
     * @return Container with the access token
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<Container> login(@RequestBody UserDTO dto) {
        return createResponseEntity(userService.authWithPassword(dto.getUsername(), dto.getPassword()));
    }

    /**
     * Endpoint to register a new user
     *
     * @param dto A {@link UserDTO} with a username, email and a password hash
     * @param publicKey Public key to encrypt incomming messages
     * @return A Container with a access token
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/register")
    public ResponseEntity<Container> register(@RequestBody UserDTO dto, @RequestHeader(value = Headers.PUBLIC_KEY) String publicKey, @RequestHeader(value = Headers.ACCESS_TOKEN, required = false) String accessToken) {
        return createResponseEntity(userService.createUser(dto, publicKey, accessToken));
    }

    /**
     * Endpoint to login with configured the admin user
     *
     * @param dto A {@link Admin} with a username and a password
     * @return A Container with the access token of the admin
     */
    @RequestMapping(method = RequestMethod.POST, value = "/admin/login")
    public ResponseEntity<Container> authAdmin(@RequestBody Admin dto) {
        return createResponseEntity(adminService.auth(dto.getUsername(), dto.getPassword()));
    }
}
