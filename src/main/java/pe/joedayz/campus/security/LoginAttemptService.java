package pe.joedayz.campus.security;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import pe.joedayz.campus.dto.OfficeDto;
import pe.joedayz.campus.rest.BackendRestInvoker;

/**
 * Created by JVergara on 14/05/2016.
 */
@Service
public class LoginAttemptService {

    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(final String username) {
        attemptsCache.invalidate(username);
    }

    public void loginFailed(String username) {
        Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);
        logger.warn("Authentication {}", username);

        int attempts = 0;
        try {
            attempts = attemptsCache.get(username);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(username, attempts);

        if (attempts == 2){
            logger.warn("Blocking user for too may auth request: " + username);
            BackendRestInvoker restInvoker= new BackendRestInvoker<List<OfficeDto>>(server, port);

            ResponseEntity<String> responseEntity = restInvoker.sendPost("/user/locked", username, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String response = responseEntity.getBody();
                logger.info("User locked response from backend {}", response);
                attemptsCache.invalidate(username);
            } else {
                logger.error("Error locking user in backend {}", username);
            }

        }
    }
}
