package pe.joedayz.campus.service.security;

import pe.joedayz.campus.dto.PageableResult;
import pe.joedayz.campus.rest.BackendRestInvoker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

/**
 * Invokes the backend to perform activities for the remember-me component
 * Created by JVergara on 16/06/2016.
 */
public class TokenServiceImpl implements PersistentTokenRepository  {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private String server;
    private int port;

    public TokenServiceImpl(String server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        try {
            BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);
            ResponseEntity<String> responseEntity = restInvoker.sendPost("/token/createNewToken/", token, String.class);
            LOGGER.info("New persistent token invoked: " + responseEntity.getBody());
        } catch(Exception e){
            LOGGER.error("Error creating new persistent token", e);
        }

    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        try {
            PersistentRememberMeToken token = new PersistentRememberMeToken("", series, tokenValue, lastUsed);
            BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);
            ResponseEntity<String> responseEntity = restInvoker.sendPost("/token/updateToken/", token, String.class);
            LOGGER.info("Update persistent token invoked: " + responseEntity.getBody());
        } catch(Exception e){
            LOGGER.error("Error updating persistent token", e);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);
            ResponseEntity<TemporalRememberMeToken> responseEntity = restInvoker.sendPost("/token/getTokenForSeries/", seriesId, TemporalRememberMeToken.class);
            TemporalRememberMeToken temporalToken = responseEntity.getBody();
            PersistentRememberMeToken token =
                    new PersistentRememberMeToken(temporalToken.getUsername(), temporalToken.getSeries(), temporalToken.getTokenValue(),
                            temporalToken.getDate());
            LOGGER.info("Token for series invoked: " + token);
            return token;
        } catch(Exception e){
            LOGGER.error("Error getting persistent token for series", e);
        }

        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        try {
            BackendRestInvoker restInvoker = new BackendRestInvoker<PageableResult>(server, port);
            ResponseEntity<String> responseEntity = restInvoker.sendPost("/token/removeUserTokens/", username, String.class);
            String token = responseEntity.getBody();
            LOGGER.info("Remove token invoked: " + token);
        } catch(Exception e){
            LOGGER.error("Error getting persistent token for series", e);
        }
    }

}
