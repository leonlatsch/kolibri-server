package dev.leonlatsch.oliviabackend.constants;

import dev.leonlatsch.oliviabackend.dto.Container;

/**
 * Json Responses for the message property of a {@link Container}
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class JsonResponse {

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    public static final String FAIL = "FAIL";
    public static final String TAKEN = "TAKEN";
    public static final String TAKEN_BY_YOU = "TAKEN_BY_YOU";
    public static final String FREE = "FREE";
    public static final String AUTHORIZED = "AUTHORIZED";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    private JsonResponse() {
    }
}
