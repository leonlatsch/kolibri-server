package dev.leonlatsch.oliviabackend.util;

import dev.leonlatsch.oliviabackend.model.dto.Container;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Static functions used in all controllers to create a {@link ResponseEntity} from a {@link Container}
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class ControllerUtils {

    public static ResponseEntity<Container> createResponseEntity(Container container) {
        container.setTimestamp();
        return new ResponseEntity<Container>(container, HttpStatus.valueOf(container.getCode()));
    }
}
