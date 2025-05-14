import axios from 'axios';
import { Portfolio, PortfolioSummary, HoldingDetails } from '../types/portfolio';

const API_BASE_URL = 'http://localhost:8080/api';

export const api = {
    getPortfolio: async (): Promise<Portfolio> => {
        const response = await axios.get(`${API_BASE_URL}/portfolio`);
        return response.data;
    },

    getPortfolioSummary: async (portfolioId: string): Promise<PortfolioSummary> => {
        const response = await axios.get(`${API_BASE_URL}/portfolios/${portfolioId}/summary`);
        return response.data;
    },

    getHoldingDetails: async (symbol: string): Promise<HoldingDetails> => {
        const response = await axios.get(`${API_BASE_URL}/portfolio/holdings/${symbol}`);
        return response.data;
    },

    updateHolding: async (portfolioId: string, symbol: string, amount: number): Promise<HoldingDetails> => {
        const response = await axios.put(
            `${API_BASE_URL}/portfolios/${portfolioId}/holdings/${symbol}?amount=${amount}`
        );
        return response.data;
    },

    getSupportedSymbols: async (): Promise<string[]> => {
        const response = await axios.get(`${API_BASE_URL}/portfolios/symbols/supported`);
        return response.data;
    },

    validateSymbol: async (symbol: string): Promise<boolean> => {
        const response = await axios.get(`${API_BASE_URL}/portfolios/symbols/validate/${symbol}`);
        return response.data;
    }
}; 