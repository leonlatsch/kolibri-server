package dev.leonlatsch.oliviabackend.util;

import dev.leonlatsch.oliviabackend.dto.Container;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
public class ControllerUtils {

    public static ResponseEntity<Container> createResponseEntity(Container container) {
        container.setTimestamp();
        return new ResponseEntity<Container>(container, HttpStatus.valueOf(container.getCode()));
    }
}
