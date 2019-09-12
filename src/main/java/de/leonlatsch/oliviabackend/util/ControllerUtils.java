package de.leonlatsch.oliviabackend.util;

import de.leonlatsch.oliviabackend.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerUtils {

    public static ResponseEntity<Response> createResponseEntity(Response response) {
        response.setTimestamp();
        return new ResponseEntity<Response>(response, HttpStatus.valueOf(response.getCode()));
    }
}
