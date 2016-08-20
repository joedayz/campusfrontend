package pe.joedayz.campus.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by JVergara on 14/05/2016.
 */
public class UserIsLockedException extends AuthenticationException  {
    public UserIsLockedException(String msg) {
        super(msg);
    }
}
