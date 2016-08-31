package pe.joedayz.campus.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pe.joedayz.campus.dto.ListResultModule;
import pe.joedayz.campus.dto.ModuleViewDto;
import pe.joedayz.campus.dto.RoleViewDto;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.service.intf.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    @Override
    public RoleViewDto find(Long roleId) {

        BackendRestInvoker restInvoker = new BackendRestInvoker<RoleViewDto>(server,port);

        ResponseEntity<RoleViewDto> responseEntity =
                restInvoker.sendGet("/role/find/"+roleId,  RoleViewDto.class);

        RoleViewDto result=null;
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            result = responseEntity.getBody();
        }

        return result;
    }




    @Override
    public List<RoleViewDto> findAllRoles() {
        List<RoleViewDto> roles= new ArrayList<>();
        ParameterizedTypeReference<List<RoleViewDto>> typeRef=new ParameterizedTypeReference<List<RoleViewDto>>() {
        };

        BackendRestInvoker restInvoker= new BackendRestInvoker<RoleViewDto>(server,port);

        ResponseEntity<List<RoleViewDto>> responseEntity = restInvoker.sendGet("/user/findRoles",typeRef);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            roles=responseEntity.getBody();

        }

        return roles;
    }

    @Override
    public List<ModuleViewDto> findMoludesActive() {

        BackendRestInvoker restInvoker = new BackendRestInvoker<ListResultModule>(server,port);

        ResponseEntity<ListResultModule> responseEntity =
                restInvoker.sendPost("/role/findModuleActive/",null,ListResultModule.class);

        return responseEntity.getBody().getResultList();

    }







}
