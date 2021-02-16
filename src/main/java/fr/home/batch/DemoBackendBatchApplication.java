package fr.home.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import fr.home.batch.entities.Questionnaire;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoBackendBatchApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(DemoBackendBatchApplication.class);

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private RestTemplate restTemplate;


	public static void main(String[] args) {
		SpringApplication.run(DemoBackendBatchApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Override
	public void run(String... args) throws Exception {

		Resource res = resourceLoader.getResource("classpath:questionnaire.json");
		String questionnaireString = new BufferedReader(new InputStreamReader(res.getInputStream()))
				.lines()
				.reduce("", String::concat);

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setData(new JSONObject(questionnaireString));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Questionnaire> request = new HttpEntity<Questionnaire>(questionnaire, headers);

		restTemplate.postForEntity("https://demo-backend-mongo.dev.insee.io/developers", request, String.class);
	}
}
