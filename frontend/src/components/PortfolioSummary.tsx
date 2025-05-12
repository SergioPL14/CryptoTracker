import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '../services/api';
import {
    Card,
    CardContent,
    Typography,
    Grid,
    CircularProgress,
    Alert
} from '@mui/material';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

export const PortfolioSummary: React.FC = () => {
    const { data: summary, isLoading, error } = useQuery(
        ['portfolioSummary'],
        api.getPortfolioSummary
    );

    if (isLoading) return <CircularProgress />;
    if (error) return <Alert severity="error">Error loading portfolio</Alert>;
    if (!summary) return null;

    const pieData = summary.holdings.map(holding => ({
        name: holding.symbol,
        value: holding.valueUsd
    }));

    return (
        <Card>
            <CardContent>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={6}>
                        <Typography variant="h5" gutterBottom>
                            Portfolio Summary
                        </Typography>
                        <Typography variant="h4" color="primary">
                            ${summary.totalValueUsd.toLocaleString()}
                        </Typography>
                        <Typography variant="subtitle1" color="textSecondary">
                            €{summary.totalValueEur.toLocaleString()}
                        </Typography>
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <ResponsiveContainer width="100%" height={200}>
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
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}; 