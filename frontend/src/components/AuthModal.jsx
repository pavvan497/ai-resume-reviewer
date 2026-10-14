import React, { useState } from 'react';
import API from '../services/api';
import { LogIn, UserPlus, Sparkles, Shield, Building, GraduationCap } from 'lucide-react';

export default function AuthModal({ onLoginSuccess }) {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'STUDENT',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
    setMessage('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');

    try {
      if (isLogin) {
        // Call POST /auth/login
        const res = await API.post('/auth/login', {
          email: formData.email,
          password: formData.password,
        });

        if (res.data && res.data.data) {
          const authData = res.data.data;
          localStorage.setItem('token', authData.token);
          localStorage.setItem('user', JSON.stringify(authData));
          onLoginSuccess(authData);
        }
      } else {
        // Call POST /users
        await API.post('/users', {
          name: formData.name,
          email: formData.email,
          password: formData.password,
          role: formData.role,
        });

        setMessage('Account registered successfully! Please log in.');
        setIsLogin(true);
      }
    } catch (err) {
      console.error(err);
      const errMsg =
        err.response?.data?.message ||
        (typeof err.response?.data === 'string' ? err.response.data : 'Authentication failed. Please check your credentials.');
      setError(errMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-gradient-to-br from-slate-100 via-indigo-50/40 to-slate-100">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-xl border border-slate-200/80 overflow-hidden">
        
        {/* Header Banner */}
        <div className="bg-gradient-to-r from-indigo-600 via-indigo-700 to-violet-800 p-8 text-white text-center">
          <div className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-white/10 backdrop-blur-md mb-3 border border-white/20">
            <Sparkles className="w-8 h-8 text-indigo-200" />
          </div>
          <h2 className="text-2xl font-bold tracking-tight">AI Placement Platform</h2>
          <p className="text-xs text-indigo-100 mt-1 font-normal">
            Next-Gen Campus Recruitment & AI Resume Matchmaking
          </p>
        </div>

        {/* Tab Toggle */}
        <div className="flex border-b border-slate-200 bg-slate-50/50">
          <button
            type="button"
            onClick={() => { setIsLogin(true); setError(''); setMessage(''); }}
            className={`flex-1 py-3 text-sm font-semibold text-center border-b-2 transition-all ${
              isLogin
                ? 'border-indigo-600 text-indigo-600 bg-white'
                : 'border-transparent text-slate-500 hover:text-slate-800'
            }`}
          >
            <span className="inline-flex items-center gap-1.5">
              <LogIn className="w-4 h-4" /> Sign In
            </span>
          </button>
          <button
            type="button"
            onClick={() => { setIsLogin(false); setError(''); setMessage(''); }}
            className={`flex-1 py-3 text-sm font-semibold text-center border-b-2 transition-all ${
              !isLogin
                ? 'border-indigo-600 text-indigo-600 bg-white'
                : 'border-transparent text-slate-500 hover:text-slate-800'
            }`}
          >
            <span className="inline-flex items-center gap-1.5">
              <UserPlus className="w-4 h-4" /> Register
            </span>
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {error && (
            <div className="p-3 text-xs rounded-lg bg-red-50 text-red-700 border border-red-200">
              {error}
            </div>
          )}

          {message && (
            <div className="p-3 text-xs rounded-lg bg-emerald-50 text-emerald-700 border border-emerald-200">
              {message}
            </div>
          )}

          {!isLogin && (
            <div>
              <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                Full Name
              </label>
              <input
                type="text"
                name="name"
                required
                value={formData.name}
                onChange={handleChange}
                placeholder="e.g. Pavan Kumar"
                className="w-full px-3.5 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
              />
            </div>
          )}

          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Email Address
            </label>
            <input
              type="email"
              name="email"
              required
              value={formData.email}
              onChange={handleChange}
              placeholder="e.g. pavan@college.edu"
              className="w-full px-3.5 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Password
            </label>
            <input
              type="password"
              name="password"
              required
              value={formData.password}
              onChange={handleChange}
              placeholder="••••••••"
              className="w-full px-3.5 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
            />
          </div>

          {!isLogin && (
            <div>
              <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1.5">
                Select Your Role
              </label>
              <div className="grid grid-cols-3 gap-2">
                <button
                  type="button"
                  onClick={() => setFormData({ ...formData, role: 'STUDENT' })}
                  className={`p-2.5 rounded-lg border text-xs font-medium flex flex-col items-center gap-1 transition-all ${
                    formData.role === 'STUDENT'
                      ? 'border-indigo-600 bg-indigo-50/70 text-indigo-700 ring-2 ring-indigo-500/20'
                      : 'border-slate-200 text-slate-600 hover:bg-slate-50'
                  }`}
                >
                  <GraduationCap className="w-5 h-5 text-indigo-600" />
                  Student
                </button>

                <button
                  type="button"
                  onClick={() => setFormData({ ...formData, role: 'RECRUITER' })}
                  className={`p-2.5 rounded-lg border text-xs font-medium flex flex-col items-center gap-1 transition-all ${
                    formData.role === 'RECRUITER'
                      ? 'border-purple-600 bg-purple-50/70 text-purple-700 ring-2 ring-purple-500/20'
                      : 'border-slate-200 text-slate-600 hover:bg-slate-50'
                  }`}
                >
                  <Building className="w-5 h-5 text-purple-600" />
                  Recruiter
                </button>

                <button
                  type="button"
                  onClick={() => setFormData({ ...formData, role: 'ADMIN' })}
                  className={`p-2.5 rounded-lg border text-xs font-medium flex flex-col items-center gap-1 transition-all ${
                    formData.role === 'ADMIN'
                      ? 'border-amber-600 bg-amber-50/70 text-amber-800 ring-2 ring-amber-500/20'
                      : 'border-slate-200 text-slate-600 hover:bg-slate-50'
                  }`}
                >
                  <Shield className="w-5 h-5 text-amber-600" />
                  TPO Admin
                </button>
              </div>
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full mt-2 py-2.5 px-4 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white font-medium text-sm transition-colors shadow-sm disabled:opacity-60 flex items-center justify-center gap-2"
          >
            {loading ? (
              <span className="inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></span>
            ) : isLogin ? (
              <>
                <LogIn className="w-4 h-4" /> Sign In
              </>
            ) : (
              <>
                <UserPlus className="w-4 h-4" /> Create Account
              </>
            )}
          </button>
        </form>
      </div>
    </div>
  );
}
