package com.hyde.wmis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WmisApplication {

	public static void main(String[] args) {
		SpringApplication.run(WmisApplication.class, args);
	}

}
