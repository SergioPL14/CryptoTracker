package com.cryptotracker.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class HoldingDetails {
    private String symbol;
    private double amount;
    private double valueUsd;
    private double valueEur;
    private double priceUsd;
    private double priceEur;
    private double priceChange24h;
} 