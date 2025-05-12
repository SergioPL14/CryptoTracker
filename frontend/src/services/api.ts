import axios from 'axios';
import { Portfolio, PortfolioSummary, HoldingDetails } from '../types/portfolio';

const API_BASE_URL = 'http://localhost:8080/api';

export const api = {
    getPortfolio: async (): Promise<Portfolio> => {
        const response = await axios.get(`${API_BASE_URL}/portfolio`);
        return response.data;
    },

    getPortfolioSummary: async (): Promise<PortfolioSummary> => {
        const response = await axios.get(`${API_BASE_URL}/portfolio/summary`);
        return response.data;
    },

    getHoldingDetails: async (symbol: string): Promise<HoldingDetails> => {
        const response = await axios.get(`${API_BASE_URL}/portfolio/holdings/${symbol}`);
        return response.data;
    },

    updateHolding: async (symbol: string, amount: number): Promise<void> => {
        await axios.put(`${API_BASE_URL}/portfolio/${symbol}?amount=${amount}`);
    }
}; 