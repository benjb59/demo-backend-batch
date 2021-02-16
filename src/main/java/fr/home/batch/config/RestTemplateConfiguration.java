package fr.home.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;


@Configuration
public class RestTemplateConfiguration {
    
    @Value("${proxy.host:#{null}}")
	private String proxyHost;

    @Value("${proxy.port:0}")
    private int proxyPort;

    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
        System.out.println(proxyHost);
        System.out.println(proxyPort);
		if (proxyHost!=null && proxyPort!=0) {
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setProxy(proxy);
			return new RestTemplate(requestFactory);
		}else{
			return builder.build();
		}
	}
    
}
