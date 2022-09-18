package com.github.coding_team_sept.nd_backend.appointment.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DoctorConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
