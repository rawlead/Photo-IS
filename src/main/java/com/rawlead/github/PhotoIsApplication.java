package com.rawlead.github;

import com.rawlead.github.configuration.CustomUserDetails;
import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.RoleRepository;
import com.rawlead.github.repository.UserRepository;
import com.rawlead.github.service.impl.PhotoCategoryServiceImpl;
import com.rawlead.github.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class PhotoIsApplication {
    private PasswordEncoder passwordEncoder;
    private PhotoCategoryServiceImpl categoryService;

    @Autowired
    public PhotoIsApplication(PasswordEncoder passwordEncoder, PhotoCategoryServiceImpl categoryService) {
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
    }

    //Graphic api interface swagger2
    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(path -> path.startsWith("/api/"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PhotoIsApplication.class, args);
    }

    /**
     * Password grants are switched on by injecting on AuthenticationManagaer
     *
     * @param builder
     * @param userRepository
     * @throws Exception
     */
    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository userRepository, RoleRepository roleRepository, UserServiceImpl service) throws Exception {
        if (userRepository.count() == 0) {

            roleRepository.save(new Role("USER"));
            roleRepository.save(new Role("PHOTOGRAPHER"));
            roleRepository.save(new Role("ADMIN"));

            service.save(new User("NameUser", "LastNameUser", "user@mail.com", "user", passwordEncoder.encode("password"), Stream.of(roleRepository.findByName("USER"), roleRepository.findByName("PHOTOGRAPHER")).collect(Collectors.toSet()), "/img/user-icon-white.png"));
            service.save(new User("NameUserTwo", "", "usertwo@mail.co,", "usertwo", passwordEncoder.encode("password"), Stream.of(roleRepository.findByName("USER"), roleRepository.findByName("PHOTOGRAPHER")).collect(Collectors.toSet()), "/img/user-icon-white.png"));
            service.save(new User("NameAdmin", "LastNameAdmin", "admin@mail.com", "admin", passwordEncoder.encode("password"), Stream.of(roleRepository.findByName("USER"), roleRepository.findByName("ADMIN")).collect(Collectors.toSet()), "/img/user-icon-white.png"));

            categoryService.addCategory("Nature");
            categoryService.addCategory("Animals");
            categoryService.addCategory("Food");
        }
        builder.userDetailsService(userDetailsService(userRepository)).passwordEncoder(passwordEncoder);
    }

    /**
     * Return an istance of our CustomUserDetails.
     *
     * @param userRepository
     * @return UserDetailsService
     */
    private UserDetailsService userDetailsService(final UserRepository userRepository) {
        return username -> new CustomUserDetails(userRepository.findByUsername(username));
    }
}
