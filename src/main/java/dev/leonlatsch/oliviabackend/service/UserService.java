package dev.leonlatsch.oliviabackend.service;

import dev.leonlatsch.oliviabackend.constants.Formats;
import dev.leonlatsch.oliviabackend.dto.Container;
import dev.leonlatsch.oliviabackend.dto.PublicUserDTO;
import dev.leonlatsch.oliviabackend.dto.UserDTO;
import dev.leonlatsch.oliviabackend.entity.AccessToken;
import dev.leonlatsch.oliviabackend.entity.User;
import dev.leonlatsch.oliviabackend.repository.UserRepository;
import dev.leonlatsch.oliviabackend.util.Base64;
import dev.leonlatsch.oliviabackend.util.CommonUtils;
import dev.leonlatsch.oliviabackend.util.DatabaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static dev.leonlatsch.oliviabackend.constants.CommonResponses.*;
import static dev.leonlatsch.oliviabackend.constants.JsonResponse.*;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RabbitMQManagementService rabbitMQManagementService;

    private DatabaseMapper mapper = DatabaseMapper.getInstance();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Container getAllUsers(String accessToken) {
        if (!adminService.auth(accessToken)) {
            return RES_UNAUTHORIZED;
        }
        List<UserDTO> list = mapToTransferObjects(userRepository.findAll());

        for (UserDTO user : list) {
            rmProfilePic(user);
        }

        return new Container(200, OK, list);
    }

    public Container getPublicKey(String accessToken, String uid) {
        if (!accessTokenService.isTokenValid(accessToken)) {
            return RES_UNAUTHORIZED;
        }
        Optional<User> blob = userRepository.findPublicKeyByUid(uid);
        return blob.isPresent() ? new Container(200, OK, Base64.convertToBase64(blob.get().getPublicKey())) : RES_ERROR;
    }

    public Container updatePublicKey(String accessToken, String publicKey) {
        Container container = new Container();
        String uid = accessTokenService.getUserForToken(accessToken);
        if (uid == null) {
            return RES_UNAUTHORIZED;
        } else {
            Optional<User> user = userRepository.findById(uid);
            if (user.isPresent()) {
                User newUser = user.get();
                newUser.setPublicKey(Base64.convertToBlob(publicKey));
                userRepository.saveAndFlush(newUser);
                return RES_OK;
            } else {
                container.setCode(500); // Should never happen case
                container.setMessage(ERROR);
                container.setContent(null);
            }
        }
        return container;
    }

    public Container get(String accessToken) {
        Container container = new Container();
        String uid = accessTokenService.getUserForToken(accessToken);
        if (uid == null) {
            return RES_UNAUTHORIZED;
        }
        Optional<User> user = userRepository.findById(uid);
        rmProfilePic(user);
        if (user.isPresent()) {
            container.setCode(200);
            container.setMessage(OK);
            container.setContent(mapper.mapToTransferObject(user.get()));
            return container;
        } else {
            return RES_ERROR;
        }
    }

    public Container get(String accessToken, String uid) {
        Container container = new Container();
        if (accessTokenService.isTokenValid(accessToken)) {
            Optional<User> user = userRepository.findById(uid);
            if (user.isPresent()) {
                container.setCode(200);
                container.setMessage(OK);
                container.setContent(mapper.mapToPublicUser(user.get()));
            } else {
                container.setContent(201);
                container.setMessage(OK);
                container.setContent(null);
            }
            return container;
        } else {
            return RES_UNAUTHORIZED;
        }
    }

    public Container createUser(UserDTO user, String publicKey) {
        Container container = new Container();
        Optional<User> checkUser = userRepository.findByUsername(user.getUsername());
        if (checkUser.isPresent()) {
            return RES_ERROR;
        }

        Blob publicKeyBlob = Base64.convertToBlob(publicKey);
        if (publicKeyBlob == null) {
            log.error("Error decoding public key: " + publicKey);
            return RES_ERROR;
        }

        User entity = mapper.mapToEntity(user);
        String uid = CommonUtils.genSafeUid();
        entity.setUid(uid);
        entity.setPublicKey(publicKeyBlob);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        if (userRepository.saveAndFlush(entity) == null) {
            log.error("Error saving user: " + uid);
            return RES_ERROR;
        }
        AccessToken token = new AccessToken();
        String rawToken = CommonUtils.genSafeAccessToken();
        token.setUid(uid);
        token.setValid(true);
        token.setToken(rawToken);
        if (accessTokenService.saveAccessToken(token) == null) {
            log.error("Error saving access token for user: " + uid);
            return RES_ERROR;
        }

        String queueName = Formats.USER_QUEUE_PREFIX + entity.getUid();
        brokerService.createQueue(queueName, true);
        if (!rabbitMQManagementService.createUser(uid, rawToken)) {
            return RES_ERROR;
        }

        container.setMessage(OK);
        container.setContent(rawToken);
        container.setCode(200);
        return container;
    }

    public Container deleteUser(String accessToken) {
        String uid = accessTokenService.getUserForToken(accessToken);
        if (uid == null) {
            return RES_ERROR;
        }
        userRepository.deleteById(uid);
        accessTokenService.disableAccessToken(accessToken);
        brokerService.deleteQueue(Formats.USER_QUEUE_PREFIX + uid);
        if (!rabbitMQManagementService.deleteUser(uid)) {
            return RES_ERROR;
        }

        return RES_OK;
    }

    public Container isUsernameFree(String accessToken, String username) {
        String uid = accessTokenService.getUserForToken(accessToken);
        Optional<User> user = userRepository.findByUsername(username);
        Container container = new Container();
        container.setCode(200);
        container.setContent(null);
        if (!user.isPresent()) {
            container.setMessage(FREE);
        } else if (user.get().getUid().equals(uid)) {
            container.setMessage(TAKEN_BY_YOU);
        } else {
            container.setMessage(TAKEN);
        }
        return container;
    }

    public Container isEmailFree(String accessToken, String email) {
        String uid = accessTokenService.getUserForToken(accessToken);
        Optional<User> user = userRepository.findByEmail(email);
        Container container = new Container();
        container.setCode(200);
        container.setContent(null);
        if (!user.isPresent()) {
            container.setMessage(FREE);
        } else if (user.get().getUid().equals(uid)) {
            container.setMessage(TAKEN_BY_YOU);
        } else {
            container.setMessage(TAKEN);
        }
        return container;
    }

    public Container updateUser(String accessToken, UserDTO userDTO) {
        Container container = new Container();
        String uid = accessTokenService.getUserForToken(accessToken);

        if (uid == null) {
            return RES_ERROR;
        }
        User user = mapper.mapToEntity(userDTO);
        user.setUid(uid);

        Optional<User> dbUser = userRepository.findById(uid);
        if (dbUser.isPresent()) {
            if (user.getEmail() != null) {
                dbUser.get().setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                dbUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
                String newToken = updateAccessToken(dbUser.get().getUid());
                if (!rabbitMQManagementService.changeBrokerPassword(dbUser.get().getUid(), newToken)) {
                    return RES_ERROR;
                }
                container.setContent(newToken);
            }
            if (user.getProfilePic() != null) {
                dbUser.get().setProfilePic(user.getProfilePic());
            }
            if (user.getProfilePicTn() != null) {
                dbUser.get().setProfilePicTn(user.getProfilePicTn());
            }
            userRepository.saveAndFlush(dbUser.get());
            container.setCode(200);
            container.setMessage(OK);
            return container;
        } else {
            return RES_ERROR;
        }
    }

    public Container search(String accessToken, String username) {
        if (!accessTokenService.isTokenValid(accessToken)) {
            return RES_UNAUTHORIZED;
        }

        String uid = accessTokenService.getUserForToken(accessToken);
        List<User> users = userRepository.findTop100ByUidNotAndUsernameContaining(uid, username);

        return new Container(200, OK, mapToPublicUsers(mapToTransferObjects(users)));
    }

    public Container authUserByEmail(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        Container container = new Container();

        if (password == null || !user.isPresent()) {
            return RES_UNAUTHORIZED;
        }

        String token = accessTokenService.getTokenForUser(user.get().getUid());
        if (passwordEncoder.matches(password, user.get().getPassword()) && token != null) {
            container.setCode(200);
            container.setMessage(AUTHORIZED);
            container.setContent(token);
            return container;
        } else {
            return RES_UNAUTHORIZED;
        }
    }

    public Container loadProfilePic(String accessToken, String uid) {
        if (!accessTokenService.isTokenValid(accessToken)) {
            return RES_UNAUTHORIZED;
        }
        String profilePic;
        Optional<User> user = userRepository.findById(uid);
        profilePic = user.isPresent() ? Base64.convertToBase64(user.get().getProfilePic()) : null;

        return new Container(200, OK, profilePic);
    }

    public boolean userExists(String uid) {
        Optional<User> user = userRepository.findById(uid);
        return user.isPresent();
    }

    private List<PublicUserDTO> mapToPublicUsers(Collection<UserDTO> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        List<PublicUserDTO> publicUsers = new ArrayList<>();
        for (UserDTO dto : users) {
            publicUsers.add(mapper.mapToPublicUser(dto));
        }
        return publicUsers;
    }

    private List<UserDTO> mapToTransferObjects(Collection<User> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        List<UserDTO> transferObjects = new ArrayList<>();
        for (User user : entities) {
            transferObjects.add(mapper.mapToTransferObject(user));
        }
        return transferObjects;
    }

    private String updateAccessToken(String uid) {
        String oldToken = accessTokenService.getTokenForUser(uid);
        accessTokenService.disableAccessToken(oldToken);
        String newToken = CommonUtils.genSafeAccessToken();
        AccessToken accessToken = new AccessToken();
        accessToken.setUid(uid);
        accessToken.setToken(newToken);
        accessToken.setValid(true);
        accessTokenService.saveAccessToken(accessToken);
        return newToken;
    }

    private void rmProfilePic(UserDTO dto) {
        dto.setProfilePic(null);
    }

    private void rmProfilePic(Optional<User> dto) {
        if (dto.isPresent()) {
            dto.get().setProfilePic(null);
        }
    }

    private void rmProfilePic(User user) {
        user.setProfilePic(null);
    }
}
