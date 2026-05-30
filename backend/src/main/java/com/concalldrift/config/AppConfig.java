package com.concalldrift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(15_000);

        RestTemplate rt = new RestTemplate(factory);
        rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/120.0.0.0 Safari/537.36");
            request.getHeaders().set("Accept", "application/json");
            request.getHeaders().set("Accept-Language", "en-US,en;q=0.9");
            return execution.execute(request, body);
        });
        return rt;
    }
}
