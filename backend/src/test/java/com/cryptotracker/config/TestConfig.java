package com.cryptotracker.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.mockito.Mockito;
import com.cryptotracker.service.CoinGeckoApiClient;

@TestConfiguration
public class TestConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CoinGeckoApiClient coinGeckoApiClient(RestTemplate restTemplate) {
        return new CoinGeckoApiClient(restTemplate, "https://api.coingecko.com/api/v3");
    }
} 