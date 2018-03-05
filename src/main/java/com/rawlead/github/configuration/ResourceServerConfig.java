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
                .antMatchers(HttpMethod.POST,"/api/photos").authenticated()

                .antMatchers(HttpMethod.DELETE,"/api/users/**/photos/**").authenticated()
                .antMatchers(HttpMethod.POST,"/api/users/**/photos").authenticated()

                .antMatchers(HttpMethod.PUT,"/api/users/**/avatar").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/users/**/password").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/users/**/avatar").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/users/**/email").authenticated()

                .antMatchers(HttpMethod.POST,"/api/users/**/favorite/users").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/users/**/favorite/users/**").authenticated()

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
}
