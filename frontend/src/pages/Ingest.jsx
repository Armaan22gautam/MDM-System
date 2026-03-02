import React, { useState } from 'react';
import api from '../services/api';
import { Save, RefreshCw } from 'lucide-react';

const Ingest = () => {
    const [formData, setFormData] = useState({
        sourceSystem: 'SALESFORCE',
        sourceRecordId: '',
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        street: '',
        city: '',
        state: '',
        zipCode: '',
        country: 'USA'
    });

    const [status, setStatus] = useState({ type: '', message: '' });
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setStatus({ type: '', message: '' });

        try {
            const res = await api.post('/customers', formData);
            setStatus({ type: 'success', message: `Record ingested successfully! Ingestion ID: ${res.data.ingestionId}` });
            // Reset form
            setFormData({ ...formData, sourceRecordId: '', firstName: '', lastName: '', email: '', phoneNumber: '', street: '', city: '', state: '', zipCode: '' });
        } catch (err) {
            setStatus({ type: 'error', message: err.response?.data?.message || 'Failed to ingest record.' });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container">
            <div className="glass-panel" style={{ maxWidth: '800px', margin: '0 auto' }}>
                <h2 style={{ marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    <RefreshCw size={24} color="var(--accent-blue)" /> Ingest Raw Customer Record
                </h2>

                {status.message && (
                    <div className={`alert ${status.type === 'error' ? 'alert-error' : 'alert-success'}`}>
                        {status.message}
                    </div>
                )}

                <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>

                    <div className="input-group">
                        <label className="input-label">Source System</label>
                        <select name="sourceSystem" className="glass-input" value={formData.sourceSystem} onChange={handleChange}>
                            <option value="SALESFORCE">Salesforce CRM</option>
                            <option value="SHOPIFY">Shopify eCom</option>
                            <option value="HUBSPOT">HubSpot</option>
                        </select>
                    </div>

                    <div className="input-group">
                        <label className="input-label">Source Record ID *</label>
                        <input type="text" name="sourceRecordId" placeholder="e.g. SF-12903" className="glass-input" required value={formData.sourceRecordId} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">First Name</label>
                        <input type="text" name="firstName" className="glass-input" value={formData.firstName} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">Last Name</label>
                        <input type="text" name="lastName" className="glass-input" value={formData.lastName} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">Email Address</label>
                        <input type="email" name="email" className="glass-input" value={formData.email} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">Phone Number</label>
                        <input type="tel" name="phoneNumber" placeholder="+1234567890" className="glass-input" value={formData.phoneNumber} onChange={handleChange} />
                    </div>

                    <div className="input-group" style={{ gridColumn: '1 / -1' }}>
                        <label className="input-label">Street Address</label>
                        <input type="text" name="street" className="glass-input" value={formData.street} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">City</label>
                        <input type="text" name="city" className="glass-input" value={formData.city} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">State / Province</label>
                        <input type="text" name="state" className="glass-input" value={formData.state} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">Zip Code</label>
                        <input type="text" name="zipCode" className="glass-input" value={formData.zipCode} onChange={handleChange} />
                    </div>

                    <div className="input-group">
                        <label className="input-label">Country</label>
                        <input type="text" name="country" className="glass-input" value={formData.country} onChange={handleChange} />
                    </div>

                    <div style={{ gridColumn: '1 / -1', marginTop: '1rem', display: 'flex', justifyContent: 'flex-end' }}>
                        <button type="submit" className="btn btn-primary" disabled={loading}>
                            {loading ? 'Submitting...' : <><Save size={18} /> Ingest Record</>}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Ingest;
