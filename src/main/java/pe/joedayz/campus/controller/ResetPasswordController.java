package pe.joedayz.campus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ResetPasswordController {
    @Value("${spring.min-passwd-length}")
    private String minPassLength;

    @RequestMapping("/reset-password")
    ModelAndView index(){
        ModelAndView mav = new ModelAndView("reset-password");
        mav.addObject("minPassLength", minPassLength);
        return mav;
    }



}
