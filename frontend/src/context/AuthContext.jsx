import React, { createContext, useContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                // Simple expiration check
                if (decoded.exp * 1000 < Date.now()) {
                    logout();
                } else {
                    setUser({ username: decoded.sub, role: decoded.role, token });
                }
            } catch (e) {
                logout();
            }
        }
        setLoading(false);
    }, []);

    const login = (token, role, username) => {
        localStorage.setItem('token', token);
        setUser({ username, role, token });
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
