package com.molardev.deckbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication()
public class DeckboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeckboxApplication.class, args);
	}

	@RestController
	class HelloController {
		@GetMapping("/hello")
		public String hello() {
			return "Hello, Deckbox!";
		}
	}
}
