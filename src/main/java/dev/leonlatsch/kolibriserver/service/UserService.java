package dev.leonlatsch.kolibriserver.service;

import dev.leonlatsch.kolibriserver.constants.Formats;
import dev.leonlatsch.kolibriserver.model.dto.Container;
import dev.leonlatsch.kolibriserver.model.dto.PublicUserDTO;
import dev.leonlatsch.kolibriserver.model.dto.UserDTO;
import dev.leonlatsch.kolibriserver.model.entity.AccessToken;
import dev.leonlatsch.kolibriserver.model.entity.User;
import dev.leonlatsch.kolibriserver.repository.UserRepository;
import dev.leonlatsch.kolibriserver.util.Base64;
import dev.leonlatsch.kolibriserver.util.CommonUtils;
import dev.leonlatsch.kolibriserver.util.DatabaseMapper;
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

import static dev.leonlatsch.kolibriserver.constants.CommonResponses.*;
import static dev.leonlatsch.kolibriserver.constants.JsonResponse.*;

/**
 * Service to manage users.
 * Also handles authorisation for normal users.
 *
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
        return blob.isPresent() ? new Container(200, OK, Base64.convertToBase64(blob.get().getPublicKey())) : RES_BAD_REQUEST;
    }

    public Container updatePublicKey(String accessToken, String publicKey) {
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
                return RES_INTERNAL_ERROR; // Should never happen case.
            }
        }
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
            return RES_INTERNAL_ERROR;
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
                container.setContent(204);
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
            return RES_BAD_REQUEST;
        }

        // Convert the provided public key into a blob
        Blob publicKeyBlob = Base64.convertToBlob(publicKey);
        if (publicKeyBlob == null) {
            log.error("Error decoding public key: " + publicKey);
            return RES_BAD_REQUEST;
        }

        // Save the user to the database
        User entity = mapper.mapToEntity(user);
        String uid = CommonUtils.genSafeUid();
        entity.setUid(uid);
        entity.setPublicKey(publicKeyBlob);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        if (!entity.validate()) {
            return RES_INTERNAL_ERROR;
        }

        userRepository.saveAndFlush(entity);

        // Generate a access token and save it to the database
        AccessToken token = new AccessToken();
        String rawToken = CommonUtils.genSafeAccessToken();
        token.setUid(uid);
        token.setValid(true);
        token.setToken(rawToken);
        if (!token.validate()) {
            return RES_INTERNAL_ERROR;
        }
        accessTokenService.saveAccessToken(token);

        // Create a queue for the user
        String queueName = Formats.USER_QUEUE_PREFIX + entity.getUid();
        brokerService.createQueue(queueName, true);

        container.setMessage(OK);
        container.setContent(rawToken);
        container.setCode(200);
        return container;
    }

    public Container deleteUser(String accessToken) {
        String uid = accessTokenService.getUserForToken(accessToken);
        if (uid == null) {
            return RES_UNAUTHORIZED;
        }

        // Delete the user, disable its access token and delete its queue
        userRepository.deleteById(uid);
        accessTokenService.disableAccessToken(accessToken);
        brokerService.deleteQueue(Formats.USER_QUEUE_PREFIX + uid);

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
            return RES_UNAUTHORIZED;
        }
        User user = mapper.mapToEntity(userDTO);
        user.setUid(uid);

        Optional<User> dbUser = userRepository.findById(uid);
        if (dbUser.isPresent()) {

            // Check all fields that can be changed
            if (user.getEmail() != null) {
                dbUser.get().setEmail(user.getEmail());
            }
            if (user.getPassword() != null) { // If there is a new password, generate a new access token and change the broker password
                dbUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
                String newToken = updateAccessToken(dbUser.get().getUid());
                container.setContent(newToken);
            }
            if (user.getProfilePic() != null) {
                dbUser.get().setProfilePic(user.getProfilePic());
            }
            if (user.getProfilePicTn() != null) {
                dbUser.get().setProfilePicTn(user.getProfilePicTn());
            }

            if (!dbUser.get().validate()) {
                return RES_BAD_REQUEST;
            }
            userRepository.saveAndFlush(dbUser.get());
            container.setCode(200);
            container.setMessage(OK);
            return container;
        } else {
            return RES_INTERNAL_ERROR; // Should never happen case
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

    public Container authWithPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
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

    /**
     * Used for rabbitmq authorisation
     *
     * @param username
     * @param token
     * @return true or false
     */
    public boolean authWithToken(String username, String token) {
        String uid = accessTokenService.getUserForToken(token);
        if (uid == null) {
            return false;
        }

        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            return user.get().getUid().equals(uid) && accessTokenService.getTokenForUser(uid).equals(token);
        } else {
            return false; // Should never happen case
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
