package com.cryptotracker.model;

import lombok.Data;

@Data
public class CryptoPrice {
    private String symbol;
    private double price;
    private double marketCap;
    private double volume24h;
    private double priceChange24h;
} 