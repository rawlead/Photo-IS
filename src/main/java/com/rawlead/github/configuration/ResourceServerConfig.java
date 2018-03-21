package com.rawlead.github.configuration;

import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.RoleRepository;
import com.rawlead.github.repository.UserRepository;
import com.rawlead.github.service.impl.PhotoCategoryServiceImpl;
import com.rawlead.github.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *The @EnableResourceServer annotation adds a filter of type OAuth2AuthenticationProcessingFilter automatically
 *to the Spring Security filter chain.
 */
@Configuration
@EnableSwagger2
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()

                .antMatchers(HttpMethod.GET,"/").permitAll()

                .antMatchers(HttpMethod.DELETE, "/api/users/*").hasAuthority("ROLE_ADMIN")

                .antMatchers(HttpMethod.PUT,"/api/users/*/avatar").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/users/*/password").authenticated()
                .antMatchers(HttpMethod.PUT,"/api/users/*/email").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/users/*/avatar").authenticated()

                .antMatchers(HttpMethod.POST,"/api/users/*/photos").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/users/*/photos/*").authenticated()

                .antMatchers(HttpMethod.POST, "/api/categories").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/categories/*").hasAuthority("ROLE_ADMIN")

                .antMatchers(HttpMethod.POST,"/api/users/*/favorite/users").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/users/*/favorite/users/**").authenticated()

                .antMatchers(HttpMethod.POST,"/api/photos/*/comments").authenticated()
                .antMatchers(HttpMethod.DELETE,"/api/comments").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/users/*/comments/*").authenticated()

                .antMatchers(HttpMethod.POST,"/api/roles").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/roles/*").hasAuthority("ROLE_ADMIN");
    }

}
