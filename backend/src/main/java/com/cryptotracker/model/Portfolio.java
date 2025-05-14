package com.cryptotracker.model;

import lombok.Data;
import java.util.List;

@Data
public class Portfolio {
    private String id;
    private String name;
    private String description;
    private List<Holding> holdings;
} 