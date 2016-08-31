package pe.joedayz.campus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/minor")
public class MinorController {
    @RequestMapping("/index")
    String index(){
        return "minor/index";
    }


}
