package com.puscas.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootApplication

public class BCrypt {


	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder(10).encode("puscas"));
	}
}
