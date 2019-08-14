package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.dto.ProfilePicDTO;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.repository.UserRepository;
import de.leonlatsch.oliviabackend.util.Base64;
import de.leonlatsch.oliviabackend.util.CommonUtils;
import de.leonlatsch.oliviabackend.util.ImageHelper;
import de.leonlatsch.oliviabackend.util.DatabaseMapper;
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
    private UserRepository userRepository;

    private DatabaseMapper mapper = DatabaseMapper.getInstance();

    public List<UserDTO> getAllUsers() {
        List<UserDTO> list = mapToTransferObjects(userRepository.findAll());

        for (UserDTO user : list) {
            rmPic(user);
        }

        return list;
    }

    public UserDTO getUserByUid(int uid) {
        Optional<User> user = userRepository.findById(uid);
        rmPic(user);
        return user.isPresent() ? mapper.mapToTransferObject(user.get()) : null;
    }

    public UserDTO getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        rmPic(user);
        return user.isPresent() ? mapper.mapToTransferObject(user.get()) : null;
    }

    public String createUser(UserDTO user) {
        Optional<User> checkUser = userRepository.findByUsername(user.getUsername());
        if (checkUser.isPresent()) {
            return ERROR;
        }

        Blob profilePic = ImageHelper.loadDefaultProfilePic();
        User entity = mapper.mapToEntity(user);

        entity.setUid(CommonUtils.genUid());
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
        User user = mapper.mapToEntity(userDTO);

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
        for (User user : users) {
            rmPic(user);
        }
        return mapToTransferObjects(users);
    }

    public List<UserDTO> getUserTop100(String username) {
        List<User> users = userRepository.findTop100ByUsernameContaining(username);
        for (User user : users) {
            rmPic(user);
        }

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

    public ProfilePicDTO loadProfilePic(int uid) {
        ProfilePicDTO profilePicDto = new ProfilePicDTO();
        Optional<User> user = userRepository.findById(uid);
        if (user.isPresent()) {
            profilePicDto.setProfilePic(Base64.convertToBase64(user.get().getProfilePic()));
        } else {
            profilePicDto.setProfilePic(null);
        }

        return profilePicDto;
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

    private void rmPic(UserDTO dto) {
        dto.setProfilePic(null);
    }

    private void rmPic(Optional<User> dto) {
        if (dto.isPresent()) {
            dto.get().setProfilePic(null);
        }
    }

    private void rmPic(User user) {
        user.setProfilePic(null);
    }
}
