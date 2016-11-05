package pe.joedayz.campus.config;


import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import pe.joedayz.campus.dto.ModuleDto;
import pe.joedayz.campus.dto.UserDto;
import pe.joedayz.campus.rest.BackendRestInvoker;
import pe.joedayz.campus.security.RESTAuthenticationEntryPoint;
import pe.joedayz.campus.security.RESTAuthenticationFailureHandler;
import pe.joedayz.campus.security.RESTAuthenticationSuccessHandler;
import pe.joedayz.campus.security.UserIsInactiveException;
import pe.joedayz.campus.security.UserIsLockedException;
import pe.joedayz.campus.security.UserNotFoundException;
import pe.joedayz.campus.service.security.TokenServiceImpl;


/**
 * Created by JVergara on 07/05/2016.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String AUTH_DEFAULT_ROLE = "CELTIC-USR";
    private static final String SECURITY_MESSAGES_PREFIX = "AbstractUserDetailsAuthenticationProvider.";

    @Value("${backend.server}")
    private String server;

    @Value("${backend.port}")
    private int port;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private RESTAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String noUsrMsg = messageSource.getMessage(SECURITY_MESSAGES_PREFIX + "noUser", new Object[]{}, new Locale("en"));
        String usrInactive = messageSource.getMessage(SECURITY_MESSAGES_PREFIX + "usrInactive", new Object[]{}, new Locale("en"));
        String usrLocked = messageSource.getMessage(SECURITY_MESSAGES_PREFIX + "usrLocked", new Object[]{}, new Locale("en"));

        auth
            .userDetailsService(new UserDetailsService() {
                @Override
                public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {
                    LOGGER.info("Authentication process init for user {}", username);
                    
                    
                    BackendRestInvoker restInvoker= new BackendRestInvoker<List<UserDto>>(server, port);
                    ResponseEntity<UserDto> responseEntity=
                            restInvoker.sendGet("/user/findByName?username=" + username, UserDto.class);

                    UserDto userDto = responseEntity.getBody();
                    LOGGER.info("User from database{}", userDto);

                    if (userDto == null){
                        LOGGER.info("User not found in database {}", userDto);
                        throw new UserNotFoundException(noUsrMsg);
                    }

                    if ("I".equals(userDto.getStatus())){
                        throw new UserIsInactiveException(usrInactive);
                    }

                    if ("L".equals(userDto.getStatus())){
                        throw new UserIsLockedException(usrLocked);
                    }

                    if (userDto != null) {

                        ResponseEntity<ModuleDto[]> responseEntity2 =
                                restInvoker.sendGet("/module/permissionModules?username=" + userDto.getUserName(), ModuleDto[].class);

                        ModuleDto[] allowedModules = responseEntity2.getBody();

                        UserDetails userDetails = new UserDetails() {
                            @Override
                            public Collection<? extends GrantedAuthority> getAuthorities() {
                                List<SimpleGrantedAuthority> userRoles = userDto.getRolList()
                                        .stream()
                                        .map(r -> new SimpleGrantedAuthority(r.getCode()))
                                        .collect(Collectors.toList());

                                List<SimpleGrantedAuthority> moduleRols = asList(allowedModules)
                                        .stream()
                                        .flatMap(mainM -> mainM.getSubModules().stream().map(m -> new SimpleGrantedAuthority(m.getCode()==null?"<<null>>": m.getPermissionType() + "|" + m.getCode().toUpperCase())))
                                        .collect(Collectors.toList());

                                List<SimpleGrantedAuthority> listFinal = new ArrayList<SimpleGrantedAuthority>();
                                listFinal.addAll(userRoles);
                                listFinal.addAll(moduleRols);
                                return listFinal;
                            }

                            @Override
                            public String getPassword() {
                                return userDto.getPassword();
                            }

                            @Override
                            public String getUsername() {
                                return userDto.getUserName();
                            }

                            @Override
                            public boolean isAccountNonExpired() {
                                return true;
                            }

                            @Override
                            public boolean isAccountNonLocked() {
                                return true;
                            }

                            @Override
                            public boolean isCredentialsNonExpired() {
                                return true;
                            }

                            @Override
                            public boolean isEnabled() {
                                return true;
                            }


                        };

                        return userDetails;
                    }
                    throw new UsernameNotFoundException("User '" + username + "' not found.");
                }
            });
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/backend/post/security/**", "/login", "/recover", "/reset-password", "/css/**", "/img/**", "/js/**", "/fonts/**", "/font-awesome/**").permitAll();

        BackendRestInvoker restInvoker= new BackendRestInvoker<List<ModuleDto>>(server,port);
        ResponseEntity<ModuleDto[]> responseEntity=
                restInvoker.sendGet("/module/visibleModules", ModuleDto[].class);


        http.authorizeRequests()
                .antMatchers("/**").authenticated();

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.csrf().disable();
        http.formLogin().successHandler(authenticationSuccessHandler);
        authenticationSuccessHandler.setUseReferer(true);
        http.formLogin().failureHandler(authenticationFailureHandler);
        http.formLogin()
                .loginPage("/login");
        http.rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(1209600); //two weeks
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        TokenServiceImpl db = new TokenServiceImpl(server, port);
        return db;
    }



}
