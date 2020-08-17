package com.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    @Configuration
    @ConfigurationProperties("com.example")
    public static class HelloConfiguration {

        String helloService;

        public String getHelloService() {
            return helloService;
        }

        public void setHelloService(String helloService) {
            this.helloService = helloService;
        }

    }

    @RestController
    public static class HelloController {

        private final RestTemplate helloService;

        public HelloController(
                RestTemplateBuilder restTemplateBuilder,
                HelloConfiguration configuration) {
            this.helloService = restTemplateBuilder
                    .rootUri(configuration.getHelloService())
                    .build();
        }

        @RequestMapping("/local")
        public String local() {
            return "Greetings from Local!";
        }

        @RequestMapping("/remote")
        public String remote() {
            return helloService.getForObject("/", String.class);
        }

    }

}
