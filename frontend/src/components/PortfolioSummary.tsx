import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '../services/api';
import {
    Card,
    CardContent,
    Typography,
    Grid,
    CircularProgress,
    Alert,
    Box
} from '@mui/material';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts';
import { PortfolioSummary as PortfolioSummaryType, HoldingDetails } from '../types/portfolio';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

interface ChartData {
    name: string;
    value: number;
}

export const PortfolioSummary: React.FC<{ portfolioId: string }> = ({ portfolioId }) => {
    const { data: summary, isLoading, error } = useQuery<PortfolioSummaryType>({
        queryKey: ['portfolioSummary', portfolioId],
        queryFn: () => api.getPortfolioSummary(portfolioId)
    });

    if (isLoading) return <CircularProgress />;
    if (error) return <Alert severity="error">Error loading portfolio</Alert>;
    if (!summary) return null;

    const pieData: ChartData[] = summary.holdings.map((holding: HoldingDetails) => ({
        name: holding.symbol,
        value: holding.valueUsd
    }));

    return (
        <Card>
            <CardContent>
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
                    <Box sx={{ flex: '1 1 300px', minWidth: 0 }}>
                        <Typography variant="h5" gutterBottom>
                            Portfolio Summary
                        </Typography>
                        <Typography variant="h4" color="primary">
                            ${summary?.totalValueUsd.toLocaleString()}
                        </Typography>
                        <Typography variant="subtitle1" color="textSecondary">
                            €{summary?.totalValueEur.toLocaleString()}
                        </Typography>
                    </Box>
                    <Box sx={{ flex: '1 1 300px', minWidth: 0, height: 200 }}>
                        <ResponsiveContainer width="100%" height="100%">
                            <PieChart>
                                <Pie
                                    data={pieData}
                                    dataKey="value"
                                    nameKey="name"
                                    cx="50%"
                                    cy="50%"
                                    outerRadius={80}
                                    label
                                >
                                    {pieData.map((entry, index) => (
                                        <Cell
                                            key={`cell-${index}`}
                                            fill={COLORS[index % COLORS.length]}
                                        />
                                    ))}
                                </Pie>
                                <Tooltip />
                            </PieChart>
                        </ResponsiveContainer>
                    </Box>
                </Box>
            </CardContent>
        </Card>
    );
}; 