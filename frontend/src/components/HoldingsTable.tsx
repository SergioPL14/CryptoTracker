import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '../services/api';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    IconButton,
    TextField,
    CircularProgress,
    Alert
} from '@mui/material';
import { Edit as EditIcon, Check as CheckIcon, Close as CloseIcon } from '@mui/icons-material';
import { PortfolioSummary } from '../types/portfolio';

export const HoldingsTable: React.FC = () => {
    const [editingSymbol, setEditingSymbol] = useState<string | null>(null);
    const [editAmount, setEditAmount] = useState<string>('');
    const queryClient = useQueryClient();

    const { data: summary, isLoading, error } = useQuery<PortfolioSummary, Error>({
        queryKey: ['portfolioSummary'],
        queryFn: api.getPortfolioSummary
    });

    const updateMutation = useMutation<void, Error, { symbol: string; amount: number }>({
        mutationFn: ({ symbol, amount }) => api.updateHolding(symbol, amount),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['portfolioSummary'] });
            setEditingSymbol(null);
        }
    });

    if (isLoading) return <CircularProgress />;
    if (error) return <Alert severity="error">Error loading holdings</Alert>;
    if (!summary) return null;

    const handleEdit = (symbol: string, amount: number) => {
        setEditingSymbol(symbol);
        setEditAmount(amount.toString());
    };

    const handleSave = (symbol: string) => {
        const amount = parseFloat(editAmount);
        if (!isNaN(amount)) {
            updateMutation.mutate({ symbol, amount });
        }
    };

    const handleCancel = () => {
        setEditingSymbol(null);
    };

    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Symbol</TableCell>
                        <TableCell align="right">Amount</TableCell>
                        <TableCell align="right">Price (USD)</TableCell>
                        <TableCell align="right">Value (USD)</TableCell>
                        <TableCell align="right">24h Change</TableCell>
                        <TableCell align="right">Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {summary.holdings.map((holding) => (
                        <TableRow key={holding.symbol}>
                            <TableCell>{holding.symbol}</TableCell>
                            <TableCell align="right">
                                {editingSymbol === holding.symbol ? (
                                    <TextField
                                        value={editAmount}
                                        onChange={(e) => setEditAmount(e.target.value)}
                                        type="number"
                                        size="small"
                                    />
                                ) : (
                                    holding.amount
                                )}
                            </TableCell>
                            <TableCell align="right">
                                ${holding.priceUsd.toLocaleString()}
                            </TableCell>
                            <TableCell align="right">
                                ${holding.valueUsd.toLocaleString()}
                            </TableCell>
                            <TableCell
                                align="right"
                                style={{
                                    color: holding.priceChange24h >= 0 ? 'green' : 'red'
                                }}
                            >
                                {holding.priceChange24h.toFixed(2)}%
                            </TableCell>
                            <TableCell align="right">
                                {editingSymbol === holding.symbol ? (
                                    <>
                                        <IconButton
                                            onClick={() => handleSave(holding.symbol)}
                                            color="primary"
                                            size="small"
                                        >
                                            <CheckIcon />
                                        </IconButton>
                                        <IconButton
                                            onClick={handleCancel}
                                            color="secondary"
                                            size="small"
                                        >
                                            <CloseIcon />
                                        </IconButton>
                                    </>
                                ) : (
                                    <IconButton
                                        onClick={() => handleEdit(holding.symbol, holding.amount)}
                                        size="small"
                                    >
                                        <EditIcon />
                                    </IconButton>
                                )}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}; 