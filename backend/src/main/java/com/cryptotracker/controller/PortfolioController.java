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
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "http://localhost:3000") // For development
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final CryptoPriceService cryptoPriceService;

    public PortfolioController(PortfolioService portfolioService, CryptoPriceService cryptoPriceService) {
        this.portfolioService = portfolioService;
        this.cryptoPriceService = cryptoPriceService;
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getAllPortfolios() throws IOException {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(portfolioService.getPortfolioById(id));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<PortfolioSummary> getPortfolioSummary(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(portfolioService.getPortfolioSummary(id));
    }

    @PutMapping("/{id}/holdings/{symbol}")
    public ResponseEntity<HoldingDetails> updateHolding(
            @PathVariable String id,
            @PathVariable String symbol,
            @RequestParam double amount) throws IOException {
        portfolioService.updateHolding(id, symbol, amount);
        Portfolio portfolio = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok(portfolioService.getHoldingDetails(id, symbol));
    }

    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) throws IOException {
        portfolioService.createPortfolio(portfolio);
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolio);
    }

    @GetMapping("/prices")
    public ResponseEntity<List<CryptoPrice>> getPrices() throws IOException {
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        List<String> symbols = portfolios.stream()
                .flatMap(p -> p.getHoldings().stream())
                .map(Holding::getSymbol)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(cryptoPriceService.getPrices(symbols));
    }

    @GetMapping("/{portfolioId}/holdings/{symbol}")
    public ResponseEntity<HoldingDetails> getHoldingDetails(
            @PathVariable String portfolioId,
            @PathVariable String symbol) throws IOException {
        return ResponseEntity.ok(portfolioService.getHoldingDetails(portfolioId, symbol));
    }

    @GetMapping("/symbols/supported")
    public ResponseEntity<List<String>> getSupportedSymbols() {
        return ResponseEntity.ok(portfolioService.getSupportedSymbols());
    }

    @GetMapping("/symbols/validate/{symbol}")
    public ResponseEntity<Boolean> validateSymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(portfolioService.isValidSymbol(symbol));
    }
} 