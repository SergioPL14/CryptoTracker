package com.cryptotracker.service.coingecko;

import com.cryptotracker.model.CryptoPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.cryptotracker.service.CoinGeckoApiClient;

class CoinGeckoApiClientTest {
    private CoinGeckoApiClient coinGeckoApiClient;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        coinGeckoApiClient = new CoinGeckoApiClient(restTemplate, "https://api.coingecko.com/api/v3");
    }

    @Test
    void getPrices_ShouldReturnCorrectPrices() {
        // Mock response data
        Map<String, Map<String, Object>> mockResponse = Map.of(
            "bitcoin", Map.of(
                "usd", 50000.0,
                "usd_market_cap", 1000000000000.0,
                "usd_24h_vol", 50000000000.0,
                "usd_24h_change", 2.5
            ),
            "ethereum", Map.of(
                "usd", 3000.0,
                "usd_market_cap", 350000000000.0,
                "usd_24h_vol", 20000000000.0,
                "usd_24h_change", 1.5
            )
        );

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(mockResponse);

        List<CryptoPrice> prices = coinGeckoApiClient.getPrices(List.of("BTC", "ETH"));

        assertNotNull(prices);
        assertEquals(2, prices.size());

        // Verify Bitcoin data
        CryptoPrice btcPrice = prices.stream()
            .filter(p -> p.getSymbol().equals("BTC"))
            .findFirst()
            .orElseThrow();
        assertEquals(50000.0, btcPrice.getPrice());
        assertEquals(1000000000000.0, btcPrice.getMarketCap());
        assertEquals(50000000000.0, btcPrice.getVolume24h());
        assertEquals(2.5, btcPrice.getPriceChange24h());

        // Verify Ethereum data
        CryptoPrice ethPrice = prices.stream()
            .filter(p -> p.getSymbol().equals("ETH"))
            .findFirst()
            .orElseThrow();
        assertEquals(3000.0, ethPrice.getPrice());
        assertEquals(350000000000.0, ethPrice.getMarketCap());
        assertEquals(20000000000.0, ethPrice.getVolume24h());
        assertEquals(1.5, ethPrice.getPriceChange24h());
    }

    @Test
    void getPrices_ShouldHandleUnknownSymbols() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(Map.of());

        List<CryptoPrice> prices = coinGeckoApiClient.getPrices(List.of("UNKNOWN"));

        assertNotNull(prices);
        assertTrue(prices.isEmpty());
    }
} 