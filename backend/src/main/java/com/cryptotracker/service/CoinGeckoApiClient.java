package com.cryptotracker.service;

import com.cryptotracker.model.CryptoPrice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoinGeckoApiClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final Map<String, String> symbolToId;

    public CoinGeckoApiClient(
            RestTemplate restTemplate,
            @Value("${coingecko.api.url:https://api.coingecko.com/api/v3}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        // Map of common crypto symbols to CoinGecko IDs
        this.symbolToId = Map.of(
            "BTC", "bitcoin",
            "ETH", "ethereum",
            "USDT", "tether",
            "BNB", "binancecoin",
            "SOL", "solana"
            // Add more mappings as needed
        );
    }

    public List<CryptoPrice> getPrices(List<String> symbols) {
        String ids = symbols.stream()
                .map(symbol -> symbolToId.getOrDefault(symbol.toUpperCase(), symbol.toLowerCase()))
                .collect(java.util.stream.Collectors.joining(","));

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/simple/price")
                .queryParam("ids", ids)
                .queryParam("vs_currencies", "usd")
                .queryParam("include_market_cap", "true")
                .queryParam("include_24hr_vol", "true")
                .queryParam("include_24hr_change", "true")
                .build()
                .toString();

        Map<String, Map<String, Object>> response = restTemplate.getForObject(url, Map.class);
        
        return symbols.stream()
                .map(symbol -> {
                    String id = symbolToId.getOrDefault(symbol.toUpperCase(), symbol.toLowerCase());
                    Map<String, Object> data = response.get(id);
                    if (data == null) {
                        return null;
                    }

                    CryptoPrice price = new CryptoPrice();
                    price.setSymbol(symbol);
                    price.setPrice(((Number) data.get("usd")).doubleValue());
                    price.setMarketCap(((Number) data.get("usd_market_cap")).doubleValue());
                    price.setVolume24h(((Number) data.get("usd_24h_vol")).doubleValue());
                    price.setPriceChange24h(((Number) data.get("usd_24h_change")).doubleValue());
                    return price;
                })
                .filter(price -> price != null)
                .collect(java.util.stream.Collectors.toList());
    }

    public Map<String, Map<String, Object>> getPricesInCurrencies(List<String> symbols, String... currencies) {
        String ids = symbols.stream()
                .map(symbol -> symbolToId.getOrDefault(symbol.toUpperCase(), symbol.toLowerCase()))
                .collect(Collectors.joining(","));

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/simple/price")
                .queryParam("ids", ids)
                .queryParam("vs_currencies", String.join(",", currencies))
                .queryParam("include_24hr_change", true)
                .build()
                .toString();

        return restTemplate.getForObject(url, Map.class);
    }
}