package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.repository.UserRepository;
import de.leonlatsch.oliviabackend.transfer.TransferUser;
import de.leonlatsch.oliviabackend.util.ImageHelper;
import de.leonlatsch.oliviabackend.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    private Mapper mapper = new Mapper();

    public List<TransferUser> getAllUsers() {
        return mapToTransferObjects(userRepository.findAll());
    }

    public TransferUser getUserByUid(int uid) {
        Optional<User> user = userRepository.findById(uid);
        return user.isPresent() ? mapper.mapUserToTransferObject(user.get()) : null;
    }

    public TransferUser getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() ? mapper.mapUserToTransferObject(user.get()) : null;
    }

    public String createUser(User user) {
        Optional<User> checkUser = userRepository.findByUsername(user.getUsername());
        if (checkUser.isPresent()) {
            return "ERROR";
        }
        user.setUid(genUid());
        user.setProfilePic(ImageHelper.loadDefaultProfilePic());
        return userRepository.saveAndFlush(user) != null ? "OK" : "ERROR";
    }

    public String deleteUser(int uid) {
        userRepository.deleteById(uid);
        return "OK";
    }

    public String isUsernameFree(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() ? "TAKEN" : "FREE";
    }

    public String isEmailFree(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() ? "TAKEN" : "FREE";
    }

    public String updateUser(TransferUser transferUser) {
        if (transferUser.getUid() < 10000000) {
            return "UID_IS_NULL";
        }
        User user = mapper.mapToUserEntity(transferUser);

        Optional<User> dbUser = userRepository.findById(user.getUid());
        if (dbUser.isPresent()) {
            if (user.getUsername() == null) {
                user.setUsername(dbUser.get().getUsername());
            }
            if (user.getEmail() == null) {
                user.setEmail(dbUser.get().getEmail());
            }
            if (user.getPasswordHash() == null) {
                user.setPasswordHash(dbUser.get().getPasswordHash());
            }
            if (user.getProfilePic() == null) {
                user.setProfilePic(dbUser.get().getProfilePic());
            }
            userRepository.saveAndFlush(user);
            return "OK";
        } else {
            return "ERROR";
        }
    }

    public List<TransferUser> getUserByUsername(String username) {
        List<User> users = userRepository.findByUsernameContaining(username);
        return mapToTransferObjects(users);
    }

    public String authUserByEmail(String email, String hash) {
        Optional<User> user = userRepository.findByEmail(email);

        if (hash == null || !user.isPresent()) {
            return "ERROR";
        }

        if (user.get().getPasswordHash().equals(hash)) {
            return "OK";
        } else {
            return "FAIL";
        }
    }

    private int genUid() {
        Random rnd = new Random();
        return 10000000 + rnd.nextInt(90000000);
    }

    private List<TransferUser> mapToTransferObjects(Collection<User> entities) {
        if (entities == null) {
            return null;
        }
        List<TransferUser> transferObjects = new ArrayList<>();
        for (User user : entities) {
            transferObjects.add(mapper.mapUserToTransferObject(user));
        }
        return transferObjects;
    }
}
