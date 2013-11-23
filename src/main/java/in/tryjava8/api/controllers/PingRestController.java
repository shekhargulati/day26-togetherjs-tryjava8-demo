package in.tryjava8.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("ping")
public class PingRestController {

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("pong", HttpStatus.OK);
        return responseEntity;
    }
}
