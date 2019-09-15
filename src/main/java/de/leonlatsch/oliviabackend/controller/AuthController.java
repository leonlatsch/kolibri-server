package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.constants.Headers;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static de.leonlatsch.oliviabackend.util.ControllerUtils.createResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<Response> login(@RequestBody UserDTO dto) {
        return createResponseEntity(userService.authUserByEmail(dto.getEmail(), dto.getPassword()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<Response> register(@RequestBody UserDTO dto, @RequestHeader(value = Headers.PUBLIC_KEY) String publicKey) {
        return createResponseEntity(userService.createUser(dto, publicKey));
    }
}
