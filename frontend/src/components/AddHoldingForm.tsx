import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

interface AddHoldingFormProps {
    portfolioId: string;
    onHoldingAdded: () => void;
}

export const AddHoldingForm: React.FC<AddHoldingFormProps> = ({ portfolioId, onHoldingAdded }) => {
    const [symbol, setSymbol] = useState('');
    const [amount, setAmount] = useState('');
    const [supportedSymbols, setSupportedSymbols] = useState<string[]>([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        loadSupportedSymbols();
    }, []);

    const loadSupportedSymbols = async () => {
        try {
            const symbols = await api.getSupportedSymbols();
            setSupportedSymbols(symbols);
        } catch (err) {
            setError('Failed to load supported symbols');
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const isValid = await api.validateSymbol(symbol.toUpperCase());
            if (!isValid) {
                setError('Invalid cryptocurrency symbol');
                return;
            }

            await api.updateHolding(portfolioId, symbol.toUpperCase(), parseFloat(amount));
            setSymbol('');
            setAmount('');
            onHoldingAdded();
        } catch (err) {
            setError('Failed to add holding');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Add New Holding</h2>
            {error && (
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                    {error}
                </div>
            )}
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Cryptocurrency Symbol
                    </label>
                    <select
                        value={symbol}
                        onChange={(e) => setSymbol(e.target.value)}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                        required
                    >
                        <option value="">Select a cryptocurrency</option>
                        {supportedSymbols.map(sym => (
                            <option key={sym} value={sym}>{sym}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">
                        Amount
                    </label>
                    <input
                        type="number"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        step="any"
                        min="0"
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                        required
                    />
                </div>
                <button
                    type="submit"
                    disabled={loading}
                    className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
                >
                    {loading ? 'Adding...' : 'Add Holding'}
                </button>
            </form>
        </div>
    );
}; 