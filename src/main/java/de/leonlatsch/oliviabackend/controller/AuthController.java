package de.leonlatsch.oliviabackend.controller;

import de.leonlatsch.oliviabackend.dto.AuthResponse;
import de.leonlatsch.oliviabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public AuthResponse login() {
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public AuthResponse register() {
        return null;
    }
}
