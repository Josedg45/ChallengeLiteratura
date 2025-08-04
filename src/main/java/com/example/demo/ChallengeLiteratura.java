package com.example.demo;

import com.example.demo.principal.principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteratura implements CommandLineRunner {

	@Autowired
	private principal principal; // ✅ Ahora lo inyecta Spring

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteratura.class, args);
	}

	@Override
	public void run(String... args) {
		principal.muestraElMenu();
	}
}

