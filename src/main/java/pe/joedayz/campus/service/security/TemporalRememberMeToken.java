package pe.joedayz.campus.service.security;

import java.util.Date;

/**
 * Temporal class to receive the token from the backend
 * This is because fault PersistentRememberMeToken cannot instance without paramerets
 * Created by JVergara on 16/06/2016.
 */
public class TemporalRememberMeToken {
    private String username;
    private String series;
    private String tokenValue;
    private Date date;

    public String getUsername() {
        return username;
    }

    public String getSeries() {
        return series;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public Date getDate() {
        return date;
    }
}
