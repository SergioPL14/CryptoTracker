package com.cryptotracker.service;

import com.cryptotracker.model.CryptoPrice;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CryptoPriceService {
    private final CoinGeckoApiClient coinGeckoApiClient;

    public CryptoPriceService(CoinGeckoApiClient coinGeckoApiClient) {
        this.coinGeckoApiClient = coinGeckoApiClient;
    }

    public List<CryptoPrice> getPrices(List<String> symbols) {
        return coinGeckoApiClient.getPrices(symbols);
    }
} 