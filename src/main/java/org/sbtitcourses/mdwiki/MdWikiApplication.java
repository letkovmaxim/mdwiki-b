package org.sbtitcourses.mdwiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Запуск wiki-сервиса
 */
@SpringBootApplication
public class MdWikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdWikiApplication.class, args);
	}

}
