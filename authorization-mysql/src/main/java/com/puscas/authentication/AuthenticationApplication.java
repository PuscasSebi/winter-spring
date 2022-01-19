package com.puscas.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class AuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}

/*	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder(10).encode("secret"));
	}*/
}
