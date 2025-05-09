package com.cryptotracker.model;

import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class PortfolioSummary {
    private double totalValueUsd;
    private double totalValueEur;
    private List<HoldingDetails> holdings;
} 