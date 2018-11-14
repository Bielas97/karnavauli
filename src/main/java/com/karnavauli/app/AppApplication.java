package com.karnavauli.app;

import com.karnavauli.app.model.dto.UserDto;
import com.karnavauli.app.model.entities.User;
import com.karnavauli.app.model.enums.Role;
import com.karnavauli.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AppApplication implements CommandLineRunner{
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public AppApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.findAll().stream().noneMatch(user -> user.getUsername().equals("bielas"))) {
			userRepository.save(User.builder().numberOfTickets(9999).password(passwordEncoder.encode("kvAdmin")).role(Role.CEO).username("bielas").build());
		}
	}
}
