package com.itwillbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QTableApplication {

	public static void main(String[] args) {
		SpringApplication.run(QTableApplication.class, args);
	}

}
