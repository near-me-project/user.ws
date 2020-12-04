package near.me.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class StatusController {

    @GetMapping(path = "/status")
    public ResponseEntity<String> status() {
        return new ResponseEntity<String>(new String("OK. " + LocalDateTime.now()), HttpStatus.OK);
    }
}
