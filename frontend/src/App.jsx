import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import AuthModal from './components/AuthModal';
import StudentDashboard from './components/StudentDashboard';
import RecruiterDashboard from './components/RecruiterDashboard';
import AdminDashboard from './components/AdminDashboard';

export default function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Check if user was previously logged in
    const storedUser = localStorage.getItem('user');
    const storedToken = localStorage.getItem('token');
    if (storedUser && storedToken) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        console.error('Failed to parse saved user', e);
      }
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  if (!user) {
    return <AuthModal onLoginSuccess={(userData) => setUser(userData)} />;
  }

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <Navbar user={user} onLogout={handleLogout} />
      
      <main className="flex-1">
        {user.role === 'STUDENT' && <StudentDashboard user={user} />}
        {user.role === 'RECRUITER' && <RecruiterDashboard user={user} />}
        {user.role === 'ADMIN' && <AdminDashboard user={user} />}
      </main>

      <footer className="border-t border-slate-200 bg-white py-4 text-center text-xs text-slate-400">
        AI-Powered Campus Placement & Recruitment Platform • Built with Spring Boot & React
      </footer>
    </div>
  );
}
