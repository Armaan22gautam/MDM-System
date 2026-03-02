import React, { useState } from 'react';
import api from '../services/api';
import { Search as SearchIcon, User, MapPin } from 'lucide-react';

const GoldenRecord = () => {
    const [searchParams, setSearchParams] = useState({
        email: '',
        phone: '',
        firstName: '',
        lastName: ''
    });

    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            // Build query params
            const params = new URLSearchParams();
            if (searchParams.email) params.append('email', searchParams.email);
            if (searchParams.phone) params.append('phone', searchParams.phone);
            if (searchParams.firstName) params.append('firstName', searchParams.firstName);
            if (searchParams.lastName) params.append('lastName', searchParams.lastName);

            const res = await api.get(`/golden/search?${params.toString()}`);
            setResults(res.data);
            if (res.data.length === 0) {
                setError('No golden records found matching the criteria.');
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Error occurred while searching.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container">
            <div className="glass-panel" style={{ marginBottom: '2rem' }}>
                <h2 style={{ marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    <SearchIcon size={24} color="var(--accent-purple)" /> Search Golden Records
                </h2>

                <form onSubmit={handleSearch} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', alignItems: 'end' }}>
                    <div className="input-group" style={{ marginBottom: 0 }}>
                        <label className="input-label">Email</label>
                        <input type="email" name="email" className="glass-input" value={searchParams.email} onChange={handleChange} />
                    </div>
                    <div className="input-group" style={{ marginBottom: 0 }}>
                        <label className="input-label">Phone</label>
                        <input type="text" name="phone" className="glass-input" value={searchParams.phone} onChange={handleChange} />
                    </div>
                    <div className="input-group" style={{ marginBottom: 0 }}>
                        <label className="input-label">First Name</label>
                        <input type="text" name="firstName" className="glass-input" value={searchParams.firstName} onChange={handleChange} />
                    </div>
                    <div className="input-group" style={{ marginBottom: 0 }}>
                        <label className="input-label">Last Name</label>
                        <input type="text" name="lastName" className="glass-input" value={searchParams.lastName} onChange={handleChange} />
                    </div>
                    <button type="submit" className="btn btn-primary" style={{ height: '42px' }} disabled={loading}>
                        {loading ? 'Searching...' : 'Search'}
                    </button>
                </form>
            </div>

            {error && <div className="alert alert-warning">{error}</div>}

            {results.length > 0 && (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '1.5rem' }}>
                    {results.map((record) => (
                        <div key={record.id} className="glass-panel" style={{ padding: '1.5rem' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1rem' }}>
                                <div style={{ backgroundColor: 'rgba(59, 130, 246, 0.2)', padding: '0.75rem', borderRadius: '50%' }}>
                                    <User size={24} color="var(--accent-blue)" />
                                </div>
                                <div>
                                    <h3 style={{ margin: 0, fontSize: '1.125rem' }}>{record.firstName} {record.lastName}</h3>
                                    <span className="badge badge-success" style={{ fontSize: '0.65rem' }}>Golden ID #{record.id}</span>
                                </div>
                            </div>

                            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', fontSize: '0.875rem' }}>
                                <p style={{ margin: 0 }}><strong>Email:</strong> {record.email || 'N/A'}</p>
                                <p style={{ margin: 0 }}><strong>Phone:</strong> {record.phoneNumber || 'N/A'}</p>
                                <div style={{ display: 'flex', gap: '0.5rem', marginTop: '0.5rem' }}>
                                    <MapPin size={16} color="var(--text-secondary)" />
                                    <span style={{ color: 'var(--text-secondary)' }}>
                                        {record.street ? `${record.street}, ${record.city}, ${record.country}` : 'No address on file'}
                                    </span>
                                </div>
                                <div style={{ marginTop: '0.5rem', paddingTop: '0.5rem', borderTop: '1px solid var(--glass-border)', fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                                    Version: {record.version}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default GoldenRecord;
