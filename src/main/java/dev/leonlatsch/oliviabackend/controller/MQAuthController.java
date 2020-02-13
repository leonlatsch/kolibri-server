package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.model.dto.rabbitmq.ResourceCheck;
import dev.leonlatsch.oliviabackend.model.dto.rabbitmq.VirtualHostCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/auth/mq")
public class MQAuthController {

    private static final String ALLOW = "allow";
    private static final String ALLOW_ADMIN = "allow administrator";
    private static final String DENY = "deny";

    @Autowired
    private Environment env;

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public String user(@RequestParam("username") String username, @RequestParam("password") String password) {
        String internalAdminUsername = env.getProperty("spring.rabbitmq.username");
        String internalAdminPassword = env.getProperty("spring.rabbitmq.password");
        if (username != null && password != null && username.equals(internalAdminUsername ) && password.equals(internalAdminPassword)) {
            return ALLOW_ADMIN;
        }
        return ALLOW;
    }

    @RequestMapping(value = "vhost", method = RequestMethod.POST)
    public String vhost(VirtualHostCheck check) {
        return ALLOW;
    }

    @RequestMapping(value = "resource", method = RequestMethod.POST)
    public String resource(ResourceCheck check) {
        return ALLOW;
    }
    @RequestMapping(value = "topic", method = RequestMethod.POST)
    public String topic() {
        return ALLOW;
    }
}
