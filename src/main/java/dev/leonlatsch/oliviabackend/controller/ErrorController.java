package dev.leonlatsch.oliviabackend.controller;

import dev.leonlatsch.oliviabackend.model.dto.Container;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@Controller
public class ErrorController extends BaseController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final String PATH = "/error";

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(value = PATH)
    @ResponseBody
    public ResponseEntity<Container> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Container container = new Container();
        container.setCode(statusCode);
        container.setMessage(HttpStatus.valueOf(statusCode).getReasonPhrase());
        return createResponseEntity(container);
    }
}
