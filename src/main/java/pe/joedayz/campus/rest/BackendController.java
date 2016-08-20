package pe.joedayz.campus.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@RestController
@RequestMapping("/backend")
public class BackendController {

	@Value("${backend.server}")
	private String server;

	@Value("${backend.port}")
	private int port;

	private static String SESSION_USER_NAME="SESSION_USER_NAME";

	@RequestMapping(value = "/get/**", produces = "application/json")
	@ResponseBody
	public String mirrorRest(HttpMethod method, HttpServletRequest request,
							 HttpServletResponse response) throws URISyntaxException
	{
		String url = stripMainContext(request.getRequestURI());
		URI uri = new URI("http", null, server, port, url, request.getQueryString(), null);

//		RestTemplate restTemplate = new RestTemplate();
		RestTemplate restTemplate = RestTemplateFactory.restTemplate();
		ResponseEntity<String> responseEntity =
				restTemplate.getForEntity(uri, String.class);

		return responseEntity.getBody();
	}

	/**
	 * Removes main URL context
	 * @param url
	 * @return
     */
	private String stripMainContext(String url){
		int contextSep = url.indexOf("/", 9);
		return url.substring(contextSep);
	}

	@RequestMapping(value= "/post/**",  method=RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String mirrorRest(@RequestBody String body, HttpMethod method, HttpServletRequest request,
							 HttpServletResponse response) throws URISyntaxException
	{
		String url = stripMainContext(request.getRequestURI());
		URI uri = new URI("http", null, server, port, url, request.getQueryString(), null);

		RestTemplate restTemplate = RestTemplateFactory.restStringTemplate();

//		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, String> headers =
				new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		//HttpHeaders headers=configHttpHeader();
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

		return responseEntity.getBody();
	}
/*
	private HttpHeaders configHttpHeader( ){
		HttpHeaders headers = new HttpHeaders();

//		headers.set(SESSION_USER_NAME,getCurrentSessionUser());
		headers.setAccept(Collections.singletonList(new MediaType("application","json")));

		return headers;
	}
*/
	private String getCurrentSessionUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = ((UserDetails) auth.getPrincipal()).getUsername();

		return username;
	}
}
