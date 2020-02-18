package dev.leonlatsch.kolibriserver.service;

import dev.leonlatsch.kolibriserver.model.dto.Container;
import dev.leonlatsch.kolibriserver.model.entity.Admin;
import dev.leonlatsch.kolibriserver.repository.AdminRepository;
import dev.leonlatsch.kolibriserver.security.Hash;
import dev.leonlatsch.kolibriserver.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.leonlatsch.kolibriserver.constants.JsonResponse.AUTHORIZED;
import static dev.leonlatsch.kolibriserver.constants.JsonResponse.UNAUTHORIZED;

/**
 * Service to initialize and authenticate the admin
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private Environment env;

    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Container auth(String username, String password) {
        initAdmin();
        Optional<Admin> admin = adminRepository.findById(username);
        return admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword()) ? new Container(200, AUTHORIZED, admin.get().getToken()) : new Container(401, UNAUTHORIZED, null);
    }

    public boolean rawAuth(String username, String password) {
        initAdmin();
        String hashedPassword = Hash.genSha256Hex(password);
        Optional<Admin> admin = adminRepository.findById(username);
        return admin.isPresent() && passwordEncoder.matches(hashedPassword, admin.get().getPassword());
    }

    public boolean auth(String token) {
        return adminRepository.findByToken(token).isPresent();
    }

    public String getUsername() {
        return env.getProperty("admin.initial-username");
    }

    @EventListener(ContextRefreshedEvent.class)
    private void initAdmin() {
        if (adminRepository.findAll().isEmpty()) {
            String username = env.getProperty("admin.initial-username");
            String password = env.getProperty("admin.initial-password");
            if (username != null && password != null) {
                password = passwordEncoder.encode(password);
                Admin admin = new Admin(username, password, CommonUtils.genSafeAccessToken());
                if (admin.validate()) {
                    adminRepository.save(new Admin(username, password, CommonUtils.genSafeAccessToken()));
                    log.info("Generated new admin user from initial config");
                } else {
                    log.error("Error creating new admin from initial config");
                }
            }
        }
    }
}
