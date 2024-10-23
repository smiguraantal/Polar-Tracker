package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class AppConfig {

    @Value("${polar.access.token}")
    private String polarAccessToken;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateHeaderModifier(polarAccessToken)));
        return restTemplate;
    }

    @Bean
    public String getPolarAccessToken() {
        return polarAccessToken;
    }

    private static class RestTemplateHeaderModifier implements ClientHttpRequestInterceptor {

        private final String token;

        public RestTemplateHeaderModifier(String token) {
            this.token = token;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request,
                                            byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            request.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");
            return execution.execute(request, body);
        }
    }
}