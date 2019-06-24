package de.leonlatsch.oliviabackend.util;

import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.dto.UserDTO;
import org.slf4j.IMarkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class Mapper {

    private static final Logger log = LoggerFactory.getLogger(Mapper.class);

    private boolean defaultPasswordFlag = false;

    public UserDTO mapUserToTransferObject(User user, boolean withPassword) {
        if (user == null) {
            return null;
        }
        try {
            UserDTO dto = new UserDTO();
            dto.setUid(user.getUid());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            if (withPassword) {
                dto.setPassword(user.getPassword());
            }
            dto.setProfilePic(ImageHelper.convertToBase64(user.getProfilePic()));
            dto.setProfilePicTn(ImageHelper.convertToBase64(user.getProfilePicTn()));
            return dto;
        } catch (SQLException e) {
            log.error("" + e);
            return null;
        }
    }

    public UserDTO mapUserToTransferObject(User user) {
        return mapUserToTransferObject(user, defaultPasswordFlag);
    }

    public User mapToUserEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        try {
            Blob profilePic = null;
            Blob profilePicTn = null;

            if (userDTO.getProfilePic() != null) {
                profilePic = new SerialBlob(ImageHelper.convertToBlob(userDTO.getProfilePic()));
                profilePicTn = new SerialBlob(ImageHelper.createThumbnail(profilePic));
            }
            return new User(userDTO.getUid(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), profilePic, profilePicTn);
        } catch (SQLException e) {
            log.error("" + e);
            return null;
        }
    }
}
