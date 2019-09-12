package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.constants.Headers;
import de.leonlatsch.oliviabackend.dto.AuthResponse;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserDTO dto) {
        AuthResponse response = userService.authUserByEmail(dto.getEmail(), dto.getPassword());

        return genResponse(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserDTO dto, @RequestHeader(value = Headers.PUBLIC_KEY) String publicKey) {
        AuthResponse response = userService.createUser(dto, publicKey);

        return genResponse(response);
    }

    private ResponseEntity<AuthResponse> genResponse(AuthResponse response) {
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
