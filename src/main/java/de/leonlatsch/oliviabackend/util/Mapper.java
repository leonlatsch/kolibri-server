package de.leonlatsch.oliviabackend.util;

import de.leonlatsch.oliviabackend.entity.User;
import de.leonlatsch.oliviabackend.transfer.TransferUser;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

public class Mapper {

    public TransferUser mapUserToTransferObject(User user) {
        if (user == null) {
            return null;
        }
        try {
            return new TransferUser(user.getUid(), user.getUsername(), user.getEmail(), ImageHelper.convertToBase64(user.getProfilePic()));
        } catch (SQLException e) {
            return null;
        }
    }

    public User mapToUserEntity(TransferUser transferUser) {
        if (transferUser == null) {
            return null;
        }

        try {
            Blob profilePic = new SerialBlob(ImageHelper.convertToBlob(transferUser.getProfilePic()));
            return new User(transferUser.getUid(), transferUser.getUsername(), transferUser.getEmail(), null, profilePic);
        } catch (SQLException e) {
            return null;
        }
    }
}
