package com.cryptotracker.model;

import lombok.Data;
import java.util.List;

@Data
public class Portfolio {
    private List<Holding> holdings;
} 