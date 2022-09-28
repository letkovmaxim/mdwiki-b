package org.sbtitcourses.mdwiki;

/**
 * Запуск wiki-сервиса
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MdWikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdWikiApplication.class, args);
	}

}
