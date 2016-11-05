package pe.joedayz.campus.controller.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

import pe.joedayz.campus.dto.AutoCompleteFilterDto;
import pe.joedayz.campus.dto.CustomerDto;
import pe.joedayz.campus.dto.UserDto;
import pe.joedayz.campus.dto.UserViewDto;
import pe.joedayz.campus.enums.ActiveInactiveStatusEnum;
import pe.joedayz.campus.enums.UserStatusEnum;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.impl.UserServiceImpl;
import pe.joedayz.campus.service.intf.RoleService;
import pe.joedayz.campus.util.ModuleCodes;

@Controller
@RequestMapping("/user")
public class UserController {

    final Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    @Autowired
    UserServiceImpl userServicePermission;



    @Autowired
    RoleService roleService;

    @RequestMapping("/search")
    ModelAndView index()throws Exception{
        ModelAndView mav = new ModelAndView("security/user-search::partial-view");
        boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.USER_SEARCH);
        mav.addObject("readOnlyModule",readOnlyModule);
        
        return mav;
    }

    @RequestMapping("/new")
    ModelAndView init() throws Exception {
        ModelAndView mav = new ModelAndView("security/user-view::partial-view");
        UserDto model= new UserDto();
        model.setStatus(ActiveInactiveStatusEnum.ACTIVE.getCode());
        mav.addObject("userModel",model);        
        mav.addObject("roles",roleService.findAllRoles());
        mav.addObject("title", "New User");
        mav.addObject("isEdit", false);
        return mav;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(Long userId) throws JsonProcessingException {
        ModelAndView modelAndView = new ModelAndView("security/user-view::partial-view");
        BackendRestInvoker restInvoker = new BackendRestInvoker<UserDto>(server,port);
        ResponseEntity<UserDto> responseEntity= restInvoker.sendGet("/user/find/"+userId,  UserDto.class);
        UserDto userDto= responseEntity.getBody();
        
        boolean readOnlyModule = userServicePermission.hasROAccess(ModuleCodes.USER_VIEW);

        modelAndView.addObject("roles",roleService.findAllRoles());
        modelAndView.addObject("userModel", userDto);
        modelAndView.addObject("title", "Edit User "+userDto.getUserName());
        modelAndView.addObject("isEdit", true);
        modelAndView.addObject("readOnlyModule",readOnlyModule);
        return modelAndView;
    }

    @RequestMapping("/profile")
    public ModelAndView editProfile()throws JsonProcessingException{
    	
        ModelAndView modelAndView = new ModelAndView("security/user-profile::partial-view");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();

        BackendRestInvoker restInvoker = new BackendRestInvoker<UserDto>(server,port);
        ResponseEntity<UserDto> responseEntity= restInvoker.sendGet("/user/findProfile/"+username,  UserDto.class);
        UserDto userDto= responseEntity.getBody();

        userDto.setStatus(UserStatusEnum.findByCode(userDto.getStatus()).getLabel());
        userDto.setPassword(null);
        userDto.setConfirmPassword(null);

        modelAndView.addObject("userProfileModel", userDto);
        return modelAndView;
    }

    // This was already in the code -- should be in UserApiController  -- commented by rsoto

    @RequestMapping(value="/find", method= RequestMethod.GET, produces="application/json")
    public UserDto findByUsername(@RequestParam("username") String username) {

        BackendRestInvoker restInvoker= new BackendRestInvoker<List<UserDto>>(server,port);

        ResponseEntity<UserDto> responseEntity=
                restInvoker.sendGet("/user/findByName?username=" + username, UserDto.class);

        return responseEntity.getBody();
    }

    @RequestMapping(value="/find/autocomplete/pricinganalyst", method=RequestMethod.GET, produces="application/json")
    public List<UserViewDto> findPricingAnaLystOld(@RequestParam("query") String query) {
     return findPricingAnalyst(query);

    }



    @RequestMapping(value="/find/autocompletePricingAnalyst", method=RequestMethod.GET, produces="application/json")
    public List<UserViewDto> findPricingAnalyst(@RequestParam("query") String query) {

        BackendRestInvoker restInvoker= new BackendRestInvoker<UserViewDto>(server,port);

        AutoCompleteFilterDto filter= new AutoCompleteFilterDto();
        filter.setQuery(query);
        ResponseEntity<List<UserViewDto>> responseEntity=
                restInvoker.sendGetList("/autocomplete/pricinganalyst",filter,UserViewDto.class);

        return responseEntity.getBody();

    }


    @RequestMapping(value="/find/autocomplete/commercialaccount", method=RequestMethod.GET, produces="application/json")
    public List<UserDto> findCommercialAccount(@RequestParam("query") String query) {

        BackendRestInvoker restInvoker= new BackendRestInvoker<List<CustomerDto>>(server,port);

        ResponseEntity<List<UserDto>> responseEntity=
                restInvoker.sendGet("/user/findUsersByName?name="+query,List.class);


        return responseEntity.getBody();
    }


    @RequestMapping(value="/find/autocomplete/commercialrep", method=RequestMethod.GET, produces="application/json")
    public List<UserDto> findCommercialRep( String query) {

        BackendRestInvoker restInvoker= new BackendRestInvoker<List<CustomerDto>>(server,port);

        ResponseEntity<List<UserDto>> responseEntity=
                restInvoker.sendGet("/user/findUsersByName?name="+query,List.class);


        return responseEntity.getBody();

    }

}
