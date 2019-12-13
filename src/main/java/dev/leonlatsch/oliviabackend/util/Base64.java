package dev.leonlatsch.oliviabackend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
public class Base64 {

    private static final Logger log = LoggerFactory.getLogger(Base64.class);

    public static Blob convertToBlob(String base64String) {
        if (base64String == null) {
            return null;
        }
        try {
            byte[] bytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(base64String);
            return new SerialBlob(bytes);
        } catch (SQLException e) {
            log.error("" + e);
            return null;
        }
    }

    public static String convertToBase64(Blob blob) {
        if (blob == null) {
            return null;
        }
        try {
            byte[] bytes = blob.getBytes(1L, (int) blob.length());
            return org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(bytes);
        } catch (SQLException e) {
            log.error("" + e);
            return null;
        }
    }
}
