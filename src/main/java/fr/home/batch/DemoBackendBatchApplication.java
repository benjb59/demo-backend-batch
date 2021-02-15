package fr.home.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoBackendBatchApplication {

	private static final Logger log = LoggerFactory.getLogger(DemoBackendBatchApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoBackendBatchApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate, ResourceLoader resourceLoader) throws Exception {
		return args -> {
			Resource res = resourceLoader.getResource("classpath:questionnaire.json");
			String questionnaire = new BufferedReader(new InputStreamReader(res.getInputStream()))
				.lines()
					.reduce("", String::concat);

			restTemplate.postForEntity("https://demo-backend-mongo.dev.insee.io/developers", questionnaire, String.class);
		};
	}
}
