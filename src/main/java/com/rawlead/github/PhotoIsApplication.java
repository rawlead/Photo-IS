package com.rawlead.github;

import com.rawlead.github.configuration.CustomUserDetails;
import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.UserRepository;
import com.rawlead.github.service.impl.PhotoCategoryServiceImpl;
import com.rawlead.github.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
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

	public static void main(String[] args) {
		SpringApplication.run(PhotoIsApplication.class, args);
	}
	/**
	 * Password grants are switched on by injecting on AuthenticationManagaer
	 * Here, we setup the builder so that the userDetailsService is the one we coded
	 * @param builder
	 * @param userRepository
	 * @throws Exception
	 * */
	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository userRepository, UserServiceImpl service) throws Exception {
		if (userRepository.count() == 0) {
			service.save(new User("NameUser","LastNameUser","user@mail.com","user", passwordEncoder.encode("password"), Stream.of(new Role("USER"), new Role("PHOTOGRAPHER")).collect(Collectors.toSet()), "/img/user-icon-white.png"));
			service.save(new User("NameUserTwo","","usertwo@mail.co,","usertwo", passwordEncoder.encode("password"), Stream.of(new Role("USER"), new Role("PHOTOGRAPHER")).collect(Collectors.toSet()), "/img/user-icon-white.png"));
			service.save(new User("NameAdmin","LastNameAdmin","admin@mail.com","admin", passwordEncoder.encode("password"), Stream.of(new Role("USER"), new Role("ADMIN")).collect(Collectors.toSet()), "/img/user-icon-white.png"));


			categoryService.addCategory("Nature");
			categoryService.addCategory("Animals");
			categoryService.addCategory("Food");
		}
		builder.userDetailsService(userDetailsService(userRepository)).passwordEncoder(passwordEncoder);

	}
	/**
	 * We return an istance of our CustomUserDetails.
	 * @param repository
	 * @return
	 */
	private UserDetailsService userDetailsService(final UserRepository repository) {

		return username -> new CustomUserDetails(repository.findByUsername(username));
	}
}
