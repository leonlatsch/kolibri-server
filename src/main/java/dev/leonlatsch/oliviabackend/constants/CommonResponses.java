package dev.leonlatsch.oliviabackend.constants;

import dev.leonlatsch.oliviabackend.dto.Container;

import static dev.leonlatsch.oliviabackend.constants.JsonResponse.*;

/**
 * Static common Responses
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
public class CommonResponses {

    public static final Container RES_UNAUTHORIZED = new Container(401, UNAUTHORIZED, null);
    public static final Container RES_OK = new Container(200, OK, null);
    public static final Container RES_BAD_REQUEST = new Container(400, ERROR, null);
    public static final Container RES_INTERNAL_ERROR = new Container(500, ERROR, null);

    private CommonResponses() {
    }
}
