export interface Holding {
    symbol: string;
    amount: number;
}

export interface HoldingDetails extends Holding {
    priceUsd: number;
    priceEur: number;
    valueUsd: number;
    valueEur: number;
    priceChange24h: number;
}

export interface PortfolioSummary {
    totalValueUsd: number;
    totalValueEur: number;
    holdings: HoldingDetails[];
}

export interface Portfolio {
    id: string;
    name: string;
    description: string;
    holdings: Holding[];
} 