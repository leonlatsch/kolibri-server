package dev.leonlatsch.oliviabackend.util;

import dev.leonlatsch.oliviabackend.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
public class ControllerUtils {

    public static ResponseEntity<Response> createResponseEntity(Response response) {
        response.setTimestamp();
        return new ResponseEntity<Response>(response, HttpStatus.valueOf(response.getCode()));
    }
}
