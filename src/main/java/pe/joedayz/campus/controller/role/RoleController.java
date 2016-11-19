package pe.joedayz.campus.controller.role;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.joedayz.campus.dto.ModuleViewDto;
import pe.joedayz.campus.dto.RoleViewDto;
import pe.joedayz.campus.enums.ActiveInactiveStatusEnum;
import pe.joedayz.campus.enums.YesNoEnum;
import pe.joedayz.campus.service.impl.UserServiceImpl;
import pe.joedayz.campus.service.intf.RoleService;
import pe.joedayz.campus.util.ModuleCodes;

@Controller
@RequestMapping("/role")
public class RoleController {

    final Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Autowired
    RoleService roleService;

    @Autowired
    UserServiceImpl userServicePermission;


    @RequestMapping("/search")
    ModelAndView index()throws Exception{

        boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.ROLE_SEARCH);

        ModelAndView mav = new ModelAndView("role/role-search::partial-view");
        mav.addObject("readOnlyModule",readOnlyModule);

        return mav;
    }


    @RequestMapping("/edit")
    ModelAndView roleEdit(Long roleId) throws Exception{
        LOG.info("--- role :edit::start ---");

        boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.ROLE_VIEW);

        RoleViewDto roleViewDto=roleService.find(roleId);

        ModelAndView mav = new ModelAndView("role/role-view::partial-view");

        if(YesNoEnum.YES.equals(YesNoEnum.findByCode(roleViewDto.getIsEditForceReadOnly()))){
            readOnlyModule=true;
        }

        //String serialization = OBJECT_MAPPER.writeValueAsString(rampAreaDto);
        //LOG.info("ramp area -> "+serialization);
        //mav.addObject("rampAreaModel", serialization);
        mav.addObject("roleModel", roleViewDto);
        mav.addObject("readCode", YesNoEnum.NO.equals(YesNoEnum.findByCode(roleViewDto.getIsEditCode())));
        //mav.addObject("isEdit", true);
        mav.addObject("readOnlyModule",readOnlyModule);
        mav.addObject("title","Edit Role "+roleViewDto.getRoleCode());
        LOG.info("--- role:edit::finish ---");
        return mav;
    }


    @RequestMapping("/new")
    ModelAndView create() throws JsonProcessingException {
        LOG.info("--- role:create::start ---");
        boolean readOnlyModule = userServicePermission.hasROAccess("SE05");

        ModelAndView mav = new ModelAndView("role/role-view::partial-view");
        RoleViewDto roleViewDto = new RoleViewDto();

        roleViewDto.setRoleStatus(ActiveInactiveStatusEnum.ACTIVE.getCode());

        List<ModuleViewDto> moduleDtos = roleService.findMoludesActive();

        roleViewDto.setModules(moduleDtos);

        mav.addObject("roleModel", roleViewDto);
       // mav.addObject("isEdit", false);
        mav.addObject("readOnlyModule",readOnlyModule);
        mav.addObject("title","New Role");
        LOG.info("---role:create::finish ---");
        return mav;
    }



}
