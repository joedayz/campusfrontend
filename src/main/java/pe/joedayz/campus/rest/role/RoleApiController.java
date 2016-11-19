package pe.joedayz.campus.rest.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.joedayz.campus.dto.PageableResult;
import pe.joedayz.campus.dto.RoleFilterDto;
import pe.joedayz.campus.dto.RoleViewDto;
import pe.joedayz.campus.rest.BackendRestInvoker;

@RestController
@RequestMapping("/roleApi")
public class RoleApiController {

    final Logger LOG = LoggerFactory.getLogger(getClass());


    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;


    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/search/find", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    PageableResult find(RoleFilterDto prFilter
    ) {

        PageableResult pageableResult = new PageableResult();
         BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);

        @SuppressWarnings("unchecked")
		ResponseEntity<PageableResult> responseEntity =
                restInvoker.sendPost("/role/find/", prFilter, PageableResult.class);


        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            pageableResult = responseEntity.getBody();
        }

        return pageableResult;
    }




    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody RoleViewDto request) {

        BackendRestInvoker restInvoker = new BackendRestInvoker<String>(server, port);

        ResponseEntity<String> responseEntity =
                restInvoker.sendPost("/role/create", request, String.class);

        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestBody RoleViewDto request) {
        BackendRestInvoker restInvoker = new BackendRestInvoker<String>(server, port);

        ResponseEntity<String> responseEntity =
                restInvoker.sendPost("/role/delete", request, String.class);

        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);

    }



}

