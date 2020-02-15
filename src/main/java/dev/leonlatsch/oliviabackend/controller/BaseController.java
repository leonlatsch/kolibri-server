package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.model.dto.Container;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    public ResponseEntity<Container> createResponseEntity(Container container) {
        container.setTimestamp();
        return new ResponseEntity<>(container, HttpStatus.valueOf(container.getCode()));
    }
}
