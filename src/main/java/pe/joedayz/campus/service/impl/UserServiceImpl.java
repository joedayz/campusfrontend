package pe.joedayz.campus.service.impl;


import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import pe.joedayz.campus.dto.UserDto;
import pe.joedayz.campus.enums.RoleEnum;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.intf.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;


    private UserDto findUserSession(){

        UserDto userDto=null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();

        BackendRestInvoker<UserDto> restInvoker = new BackendRestInvoker<UserDto>(server, port);
        ResponseEntity<UserDto> responseEntity= restInvoker.sendGet("/user/findByName?username="+username,UserDto.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            userDto = responseEntity.getBody();
        }

        return userDto;

    }


    public UserDto getUserSession(){
        return findUserSession();

    }

    protected boolean hasRole(String[] roles) {
        boolean result = false;
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String userRole = authority.getAuthority();
            for (String role : roles) {
                if (role.equals(userRole)) {
                    result = true;
                    break;
                }
            }

            if (result) {
                break;
            }
        }

        return result;
    }

    public String getPermissionType(String moduleCode) {
        boolean result = false;
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String userRole = authority.getAuthority();

            if (userRole != null && userRole.contains(moduleCode)){
                return userRole.substring(0, 1);
            }

        }

        return "N";
            }

    public boolean hasAccess(String moduleCode){
        String pt = getPermissionType(moduleCode);
        return !"N".equals(pt);
    }

    public boolean hasROAccess(String moduleCode){
        String pt = getPermissionType(moduleCode);
        return "R".equals(pt);
    }

    public boolean hasWRAccess(String moduleCode){
        String pt = getPermissionType(moduleCode);
        return "W".equals(pt);
    }

    public boolean hasAny(RoleEnum role){
        return hasAny(asList(role));

        }
    public boolean hasAny(List<RoleEnum> roles){
        List<String> codeRoles = roles.stream().map(r -> r.getCode()).collect(toList());
        return hasRole(codeRoles.toArray(new String[codeRoles.size()]));
    }
}
