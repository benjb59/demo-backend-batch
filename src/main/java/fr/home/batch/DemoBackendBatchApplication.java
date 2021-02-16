package fr.home.batch;

import fr.home.batch.domain.Questionnaire;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class DemoBackendBatchApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoBackendBatchApplication.class);

    private final long NB_QUESTIONNAIRES = 1000;

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
        JSONObject data = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(res.getInputStream())));

        for (int i = 1; i <= NB_QUESTIONNAIRES; i++) {

            Questionnaire questionnaire = new Questionnaire();

            questionnaire.setId(String.format("%08d", i));
            questionnaire.setData(data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Questionnaire> request = new HttpEntity<Questionnaire>(questionnaire, headers);

            log.info("Chargement questionnaire " + questionnaire.getId());

            restTemplate.postForEntity("https://demo-backend-mongo.dev.insee.io/questionnaires", request, String.class);

        }
    }
}
