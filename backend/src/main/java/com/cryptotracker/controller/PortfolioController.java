package com.cryptotracker.controller;

import com.cryptotracker.model.CryptoPrice;
import com.cryptotracker.model.Holding;
import com.cryptotracker.model.Portfolio;
import com.cryptotracker.model.HoldingDetails;
import com.cryptotracker.model.PortfolioSummary;
import com.cryptotracker.service.CryptoPriceService;
import com.cryptotracker.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:3000") // For development
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final CryptoPriceService cryptoPriceService;

    public PortfolioController(PortfolioService portfolioService, CryptoPriceService cryptoPriceService) {
        this.portfolioService = portfolioService;
        this.cryptoPriceService = cryptoPriceService;
    }

    @GetMapping
    public ResponseEntity<Portfolio> getPortfolio() throws IOException {
        return ResponseEntity.ok(portfolioService.getPortfolio());
    }

    @PutMapping("/{symbol}")
    public ResponseEntity<Void> updateHolding(
            @PathVariable String symbol,
            @RequestParam double amount) throws IOException {
        portfolioService.updateHolding(symbol, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/prices")
    public ResponseEntity<List<CryptoPrice>> getPrices() throws IOException {
        Portfolio portfolio = portfolioService.getPortfolio();
        List<String> symbols = portfolio.getHoldings().stream()
                .map(Holding::getSymbol)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cryptoPriceService.getPrices(symbols));
    }

    @GetMapping("/holdings/{symbol}")
    public ResponseEntity<HoldingDetails> getHoldingDetails(@PathVariable String symbol) throws IOException {
        return ResponseEntity.ok(portfolioService.getHoldingDetails(symbol));
    }

    @GetMapping("/summary")
    public ResponseEntity<PortfolioSummary> getPortfolioSummary() throws IOException {
        return ResponseEntity.ok(portfolioService.getPortfolioSummary());
    }
} 