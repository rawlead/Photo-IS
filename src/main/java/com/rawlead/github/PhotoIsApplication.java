package com.rawlead.github;

import com.rawlead.github.configuration.CustomUserDetails;
import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.UserRepository;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class PhotoIsApplication {

	private PasswordEncoder passwordEncoder;


	@Autowired
	public PhotoIsApplication(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
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
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository userRepository, UserService service) throws Exception {
		if (userRepository.count() == 0) {
			service.save(new User("user@mail.com","user", passwordEncoder.encode("password"), Arrays.asList(new Role("USER"), new Role("PHOTOGRAPHER")), "https://s3.eu-central-1.amazonaws.com/photois-uploads-bucket/1519303180823-avatar.jpg"));
			service.save(new User("usertwo@mail.co,","usertwo", passwordEncoder.encode("password"), Arrays.asList(new Role("USER"), new Role("PHOTOGRAPHER")), ""));
			service.save(new User("admin@mail.com","admin", passwordEncoder.encode("password"), Arrays.asList(new Role("USER"), new Role("ADMIN")), ""));
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
