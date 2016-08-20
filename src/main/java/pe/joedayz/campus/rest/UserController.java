package pe.joedayz.campus.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.joedayz.campus.dto.AutoCompleteFilterDto;
import pe.joedayz.campus.dto.CustomerDto;
import pe.joedayz.campus.dto.OfficeDto;
import pe.joedayz.campus.dto.UserDto;

@RestController
@RequestMapping("/user")
public class UserController {

	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;

	@RequestMapping(value="/find", method=RequestMethod.GET, produces="application/json")
	public UserDto findByUsername(@RequestParam("username") String username) {

		BackendRestInvoker restInvoker= new BackendRestInvoker<List<OfficeDto>>(server,port);

		ResponseEntity<UserDto> responseEntity=
				restInvoker.sendGet("/user/findByName?username=" + username, UserDto.class);

		return responseEntity.getBody();
	}


	@RequestMapping(value="/find/autocomplete/pricinganalyst", method=RequestMethod.GET, produces="application/json")
	public List<UserDto> findPricingAnaLyst(@RequestParam("query") String query) {

		BackendRestInvoker restInvoker= new BackendRestInvoker<UserDto>(server,port);

		AutoCompleteFilterDto filter= new AutoCompleteFilterDto();
		filter.setQuery(query);
		ResponseEntity<List<UserDto>> responseEntity=
				restInvoker.sendGetList("/autocomplete/pricinganalyst",filter,UserDto.class);


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
/*
		List<UserDto> items= new ArrayList<UserDto>();

		for (int i=0;i<10;i++){

			UserDto item= new UserDto();
			item.setId(+i);
			item.setFirstName("Commercial Rep First Name "+ i);
			item.setLastName("Commercial Rep Last Name "+ i);
			item.setLogin("Login "+i);

			items.add(item);
		}

		return items;*/
	}


}
