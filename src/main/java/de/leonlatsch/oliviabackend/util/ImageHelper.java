package de.leonlatsch.oliviabackend.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class ImageHelper {

    private static final Logger log = LoggerFactory.getLogger(ImageHelper.class);

    public static Blob createThumbnail(Blob original) {
        try {
            byte[] bytes = original.getBytes(1L, (int) original.length());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .size(256, 256)
                    .toOutputStream(outputStream);
            bytes = outputStream.toByteArray();

            return new SerialBlob(bytes);
        } catch (SQLException | IOException e) {
            log.error("" + e);
            return null;
        }
    }
}
