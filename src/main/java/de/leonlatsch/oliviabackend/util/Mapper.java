package de.leonlatsch.oliviabackend.util;

import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.dto.UserDTO;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

public class Mapper {

    public UserDTO mapUserToTransferObject(User user) {
        if (user == null) {
            return null;
        }
        try {
            return new UserDTO(user.getUid(), user.getUsername(), user.getEmail(), ImageHelper.convertToBase64(user.getProfilePic()));
        } catch (SQLException e) {
            return null;
        }
    }

    public User mapToUserEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        try {
            Blob profilePic = null;

            if (userDTO.getProfilePic() != null) {
                profilePic = new SerialBlob(ImageHelper.convertToBlob(userDTO.getProfilePic()));
            }
            return new User(userDTO.getUid(), userDTO.getUsername(), userDTO.getEmail(), null, profilePic);
        } catch (SQLException e) {
            return null;
        }
    }
}
