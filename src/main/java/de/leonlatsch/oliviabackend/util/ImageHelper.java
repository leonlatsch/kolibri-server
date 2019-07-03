package de.leonlatsch.oliviabackend.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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

    public static Blob createThumbnail(Blob original) {
        try {
            byte[] bytes = original.getBytes(1L, (int) original.length());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .size(64, 64)
                    .toOutputStream(outputStream);
            bytes = outputStream.toByteArray();

            return new SerialBlob(bytes);
        } catch (SQLException | IOException e) {
            log.error("" + e);
            return null;
        }
    }
}
