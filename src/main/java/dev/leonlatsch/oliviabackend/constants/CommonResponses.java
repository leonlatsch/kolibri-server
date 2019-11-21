package dev.leonlatsch.oliviabackend.constants;

import dev.leonlatsch.oliviabackend.dto.Response;

import static dev.leonlatsch.oliviabackend.constants.JsonResponse.*;

public class CommonResponses {

    public static final Response RES_UNAUTHORIZED = new Response(401, UNAUTHORIZED, null);
    public static final Response RES_OK = new Response(200, OK, null);
    public static final Response RES_ERROR = new Response(400, ERROR, null);

    private CommonResponses() {}
}
