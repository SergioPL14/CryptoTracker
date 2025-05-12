package com.cryptotracker.service;

import com.cryptotracker.model.Holding;
import com.cryptotracker.model.Portfolio;
import com.cryptotracker.model.HoldingDetails;
import com.cryptotracker.model.PortfolioSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final ObjectMapper objectMapper;
    private final CoinGeckoApiClient coinGeckoApiClient;
    private final String PORTFOLIO_FILE = "portfolio.json";
    private final Map<String, String> symbolToId = Map.of(
        "BTC", "bitcoin",
        "ETH", "ethereum",
        "USDT", "tether",
        "BNB", "binancecoin",
        "SOL", "solana"
    );

    public PortfolioService(ObjectMapper objectMapper, CoinGeckoApiClient coinGeckoApiClient) {
        this.objectMapper = objectMapper;
        this.coinGeckoApiClient = coinGeckoApiClient;
    }

    public Portfolio getPortfolio() throws IOException {
        ClassPathResource resource = new ClassPathResource(PORTFOLIO_FILE);
        return objectMapper.readValue(resource.getInputStream(), Portfolio.class);
    }

    public void updateHolding(String symbol, double amount) throws IOException {
        Portfolio portfolio = getPortfolio();
        List<Holding> holdings = portfolio.getHoldings();
        
        holdings.stream()
                .filter(h -> h.getSymbol().equals(symbol))
                .findFirst()
                .ifPresentOrElse(
                    h -> h.setAmount(amount),
                    () -> {
                        Holding newHolding = new Holding();
                        newHolding.setSymbol(symbol);
                        newHolding.setAmount(amount);
                        holdings.add(newHolding);
                    }
                );

        objectMapper.writeValue(new ClassPathResource(PORTFOLIO_FILE).getFile(), portfolio);
    }

    public HoldingDetails getHoldingDetails(String symbol) throws IOException {
        Portfolio portfolio = getPortfolio();
        Holding holding = portfolio.getHoldings().stream()
                .filter(h -> h.getSymbol().equals(symbol))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Holding not found: " + symbol));

        Map<String, Map<String, Object>> prices = coinGeckoApiClient.getPricesInCurrencies(
                List.of(symbol), "usd", "eur");
        
        String coinGeckoId = symbolToId.getOrDefault(symbol.toLowerCase(), symbol);
        Map<String, Object> priceData = prices.get(coinGeckoId);
        
        if (priceData == null) {
            throw new RuntimeException("Could not fetch price data for symbol: " + symbol + 
                ". CoinGecko ID used: " + coinGeckoId);
        }
        
        double priceUsd = ((Number) priceData.get("usd")).doubleValue();
        double priceEur = ((Number) priceData.get("eur")).doubleValue();
        double priceChange = ((Number) priceData.getOrDefault("usd_24h_change", 0.0)).doubleValue();

        return HoldingDetails.builder()
                .symbol(symbol)
                .amount(holding.getAmount())
                .priceUsd(priceUsd)
                .priceEur(priceEur)
                .valueUsd(priceUsd * holding.getAmount())
                .valueEur(priceEur * holding.getAmount())
                .priceChange24h(priceChange)
                .build();
    }

    public PortfolioSummary getPortfolioSummary() throws IOException {
        Portfolio portfolio = getPortfolio();
        List<String> symbols = portfolio.getHoldings().stream()
                .map(Holding::getSymbol)
                .collect(Collectors.toList());

        Map<String, Map<String, Object>> prices = coinGeckoApiClient.getPricesInCurrencies(
                symbols, "usd", "eur");

        List<HoldingDetails> holdingDetails = portfolio.getHoldings().stream()
                .map(holding -> {
                    String symbol = holding.getSymbol();
                    String coinGeckoId = symbolToId.getOrDefault(symbol.toUpperCase(), symbol.toLowerCase());
                    Map<String, Object> priceData = prices.get(coinGeckoId);
                    
                    if (priceData == null) {
                        throw new RuntimeException("Could not fetch price data for symbol: " + symbol);
                    }
                    
                    double priceUsd = ((Number) priceData.get("usd")).doubleValue();
                    double priceEur = ((Number) priceData.get("eur")).doubleValue();
                    double priceChange = ((Number) priceData.get("usd_24h_change")).doubleValue();

                    return HoldingDetails.builder()
                            .symbol(symbol)
                            .amount(holding.getAmount())
                            .priceUsd(priceUsd)
                            .priceEur(priceEur)
                            .valueUsd(priceUsd * holding.getAmount())
                            .valueEur(priceEur * holding.getAmount())
                            .priceChange24h(priceChange)
                            .build();
                })
                .collect(Collectors.toList());

        double totalValueUsd = holdingDetails.stream()
                .mapToDouble(HoldingDetails::getValueUsd)
                .sum();
        double totalValueEur = holdingDetails.stream()
                .mapToDouble(HoldingDetails::getValueEur)
                .sum();

        return PortfolioSummary.builder()
                .totalValueUsd(totalValueUsd)
                .totalValueEur(totalValueEur)
                .holdings(holdingDetails)
                .build();
    }
} 