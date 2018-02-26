package com.rawlead.github.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 *The @EnableResourceServer annotation adds a filter of type OAuth2AuthenticationProcessingFilter automatically
 *to the Spring Security filter chain.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST,"/posts").authenticated()
                .antMatchers(HttpMethod.PUT,"/users/**/updateAvatar").authenticated()
                .antMatchers(HttpMethod.DELETE,"/users/**/deleteAvatar").authenticated()
                .antMatchers(HttpMethod.PUT,"/users/**/updatePassword").authenticated()
                .antMatchers(HttpMethod.PUT,"/users/**/updateEmail").authenticated()
//                .antMatchers("/profile/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

    }

//    private AuthenticationEntryPoint authenticationEntryPoint() {
//        return new AuthenticationEntryPoint() {
//            @Override
//            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//                response.sendRedirect("/oauth/token?redirect_uri=localhost:8080/profile");
//            }
//        };
//    }
}
