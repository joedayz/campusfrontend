package pe.joedayz.campus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kendo")
public class KendoController {
    @RequestMapping("/index")
    String index(){
        return "kendo/index::partial-view";
    }


    @RequestMapping("/grid")
    String grid(){
        return "kendo/grid::partial-view";
    }

}
