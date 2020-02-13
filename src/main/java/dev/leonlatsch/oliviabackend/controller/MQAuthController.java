package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.constants.Formats;
import dev.leonlatsch.oliviabackend.model.dto.rabbitmq.ResourceCheck;
import dev.leonlatsch.oliviabackend.service.AdminService;
import dev.leonlatsch.oliviabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.StyledEditorKit;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/auth/mq")
public class MQAuthController {

    private static final String ALLOW = "allow";
    private static final String DENY = "deny";
    private static final String ALLOW_ADMIN = "allow administrator";

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public String user(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (adminService.rawAuth(username, password)) {
            return ALLOW_ADMIN;
        }
        return userService.authByUsernameAndToken(username, password) ? ALLOW : DENY;
    }

    @RequestMapping(value = "resource", method = RequestMethod.POST)
    public String resource(ResourceCheck check) {
        if (check.getUsername().equals(adminService.getUsername())) {
            return ALLOW_ADMIN;
        }
        //TODO: prettify
        return check.getUsername().equals(check.getName().replace(Formats.USER_QUEUE_PREFIX, "")) && check.getPermission().equals("read") && check.getResource().equals("queue") ? ALLOW : DENY;
    }

    @RequestMapping(value = "vhost", method = RequestMethod.POST)
    public String vhost() {
        return ALLOW;
    }

    @RequestMapping(value = "topic", method = RequestMethod.POST)
    public String topic() {
        return ALLOW;
    }
}
