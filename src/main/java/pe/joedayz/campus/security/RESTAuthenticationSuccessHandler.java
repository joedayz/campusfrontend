package pe.joedayz.campus.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by JVergara on 07/05/2016.
 */
@Component
public class RESTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /*@Value("${spring.sessionTimeOutMinutes}")
    private int sessionTimeOutMinutes;
    */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

//        clearAuthenticationAttributes(request);
        //request.getSession().setMaxInactiveInterval(sessionTimeOutMinutes * 60);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{\"success\": true}");
        response.getWriter().flush();
    }
}
