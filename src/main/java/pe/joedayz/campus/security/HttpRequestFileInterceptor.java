package pe.joedayz.campus.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by rsoto on 13/06/2016.
 */
public class HttpRequestFileInterceptor implements ClientHttpRequestInterceptor {

    private static String SESSION_USER_NAME="SESSION_USER_NAME";

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.set(SESSION_USER_NAME,getCurrentSessionUser());

        headers.setAccept(Collections.singletonList(new MediaType("application","vnd.openxmlformats-officedocument.spreadsheetml.sheet")));

        return execution.execute(request, body);
    }


    private String getCurrentSessionUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null &&  auth.getPrincipal()!=null){
            if (auth.getPrincipal() instanceof UserDetails) {
                String username = ((UserDetails) auth.getPrincipal()).getUsername();
                return username;
            }
        }
        return "";
    }
}