package dev.leonlatsch.oliviabackend.util;

import dev.leonlatsch.oliviabackend.constants.Formats;
import dev.leonlatsch.oliviabackend.model.dto.PublicUserDTO;
import dev.leonlatsch.oliviabackend.model.dto.UserDTO;
import dev.leonlatsch.oliviabackend.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * Mapper class to convert dtos to models and back
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class DatabaseMapper {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMapper.class);
    private static DatabaseMapper databaseMapper; // Singleton

    public static DatabaseMapper getInstance() {
        if (databaseMapper == null) {
            databaseMapper = new DatabaseMapper();
        }

        return databaseMapper;
    }

    public UserDTO mapToTransferObject(User user, boolean withPassword) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setUid(user.getUid());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        if (withPassword) {
            dto.setPassword(user.getPassword());
        }
        if (user.getProfilePic() != null) {
            dto.setProfilePic(Base64.convertToBase64(user.getProfilePic()));
        }
        if (user.getProfilePicTn() != null) {
            dto.setProfilePicTn(Base64.convertToBase64(user.getProfilePicTn()));
        }
        return dto;
    }

    public UserDTO mapToTransferObject(User user) {
        return mapToTransferObject(user, false);
    }

    public User mapToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        try {
            Blob profilePic = null;
            Blob profilePicTn = null;

            if (userDTO.getProfilePic() != null) {
                profilePic = new SerialBlob(Base64.convertToBlob(userDTO.getProfilePic()));
                profilePicTn = new SerialBlob(CommonUtils.createThumbnail(profilePic));
            }
            return new User(userDTO.getUid(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), profilePic, profilePicTn, null);
        } catch (SQLException e) {
            log.error("" + e);
            return null;
        }
    }

    public PublicUserDTO mapToPublicUser(User user) {
        return mapToPublicUser(mapToTransferObject(user, false));
    }

    public PublicUserDTO mapToPublicUser(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setUid(dto.getUid());
        publicUserDTO.setUsername(dto.getUsername());
        publicUserDTO.setProfilePicTn(dto.getProfilePicTn());

        return publicUserDTO;
    }

    private Timestamp stringToTimestamp(String timestamp) {
        try {
            Date parsed = Formats.DATE_FORMAT.parse(timestamp);
            return new Timestamp(parsed.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
