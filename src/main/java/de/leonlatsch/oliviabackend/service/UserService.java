package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.AuthResponse;
import de.leonlatsch.oliviabackend.dto.ProfilePicDTO;
import de.leonlatsch.oliviabackend.dto.PublicUserDTO;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.entity.AccessToken;
import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.repository.AccessTokenRepository;
import de.leonlatsch.oliviabackend.repository.UserRepository;
import de.leonlatsch.oliviabackend.util.*;
import de.leonlatsch.oliviabackend.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.*;

import static de.leonlatsch.oliviabackend.constants.JsonResponse.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessTokenService accessTokenService;

    private DatabaseMapper mapper = DatabaseMapper.getInstance();

    public List<UserDTO> getAllUsers(String accessToken) {
        if (!AdminManager.getAdminAccessToken().equals(accessToken)) {
            return null;
        }
        List<UserDTO> list = mapToTransferObjects(userRepository.findAll());

        for (UserDTO user : list) {
            rmProfilePic(user);
        }

        return list;
    }

    public UserDTO get(String accessToken) {
        int uid = accessTokenService.getUserForToken(accessToken);
        if (uid == -1) {
            return null;
        }
        Optional<User> user = userRepository.findById(uid);
        rmProfilePic(user);
        return user.isPresent() ? mapper.mapToTransferObject(user.get()) : null;
    }

    public AuthResponse createUser(UserDTO user) {
        AuthResponse response = new AuthResponse();
        Optional<User> checkUser = userRepository.findByUsername(user.getUsername());
        if (checkUser.isPresent()) {
            response.setMessage(ERROR);
            response.setAccessToken(null);
            response.setSuccess(false);
            return response;
        }

        Blob profilePic = ImageHelper.loadDefaultProfilePic();
        User entity = mapper.mapToEntity(user);

        int uid = CommonUtils.genUid();
        entity.setUid(uid);
        entity.setProfilePic(profilePic);
        entity.setProfilePicTn(ImageHelper.createThumbnail(profilePic));
        if (userRepository.saveAndFlush(entity) == null) {
            response.setMessage(ERROR);
            response.setAccessToken(null);
            response.setSuccess(false);
            return response;
        }

        AccessToken token = new AccessToken();
        String rawToken = CommonUtils.genAccessToken(24);
        token.setUid(uid);
        token.setValid(true);
        token.setToken(rawToken);
        if (accessTokenRepository.saveAndFlush(token) == null) {
            response.setMessage(ERROR);
            response.setAccessToken(null);
            response.setSuccess(false);
            return response;
        }

        response.setMessage(OK);
        response.setAccessToken(rawToken);
        response.setSuccess(true);
        return response;
    }

    public String deleteUser(String accessToken) {
        int uid = accessTokenService.getUserForToken(accessToken);
        if (uid == -1) {
            return ERROR;
        }
        userRepository.deleteById(uid);
        accessTokenService.disableAccessToken(accessToken);
        return OK;
    }

    public String isUsernameFree(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() ? TAKEN : FREE;
    }

    public String isEmailFree(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() ? TAKEN : FREE;
    }

    public String updateUser(String accessToken, UserDTO userDTO) {
        int uid = accessTokenService.getUserForToken(accessToken);

        if (uid == -1) {
            return ERROR;
        }
        User user = mapper.mapToEntity(userDTO);
        user.setUid(uid);

        Optional<User> dbUser = userRepository.findById(uid);
        if (dbUser.isPresent()) {
            if (user.getUsername() == null) {
                user.setUsername(dbUser.get().getUsername());
            }
            if (user.getEmail() == null) {
                user.setEmail(dbUser.get().getEmail());
            }
            if (user.getPassword() == null) {
                user.setPassword(dbUser.get().getPassword());
            }
            if (user.getProfilePic() == null) {
                user.setProfilePic(dbUser.get().getProfilePic());
            }
            if (user.getProfilePicTn() == null) {
                user.setProfilePicTn(dbUser.get().getProfilePicTn());
            }
            userRepository.saveAndFlush(user);
            return OK;
        } else {
            return ERROR;
        }
    }

    public List<PublicUserDTO> search(String username) {
        List<User> users = userRepository.findByUsernameContaining(username);
        for (User user : users) {
            rmProfilePic(user);
        }
        return mapToPublicUsers(mapToTransferObjects(users));
    }

    public List<PublicUserDTO> searchTop100(String username) {
        List<User> users = userRepository.findTop100ByUsernameContaining(username);
        for (User user : users) {
            rmProfilePic(user);
        }

        return mapToPublicUsers(mapToTransferObjects(users));
    }

    public AuthResponse authUserByEmail(String email, String hash) {
        Optional<User> user = userRepository.findByEmail(email);
        AuthResponse response = new AuthResponse();

        if (hash == null || !user.isPresent()) {
            response.setMessage(UNAUTORIZED);
            response.setSuccess(false);
            return response;
        }

        String token = accessTokenService.getTokenForUser(user.get().getUid());
        if (user.get().getPassword().equals(hash) && token != null) {
            response.setMessage(AUTORIZED);
            response.setAccessToken(token);
            response.setSuccess(true);
            return response;
        } else {
            response.setMessage(UNAUTORIZED);
            response.setAccessToken(null);
            response.setSuccess(false);
            return response;
        }
    }

    public ProfilePicDTO loadProfilePic(String accessToken) {
        int uid = accessTokenService.getUserForToken(accessToken);
        if (uid == -1) {
            return null;
        }
        ProfilePicDTO profilePicDto = new ProfilePicDTO();
        Optional<User> user = userRepository.findById(uid);
        if (user.isPresent()) {
            profilePicDto.setProfilePic(Base64.convertToBase64(user.get().getProfilePic()));
        } else {
            profilePicDto.setProfilePic(null);
        }

        return profilePicDto;
    }

    private List<PublicUserDTO> mapToPublicUsers(Collection<UserDTO> users) {
        if (users == null) {
            return null;
        }

        List<PublicUserDTO> publicUsers = new ArrayList<>();
        for (UserDTO dto : users) {
            publicUsers.add(mapper.mapToPublicUser(dto));
        }
        return publicUsers;
    }

    private List<UserDTO> mapToTransferObjects(Collection<User> entities) {
        if (entities == null) {
            return null;
        }
        List<UserDTO> transferObjects = new ArrayList<>();
        for (User user : entities) {
            transferObjects.add(mapper.mapToTransferObject(user));
        }
        return transferObjects;
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
