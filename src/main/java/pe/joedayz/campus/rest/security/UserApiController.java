package pe.joedayz.campus.rest.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.joedayz.campus.dto.JsonResult;
import pe.joedayz.campus.dto.PageableResult;
import pe.joedayz.campus.dto.RoleViewDto;
import pe.joedayz.campus.dto.UserDto;
import pe.joedayz.campus.dto.UserFilterDto;
import pe.joedayz.campus.rest.BackendRestInvoker;

@RestController
@RequestMapping("/userApi")
public class UserApiController {

    final Logger LOG = LoggerFactory.getLogger(getClass());


    List<RoleViewDto> tempRoleList;
    @Value("${backend.server}")
    private String server;
    @Value("${backend.port}")
    private int port;

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    PageableResult find(UserFilterDto filter) {
        PageableResult pageableResult = new PageableResult();
        final BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);

        final ResponseEntity<PageableResult> responseEntity =
                restInvoker.sendPost("/user/search/", filter, PageableResult.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            pageableResult = responseEntity.getBody();

        }

        return pageableResult;
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Long> save(@RequestBody UserDto request) {
        BackendRestInvoker restInvoker = new BackendRestInvoker<Long>(server, port);
        
        request.setRoleList(tempRoleList);
        ResponseEntity<Long> responseEntity = restInvoker.sendPost("/user/create", request, Long.class);
        Long processedId = responseEntity.getBody();

        if (processedId.intValue() != -1) {
            tempRoleList = new ArrayList<>();
            
        }
        return new ResponseEntity<>(processedId, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public ResponseEntity<Long> updateProfile(@RequestBody UserDto request) {
        BackendRestInvoker restInvoker = new BackendRestInvoker<Long>(server, port);
        ResponseEntity<Long> responseEntity = restInvoker.sendPost("/user/updateProfile", request, Long.class);
        Long processedId = responseEntity.getBody();
        return new ResponseEntity<>(processedId, HttpStatus.OK);
    }

    @RequestMapping(value = "/initTemps", method = RequestMethod.POST)
    public ResponseEntity<Long> initTemps(@RequestBody UserDto request) {

        if (request.getId() != null) {
            
            tempRoleList = request.getRoleList();
        } else {
            
            tempRoleList = new ArrayList<>();
        }
        return new ResponseEntity<>(request.getId() == null ? new Long(-1) : request.getId(), HttpStatus.OK);
    }

    @RequestMapping(value = "/cleanTemps", method = RequestMethod.POST)
    public ResponseEntity<Long> clean() {
        
        tempRoleList = null;
        return new ResponseEntity<>(new Long(-1), HttpStatus.OK);
    }




    @RequestMapping(value = "/searchRoles", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public PageableResult searchRoles() {
        if (tempRoleList == null) tempRoleList = new ArrayList<>();
        return loadPageableResult(tempRoleList);
    }

  

    @RequestMapping(value = "/addRole", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<JsonResult> addRole(@RequestParam long roleId, @RequestParam String roleName) throws Exception {
        JsonResult result = new JsonResult();
        RoleViewDto role = new RoleViewDto();
        role.setRoleId(roleId);
        role.setRoleName(roleName);
        if (!existsInRoleList(roleId)) {
            tempRoleList.add(role);
            result.setStatus("OK");
        } else {
            result.setStatus("ERROR");
            result.setDescription("Selected Role it's already in the list.");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




    @RequestMapping(value = "/removeRole", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<JsonResult> removeRole(@RequestBody long roleId) {
        JsonResult result = new JsonResult();
        RoleViewDto toDelete = null;

        for (RoleViewDto item : tempRoleList)
            if (item.getRoleId().intValue() == roleId) {
                toDelete = item;
                break;
            }
        tempRoleList.remove(toDelete);

        result.setStatus("OK");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    private PageableResult loadPageableResult(List<?> list) {
        PageableResult pageableResult = new PageableResult();
        pageableResult.setTotal(list.size());
        pageableResult.setResultList(list);
        return pageableResult;
    }




    private boolean existsInRoleList(long roleId) {
        boolean exists = false;
        for (RoleViewDto excluded : tempRoleList)
            if (excluded.getRoleId().intValue() == roleId) {
                exists = true;
                break;
            }
        return exists;
    }


}
