import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Ingest from './pages/Ingest';
import GoldenRecord from './pages/GoldenRecord';

const ProtectedRoute = ({ children }) => {
  const { user, loading } = useAuth();
  if (loading) return <div>Loading...</div>;
  if (!user) return <Navigate to="/login" replace />;
  return children;
};

function App() {
  const { user } = useAuth();

  return (
    <div className="app-layout">
      {user && <Navbar />}
      <main className="main-content">
        <Routes>
          <Route path="/login" element={!user ? <Login /> : <Navigate to="/dashboard" />} />

          <Route path="/dashboard" element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          } />

          <Route path="/ingest" element={
            <ProtectedRoute>
              <Ingest />
            </ProtectedRoute>
          } />

          <Route path="/search" element={
            <ProtectedRoute>
              <GoldenRecord />
            </ProtectedRoute>
          } />

          <Route path="*" element={<Navigate to={user ? "/dashboard" : "/login"} replace />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
