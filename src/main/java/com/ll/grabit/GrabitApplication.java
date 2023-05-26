package com.ll.grabit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GrabitApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrabitApplication.class, args);
	}

}
