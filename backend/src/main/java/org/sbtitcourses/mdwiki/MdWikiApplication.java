package org.sbtitcourses.mdwiki;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.PersonService;
import org.sbtitcourses.mdwiki.service.RegistrationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Запуск wiki-сервиса
 */
@SpringBootApplication
public class MdWikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdWikiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	CommandLineRunner run(RegistrationService registrationService) {
		return args -> {
			registrationService.register(new Person("admin", "adminadmin", "admin", "admin@mail.com"));
		};
	}
}
