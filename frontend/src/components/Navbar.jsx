import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Database, LayoutDashboard, UserPlus, Search, LogOut } from 'lucide-react';
import './Navbar.css';

const Navbar = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    if (!user) return null;

    return (
        <nav className="navbar glass-panel">
            <div className="nav-brand">
                <Database size={24} color="var(--accent-blue)" />
                <span>MDM Platform</span>
            </div>

            <div className="nav-links">
                <NavLink to="/dashboard" className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}>
                    <LayoutDashboard size={18} /> Dashboard
                </NavLink>
                <NavLink to="/ingest" className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}>
                    <UserPlus size={18} /> Ingest Record
                </NavLink>
                <NavLink to="/search" className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}>
                    <Search size={18} /> Golden Search
                </NavLink>
            </div>

            <div className="nav-user">
                <span className="user-badge">{user.username} ({user.role})</span>
                <button onClick={handleLogout} className="btn-logout" title="Logout">
                    <LogOut size={18} />
                </button>
            </div>
        </nav>
    );
};

export default Navbar;
