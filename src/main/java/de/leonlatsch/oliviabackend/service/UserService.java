package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.constants.Formats;
import de.leonlatsch.oliviabackend.dto.PublicUserDTO;
import de.leonlatsch.oliviabackend.dto.Response;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.entity.AccessToken;
import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.repository.UserRepository;
import de.leonlatsch.oliviabackend.security.AdminManager;
import de.leonlatsch.oliviabackend.util.Base64;
import de.leonlatsch.oliviabackend.util.CommonUtils;
import de.leonlatsch.oliviabackend.util.DatabaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static de.leonlatsch.oliviabackend.constants.CommonResponses.*;
import static de.leonlatsch.oliviabackend.constants.JsonResponse.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private RabbitMQService rabbitMQService;

    private DatabaseMapper mapper = DatabaseMapper.getInstance();

    public Response getAllUsers(String accessToken) {
        if (!AdminManager.getAdminAccessToken().equals(accessToken)) {
            return null;
        }
        List<UserDTO> list = mapToTransferObjects(userRepository.findAll());

        for (UserDTO user : list) {
            rmProfilePic(user);
        }

        return new Response(200, OK, list);
    }

    public Response getPublicKey(String accessToken, int uid) {
        if (!accessTokenService.isTokenValid(accessToken)) {
            return new Response(401, UNAUTHORIZED, null);
        }
        Optional<User> blob = userRepository.findPublicKeyByUid(uid);
        if (blob.isPresent()) {
            return new Response(200, OK, Base64.convertToBase64(blob.get().getPublicKey()));
        } else {
            return RES_ERROR;
        }
    }

    public Response updatePublicKey(String accessToken, String publicKey) {
        Response response = new Response();
        int uid = accessTokenService.getUserForToken(accessToken);
        if (uid == -1) {
            return RES_UNAUTHORIZED;
        } else {
            Optional<User> user = userRepository.findById(uid);
            if (user.isPresent()) {
                User newUser = user.get();
                newUser.setPublicKey(Base64.convertToBlob(publicKey));
                userRepository.saveAndFlush(newUser);
                return RES_OK;
            } else {
                response.setCode(500); // Should never happen case
                response.setMessage(ERROR);
                response.setContent(null);
            }
        }
        return response;
    }

    public Response get(String accessToken) {
        Response response = new Response();
        int uid = accessTokenService.getUserForToken(accessToken);
        if (uid == -1) {
            return RES_OK;
        }
        Optional<User> user = userRepository.findById(uid);
        rmProfilePic(user);
        if (user.isPresent()) {
            response.setCode(200);
            response.setMessage(OK);
            response.setContent(mapper.mapToTransferObject(user.get()));
            return response;
        } else {
            return RES_ERROR;
        }
    }

    public Response createUser(UserDTO user, String publicKey) {
        Response response = new Response();
        Optional<User> checkUser = userRepository.findByUsername(user.getUsername());
        if (checkUser.isPresent()) {
            return RES_ERROR;
        }

        User entity = mapper.mapToEntity(user);

        int uid = CommonUtils.genUid();
        entity.setUid(uid);
        entity.setPublicKey(Base64.convertToBlob(publicKey));
        if (userRepository.saveAndFlush(entity) == null) {
            return RES_ERROR;
        }
        AccessToken token = new AccessToken();
        String rawToken = CommonUtils.genAccessToken();
        token.setUid(uid);
        token.setValid(true);
        token.setToken(rawToken);
        if (accessTokenService.saveAccessToken(token) == null) {
            return RES_ERROR;
        }

        String queueName = Formats.USER_QUEUE_PREFIX + entity.getUid();
        rabbitMQService.createQueue(queueName, true);

        response.setMessage(OK);
        response.setContent(rawToken);
        response.setCode(200);
        return response;
    }

    public Response deleteUser(String accessToken) {
        int uid = accessTokenService.getUserForToken(accessToken);
        if (uid == -1) {
            return RES_ERROR;
        }
        userRepository.deleteById(uid);
        accessTokenService.disableAccessToken(accessToken);
        return RES_OK;
    }

    public Response isUsernameFree(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Response response = new Response();
        response.setCode(200);
        response.setContent(null);
        if (user.isPresent()){
            response.setMessage(TAKEN);
        } else {
            response.setMessage(FREE);
        }
        return response;
    }

    public Response isEmailFree(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        Response response = new Response();
        response.setCode(200);
        response.setContent(null);
        if (user.isPresent()){
            response.setMessage(TAKEN);
        } else {
            response.setMessage(FREE);
        }
        return response;
    }

    public Response updateUser(String accessToken, UserDTO userDTO) {
        Response response = new Response();
        int uid = accessTokenService.getUserForToken(accessToken);

        if (uid == -1) {
            return RES_ERROR;
        }
        User user = mapper.mapToEntity(userDTO);
        user.setUid(uid);

        Optional<User> dbUser = userRepository.findById(uid);
        if (dbUser.isPresent()) {
            if (user.getUsername() != null) {
                dbUser.get().setUsername(user.getUsername());
            }
            if (user.getEmail() != null) {
                dbUser.get().setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                dbUser.get().setPassword(user.getPassword());
                String newToken = updateAccessToken(dbUser.get().getUid());
                response.setContent(newToken);
            }
            if (user.getProfilePic() != null) {
                dbUser.get().setProfilePic(user.getProfilePic());
            }
            if (user.getProfilePicTn() != null) {
                dbUser.get().setProfilePicTn(user.getProfilePicTn());
            }
            userRepository.saveAndFlush(dbUser.get());
            response.setCode(200);
            response.setMessage(OK);
            return response;
        } else {
            return RES_ERROR;
        }
    }

    public Response search(String accessToken, String username) {
        if (!accessTokenService.isTokenValid(accessToken)) {
            return RES_UNAUTHORIZED;
        }
        List<User> users = userRepository.findByUsernameContaining(username);
        for (User user : users) {
            rmProfilePic(user);
        }
        return new Response(200, OK, mapToPublicUsers(mapToTransferObjects(users)));
    }

    public Response searchTop100(String accessToken, String username) {
        if (!accessTokenService.isTokenValid(accessToken)) {
            return RES_UNAUTHORIZED;
        }
        List<User> users = userRepository.findTop100ByUsernameContaining(username);
        for (User user : users) {
            rmProfilePic(user);
        }

        return new Response(200, OK, mapToPublicUsers(mapToTransferObjects(users)));
    }

    public Response authUserByEmail(String email, String hash) {
        Optional<User> user = userRepository.findByEmail(email);
        Response response = new Response();

        if (hash == null || !user.isPresent()) {
           return RES_UNAUTHORIZED;
        }

        String token = accessTokenService.getTokenForUser(user.get().getUid());
        if (user.get().getPassword().equals(hash) && token != null) {
            response.setCode(200);
            response.setMessage(AUTHORIZED);
            response.setContent(token);
            return response;
        } else {
            return RES_UNAUTHORIZED;
        }
    }

    public Response loadProfilePic(String accessToken, int uid) {
        if (!accessTokenService.isTokenValid(accessToken)) {
            return RES_UNAUTHORIZED;
        }
        String profilePic;
        Optional<User> user = userRepository.findById(uid);
        if (user.isPresent()) {
            profilePic = Base64.convertToBase64(user.get().getProfilePic());
        } else {
            profilePic = null;
        }

        return new Response(200, OK, profilePic);
    }

    public boolean userExists(int uid) {
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

    private String updateAccessToken(int uid) {
        String oldToken = accessTokenService.getTokenForUser(uid);
        accessTokenService.disableAccessToken(oldToken);
        String newToken = CommonUtils.genAccessToken();
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
