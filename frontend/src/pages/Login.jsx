import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { LogIn, UserPlus, Database } from 'lucide-react';
import './Login.css';

const Login = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            if (isLogin) {
                const res = await api.post('/auth/login', { username, password });
                login(res.data.token, res.data.role, username);
                navigate('/dashboard');
            } else {
                await api.post('/auth/register', { username, password });
                setIsLogin(true);
                setError('Registration successful! Please log in.');
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Authentication failed. Please check credentials or API gateway.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-container">
            <div className="login-panel glass-panel">
                <div className="login-header">
                    <div className="logo-icon">
                        <Database size={32} color="var(--accent-blue)" />
                    </div>
                    <h2>CDUP<br /><span>MDM Platform</span></h2>
                    <p>{isLogin ? 'Welcome back! Log in to your account.' : 'Create an account to start managing golden records.'}</p>
                </div>

                <form onSubmit={handleSubmit} className="login-form">
                    {error && <div className={error.includes('successful') ? 'alert alert-success' : 'alert alert-error'}>{error}</div>}

                    <div className="input-group">
                        <label className="input-label">Username</label>
                        <input
                            type="text"
                            placeholder="e.g. admin"
                            className="glass-input"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    <div className="input-group">
                        <label className="input-label">Password</label>
                        <input
                            type="password"
                            placeholder="••••••••"
                            className="glass-input"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button type="submit" className="btn btn-primary w-full" disabled={loading}>
                        {loading ? 'Processing...' : (isLogin ? <><LogIn size={18} /> Login</> : <><UserPlus size={18} /> Register</>)}
                    </button>
                </form>

                <div className="login-footer">
                    <button className="text-btn" onClick={() => setIsLogin(!isLogin)}>
                        {isLogin ? "Don't have an account? Register" : "Already have an account? Log in"}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Login;
