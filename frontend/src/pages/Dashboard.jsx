import React from 'react';
import { Users, Database, ShieldAlert, Activity } from 'lucide-react';

const StatCard = ({ title, value, icon, color }) => (
    <div className="glass-panel" style={{ display: 'flex', alignItems: 'center', gap: '1.5rem', padding: '1.5rem' }}>
        <div style={{ backgroundColor: `${color}20`, color: color, padding: '1rem', borderRadius: '12px' }}>
            {icon}
        </div>
        <div>
            <h3 style={{ margin: 0, fontSize: '2rem' }}>{value}</h3>
            <p style={{ margin: 0, fontSize: '0.875rem' }}>{title}</p>
        </div>
    </div>
);

const Dashboard = () => {
    return (
        <div className="container" style={{ animation: 'fadeIn 0.5s ease-out' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2.5rem' }}>
                <div>
                    <h2 style={{ fontSize: '2.5rem', fontWeight: '700', margin: 0, background: 'linear-gradient(to right, var(--text-primary), var(--accent-blue))', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>Platform Dashboard</h2>
                    <p style={{ marginTop: '0.5rem', fontSize: '1.1rem' }}>Unified Customer Intelligence Overview</p>
                </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '2rem', marginBottom: '3rem' }}>
                <StatCard title="Total Raw Records" value="1,248" icon={<Database size={32} />} color="var(--accent-blue)" />
                <StatCard title="Golden Customers" value="842" icon={<Users size={32} />} color="var(--status-success)" />
                <StatCard title="Pending Resolution" value="14" icon={<ShieldAlert size={32} />} color="var(--status-warning)" />
                <StatCard title="Auto-Merged (24h)" value="56" icon={<Activity size={32} />} color="var(--accent-purple)" />
            </div>

            <div className="glass-panel">
                <h3>Recent System Activity</h3>
                <table className="glass-table" style={{ marginTop: '1.5rem' }}>
                    <thead>
                        <tr>
                            <th>Time</th>
                            <th>Action</th>
                            <th>Entity ID</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>10 mins ago</td>
                            <td>Ingested from SALESFORCE</td>
                            <td>SF-9912</td>
                            <td><span className="badge badge-warning">PENDING</span></td>
                        </tr>
                        <tr>
                            <td>25 mins ago</td>
                            <td>Golden Record Promotion</td>
                            <td>1054</td>
                            <td><span className="badge badge-success">PROMOTED</span></td>
                        </tr>
                        <tr>
                            <td>1 hr ago</td>
                            <td>Deduplication Merge (Score: 92%)</td>
                            <td>1053</td>
                            <td><span className="badge badge-success">MERGED</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Dashboard;
