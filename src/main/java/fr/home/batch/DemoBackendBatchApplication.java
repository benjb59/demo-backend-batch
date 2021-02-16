package fr.home.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.web.client.RestTemplate;

import fr.home.batch.domain.Questionnaire;

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

	@Override
	public void run(String... args) throws Exception {

		Resource res = resourceLoader.getResource("classpath:questionnaire.json");

		JSONParser parser = new JSONParser();
		JSONObject data=(JSONObject) parser.parse(new BufferedReader(new InputStreamReader(res.getInputStream())));

		Questionnaire questionnaire = new Questionnaire();

		questionnaire.setId("A0000002");
		questionnaire.setData(data);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Questionnaire> request = new HttpEntity<Questionnaire>(questionnaire, headers);

		restTemplate.postForEntity("https://demo-backend-mongo.dev.insee.io/questionnaires", request, String.class);
	}
}
