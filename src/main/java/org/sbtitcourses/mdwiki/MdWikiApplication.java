package org.sbtitcourses.mdwiki;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Главный класс с точкой входа приложения.
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
}
