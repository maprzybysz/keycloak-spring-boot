package pl.mprzybysz.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().registerModules(new ProblemModule(), new ConstraintViolationProblemModule());
	}
}
