package pe.joedayz.campus.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @RequestMapping("/login")
    ModelAndView index(){
        ModelAndView mav = new ModelAndView("login");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails){
            String username = ((UserDetails) auth.getPrincipal()).getUsername();
            mav.addObject("username", username);
        }

        return mav;
    }


}
