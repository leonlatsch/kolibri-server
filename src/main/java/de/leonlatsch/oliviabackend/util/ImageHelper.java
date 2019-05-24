package de.leonlatsch.oliviabackend.util;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class ImageHelper {

    private static final Logger log = LoggerFactory.getLogger(ImageHelper.class);

    public static Blob convertToBlob(String base64String) throws SQLException {
        byte[] bytes = Base64.decodeBase64(base64String);
        return new SerialBlob(bytes);
    }

    public static String convertToBase64(Blob blob) throws SQLException {
        byte[] bytes = blob.getBytes(1L, (int) blob.length());
        return Base64.encodeBase64String(bytes);
    }

    public static Blob loadDefaultProfilePic() {
        InputStream inputStream = ImageHelper.class.getClassLoader().getResourceAsStream("images/default_profile_pic.jpg");
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return new SerialBlob(bytes);
        } catch (IOException | SQLException e) {
            log.error("Error loading standard profile pic");
            throw new RuntimeException(e);
        }
    }
}
