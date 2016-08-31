package pe.joedayz.campus.service.impl;


import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import pe.joedayz.campus.dto.PageableResult;
import pe.joedayz.campus.dto.UserDto;
import pe.joedayz.campus.enums.RoleEnum;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.intf.UserService;


@Service
public class UserServiceImpl implements UserService {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

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

    public static final String NO_PERMISSION = "N";
    public static final String READ_PERMISSION = "R";
    public static final String WRITE_PERMISSION = "W";

    public String getPermissionType(String moduleCode) {
        boolean result = false;
        List<String> permissionsForModule = new ArrayList<>();
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String userRole = authority.getAuthority();

            if (userRole != null && userRole.contains(moduleCode)){
                permissionsForModule.add(userRole.substring(0, 1));
            }
            }

        if (permissionsForModule.isEmpty()) {
            return NO_PERMISSION;
        }

        Optional<String> hasWritePermission = permissionsForModule.stream()
                .filter(permission -> WRITE_PERMISSION.equals(permission))
                .findFirst();

        return hasWritePermission.orElse(READ_PERMISSION);
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

    @Override
    public boolean hasPersistentToken(String username) {
        try {
            BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);
            ResponseEntity<String> responseEntity = restInvoker.sendGet("/token/getPersistentToken?username="+username, String.class);
            String token = responseEntity.getBody();
            if (token.contains("NoToken")){
                return false;
            }
            LOG.info("User has persistent token: " + token);
            return true;
        } catch(Exception e){
            LOG.error("Error getting persistent token for user " + username, e);
        }
        return false;
    }

}
