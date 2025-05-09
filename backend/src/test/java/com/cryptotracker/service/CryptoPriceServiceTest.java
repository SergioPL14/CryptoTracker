package com.cryptotracker.service;

import com.cryptotracker.model.CryptoPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class CryptoPriceServiceTest {
    @Mock
    private CoinGeckoApiClient coinGeckoApiClient;

    private CryptoPriceService cryptoPriceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cryptoPriceService = new CryptoPriceService(coinGeckoApiClient);
    }

    @Test
    void getPrices_ShouldReturnPricesFromApiClient() {
        List<CryptoPrice> mockPrices = List.of(
            createMockPrice("BTC", 50000.0),
            createMockPrice("ETH", 3000.0)
        );

        when(coinGeckoApiClient.getPrices(anyList()))
            .thenReturn(mockPrices);

        List<CryptoPrice> prices = cryptoPriceService.getPrices(List.of("BTC", "ETH"));

        assertNotNull(prices);
        assertEquals(2, prices.size());
        assertEquals("BTC", prices.get(0).getSymbol());
        assertEquals("ETH", prices.get(1).getSymbol());
    }

    private CryptoPrice createMockPrice(String symbol, double price) {
        CryptoPrice cryptoPrice = new CryptoPrice();
        cryptoPrice.setSymbol(symbol);
        cryptoPrice.setPrice(price);
        cryptoPrice.setMarketCap(price * 1000000);
        cryptoPrice.setVolume24h(price * 100000);
        cryptoPrice.setPriceChange24h(2.5);
        return cryptoPrice;
    }
} 