package pe.joedayz.campus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ping")
public class PingController {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    @RequestMapping("/poke")
    public ResponseEntity<String> poke() {
        LOG.info("Refrescando session");
        String message =  "Refrescando Session";
        return   new ResponseEntity<String>("{\"message\":\""+message+"\"}", HttpStatus.CREATED);
    }



}
