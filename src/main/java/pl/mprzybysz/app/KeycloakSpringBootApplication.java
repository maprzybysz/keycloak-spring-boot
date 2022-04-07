package pl.mprzybysz.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)

public class KeycloakSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakSpringBootApplication.class, args);
	}

}
