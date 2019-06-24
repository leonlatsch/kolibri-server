package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.repository.UserRepository;
import de.leonlatsch.oliviabackend.util.ImageHelper;
import de.leonlatsch.oliviabackend.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String FAIL = "FAIL";
    private static final String TAKEN = "TAKEN";
    private static final String FREE = "FREE";

    @Autowired
    private UserRepository userRepository;

    private Mapper mapper = new Mapper();

    public List<UserDTO> getAllUsers() {
        List<UserDTO> list = mapToTransferObjects(userRepository.findAll());

        for (UserDTO user : list) {
            user.setProfilePic(null);
        }

        return list;
    }

    public UserDTO getUserByUid(int uid) {
        Optional<User> user = userRepository.findById(uid);
        return user.isPresent() ? mapper.mapUserToTransferObject(user.get()) : null;
    }

    public UserDTO getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() ? mapper.mapUserToTransferObject(user.get()) : null;
    }

    public String createUser(UserDTO user) {
        Optional<User> checkUser = userRepository.findByUsername(user.getUsername());
        if (checkUser.isPresent()) {
            return ERROR;
        }

        Blob profilePic = ImageHelper.loadDefaultProfilePic();
        User entity = mapper.mapToUserEntity(user);

        entity.setUid(genUid());
        entity.setProfilePic(profilePic);
        entity.setProfilePicTn(ImageHelper.createThumbnail(profilePic));
        return userRepository.saveAndFlush(entity) != null ? OK : ERROR;
    }

    public String deleteUser(int uid) {
        userRepository.deleteById(uid);
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

    public String updateUser(UserDTO userDTO) {
        if (userDTO.getUid() < 10000000) {
            return "UID_IS_NULL";
        }
        User user = mapper.mapToUserEntity(userDTO);

        Optional<User> dbUser = userRepository.findById(user.getUid());
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

    public List<UserDTO> getUserByUsername(String username) {
        List<User> users = userRepository.findByUsernameContaining(username);
        return mapToTransferObjects(users);
    }

    public String authUserByEmail(String email, String hash) {
        Optional<User> user = userRepository.findByEmail(email);

        if (hash == null || !user.isPresent()) {
            return ERROR;
        }

        if (user.get().getPassword().equals(hash)) {
            return OK;
        } else {
            return FAIL;
        }
    }



    private int genUid() {
        Random rnd = new Random();
        return 10000000 + rnd.nextInt(99999999);
    }

    private List<UserDTO> mapToTransferObjects(Collection<User> entities) {
        if (entities == null) {
            return null;
        }
        List<UserDTO> transferObjects = new ArrayList<>();
        for (User user : entities) {
            transferObjects.add(mapper.mapUserToTransferObject(user));
        }
        return transferObjects;
    }

    private void rmPic(UserDTO dto) {
        dto.setProfilePic(null);
    }

    private void rmPic(Optional<UserDTO> dto) {
        if (dto.isPresent()) {
            dto.get().setProfilePic(null);
        }
    }
}
