import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { AddHoldingForm } from './AddHoldingForm';
import { useQuery, useQueryClient } from 'react-query';
import { api } from '../services/api';

export const PortfolioDetail: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const queryClient = useQueryClient();
    const [showAddForm, setShowAddForm] = useState(false);

    const { data: summary, refetch: fetchSummary } = useQuery({
        queryKey: ['portfolioSummary', id],
        queryFn: () => api.getPortfolioSummary(id!)
    });

    const handleHoldingAdded = async () => {
        setShowAddForm(false);
        await fetchSummary();
    };

    return (
        <div className="container mx-auto p-4">
            <div className="mt-6 flex justify-end">
                <button
                    onClick={() => setShowAddForm(!showAddForm)}
                    className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
                >
                    {showAddForm ? 'Cancel' : 'Add New Holding'}
                </button>
            </div>

            {showAddForm && (
                <div className="mt-4">
                    <AddHoldingForm
                        portfolioId={id!}
                        onHoldingAdded={handleHoldingAdded}
                    />
                </div>
            )}
        </div>
    );
}; 