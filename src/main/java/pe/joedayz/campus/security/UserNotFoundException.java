package pe.joedayz.campus.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by JVergara on 14/05/2016.
 */
public class UserNotFoundException  extends AuthenticationException  {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
