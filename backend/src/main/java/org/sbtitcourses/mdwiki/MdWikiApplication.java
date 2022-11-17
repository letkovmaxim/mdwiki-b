package org.sbtitcourses.mdwiki;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Запуск wiki-сервиса
 */
@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class MdWikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdWikiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
