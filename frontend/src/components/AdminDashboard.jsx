import React, { useState, useEffect } from 'react';
import API from '../services/api';
import { 
  ShieldCheck, Users, Building2, Briefcase, Award, 
  Sparkles, RefreshCw, AlertCircle 
} from 'lucide-react';

export default function AdminDashboard({ user }) {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await API.get('/admin/dashboard');
      if (res.data && res.data.data) {
        setStats(res.data.data);
      }
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || 'Failed to load TPO dashboard metrics.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
        <div>
          <div className="flex items-center gap-2 mb-1">
            <span className="p-1 rounded-md bg-amber-100 text-amber-800">
              <ShieldCheck className="w-4 h-4" />
            </span>
            <span className="text-xs font-semibold text-amber-800 tracking-wider uppercase">
              TPO Head Command Center
            </span>
          </div>
          <h2 className="text-xl font-bold text-slate-900">Campus Placement Live Analytics</h2>
          <p className="text-xs text-slate-500 mt-0.5">
            Real-time bird's-eye metrics on students, recruiting partners, and active drives.
          </p>
        </div>

        <button
          onClick={fetchStats}
          disabled={loading}
          className="inline-flex items-center gap-2 px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold rounded-xl transition-colors shrink-0"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} /> Refresh Metrics
        </button>
      </div>

      {error && (
        <div className="p-4 rounded-xl flex items-center gap-3 bg-red-50 border border-red-200 text-red-800 text-xs font-medium">
          <AlertCircle className="w-4 h-4 shrink-0" />
          {error}
        </div>
      )}

      {loading && !stats ? (
        <div className="py-20 text-center text-xs text-slate-400">
          <span className="w-6 h-6 border-2 border-indigo-600 border-t-transparent rounded-full animate-spin inline-block mb-2"></span>
          <p>Aggregating campus placement metrics...</p>
        </div>
      ) : stats ? (
        <>
          {/* Metric Cards Grid */}
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">
            
            <div className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm">
              <div className="flex items-center justify-between text-slate-400 mb-3">
                <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">Students</span>
                <Users className="w-4 h-4 text-blue-500" />
              </div>
              <div className="text-2xl font-black text-slate-900">{stats.totalStudents}</div>
              <div className="text-[11px] text-slate-400 mt-1">Registered Profiles</div>
            </div>

            <div className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm">
              <div className="flex items-center justify-between text-slate-400 mb-3">
                <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">Recruiters</span>
                <Building2 className="w-4 h-4 text-purple-500" />
              </div>
              <div className="text-2xl font-black text-slate-900">{stats.totalRecruiters}</div>
              <div className="text-[11px] text-slate-400 mt-1">HR Representatives</div>
            </div>

            <div className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm">
              <div className="flex items-center justify-between text-slate-400 mb-3">
                <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">Companies</span>
                <Building2 className="w-4 h-4 text-indigo-500" />
              </div>
              <div className="text-2xl font-black text-slate-900">{stats.totalCompanies}</div>
              <div className="text-[11px] text-slate-400 mt-1">Onboarded Partners</div>
            </div>

            <div className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm">
              <div className="flex items-center justify-between text-slate-400 mb-3">
                <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">Active Drives</span>
                <Briefcase className="w-4 h-4 text-emerald-500" />
              </div>
              <div className="text-2xl font-black text-slate-900">{stats.totalActiveJobs}</div>
              <div className="text-[11px] text-slate-400 mt-1">Open for Application</div>
            </div>

            <div className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm col-span-2 sm:col-span-1">
              <div className="flex items-center justify-between text-slate-400 mb-3">
                <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">Applications</span>
                <Award className="w-4 h-4 text-amber-500" />
              </div>
              <div className="text-2xl font-black text-slate-900">{stats.totalApplications}</div>
              <div className="text-[11px] text-slate-400 mt-1">Total Submissions</div>
            </div>
          </div>

          {/* Recent Applications Feed */}
          <div className="bg-white rounded-2xl border border-slate-200/80 shadow-sm p-6 space-y-4">
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-base font-bold text-slate-900">Recent Campus Submissions</h3>
                <p className="text-xs text-slate-500">
                  Latest candidates who passed eligibility and underwent AI matchmaking.
                </p>
              </div>
              <span className="inline-flex items-center gap-1 text-[11px] font-semibold text-indigo-600 bg-indigo-50 px-2.5 py-1 rounded-full border border-indigo-100">
                <Sparkles className="w-3.5 h-3.5" /> AI Evaluated
              </span>
            </div>

            {stats.recentApplications?.length === 0 ? (
              <div className="py-8 text-center text-xs text-slate-400">
                No job applications submitted yet.
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-left text-xs">
                  <thead>
                    <tr className="border-b border-slate-200 text-slate-400 uppercase tracking-wider font-semibold">
                      <th className="pb-3 font-semibold">Student</th>
                      <th className="pb-3 font-semibold">Applied Role</th>
                      <th className="pb-3 font-semibold">Company</th>
                      <th className="pb-3 font-semibold">Applied Date</th>
                      <th className="pb-3 font-semibold text-right">AI Match</th>
                      <th className="pb-3 font-semibold text-right">Status</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {stats.recentApplications.map((app) => (
                      <tr key={app.id} className="hover:bg-slate-50/50 transition-colors">
                        <td className="py-3 font-medium text-slate-900">
                          <div>{app.studentName}</div>
                          <div className="text-[11px] text-slate-400">{app.studentEmail}</div>
                        </td>
                        <td className="py-3 text-slate-700 font-medium">{app.jobTitle}</td>
                        <td className="py-3 text-slate-600">{app.companyName}</td>
                        <td className="py-3 text-slate-500">
                          {new Date(app.applicationDate).toLocaleDateString()}
                        </td>
                        <td className="py-3 text-right">
                          <span className={`px-2 py-0.5 rounded-full font-bold ${
                            app.aiMatchScore >= 80
                              ? 'bg-emerald-100 text-emerald-800'
                              : app.aiMatchScore >= 60
                              ? 'bg-indigo-100 text-indigo-800'
                              : 'bg-amber-100 text-amber-800'
                          }`}>
                            {app.aiMatchScore !== null ? `${app.aiMatchScore}%` : 'N/A'}
                          </span>
                        </td>
                        <td className="py-3 text-right">
                          <span className="px-2 py-0.5 rounded-full bg-slate-100 text-slate-700 font-semibold text-[11px]">
                            {app.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </>
      ) : null}
    </div>
  );
}
