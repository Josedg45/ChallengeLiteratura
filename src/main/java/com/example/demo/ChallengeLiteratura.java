package com.example.demo;

import com.example.demo.principal.principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Principal;

@SpringBootApplication
public class ChallengeLiteratura implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteratura.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		principal principal = new principal();
		principal.muestraElMenu();
	}
}
