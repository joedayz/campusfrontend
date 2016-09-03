package pe.joedayz.campus.controller.curso;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pe.joedayz.campus.dto.CursoDto;
import pe.joedayz.campus.dto.UserDto;
import pe.joedayz.campus.enums.RoleEnum;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.intf.UserService;

@Controller
@RequestMapping("/curso")
public class CursoController {

	
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    
    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;
    
    
    @Autowired
    UserService userService;
    
    @RequestMapping("/novedades")
    public ModelAndView novedades() throws Exception{


        UserDto userDto=userService.getUserSession();
        List<RoleEnum> roles= new ArrayList<>();

        roles.add(RoleEnum.ALUMNO);
        roles.add(RoleEnum.PROFESOR);
        roles.add(RoleEnum.SYSTEM);

        boolean viewFilter=userService.hasAny(roles);

        ModelAndView mav = new ModelAndView("curso/curso-novedades::partial-view");
        mav.addObject("viewFilter",viewFilter);

        mav.addObject("pricingAnalystId",viewFilter?"":userDto.getId());
        return mav;
    }
    
    
    @SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping("/contenido/{key}")
    public ModelAndView contenido(@PathVariable("key") String keyCurso ) throws Exception{

        BackendRestInvoker restInvoker= new BackendRestInvoker<List<CursoDto>>(server, port);

        @SuppressWarnings("unchecked")
		ResponseEntity<CursoDto> responseEntity=
                restInvoker.sendGet("/user/findByName?username=", CursoDto.class);

        CursoDto cursoDto=responseEntity.getBody();
    	
    	
    	ModelAndView mav = new ModelAndView("curso/curso-contenido::partial-view");
        mav.addObject("keyCurso", keyCurso);

        return mav;
    }


}
