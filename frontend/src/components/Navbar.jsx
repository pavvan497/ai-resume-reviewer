import React from 'react';
import { LogOut, GraduationCap, Building2, ShieldCheck, User } from 'lucide-react';

export default function Navbar({ user, onLogout }) {
  const getRoleBadge = (role) => {
    switch (role) {
      case 'STUDENT':
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-blue-100 text-blue-700 border border-blue-200">
            <GraduationCap className="w-3.5 h-3.5" /> Student
          </span>
        );
      case 'RECRUITER':
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-purple-100 text-purple-700 border border-purple-200">
            <Building2 className="w-3.5 h-3.5" /> Recruiter
          </span>
        );
      case 'ADMIN':
        return (
          <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-amber-100 text-amber-800 border border-amber-200">
            <ShieldCheck className="w-3.5 h-3.5" /> Admin / TPO
          </span>
        );
      default:
        return null;
    }
  };

  return (
    <header className="bg-white border-b border-slate-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-indigo-600 to-violet-500 flex items-center justify-center text-white shadow-md shadow-indigo-100">
            <GraduationCap className="w-6 h-6" />
          </div>
          <div>
            <h1 className="text-lg font-bold text-slate-900 leading-tight">AI Campus Placement</h1>
            <p className="text-xs text-slate-500 font-medium">Training & Placement Portal</p>
          </div>
        </div>

        <div className="flex items-center gap-4">
          <div className="flex items-center gap-2.5">
            <div className="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center text-slate-600">
              <User className="w-4 h-4" />
            </div>
            <div className="hidden sm:block text-left">
              <div className="text-sm font-semibold text-slate-800 leading-tight">{user.name}</div>
              <div className="text-xs text-slate-500">{user.email}</div>
            </div>
          </div>

          {getRoleBadge(user.role)}

          <button
            onClick={onLogout}
            className="inline-flex items-center gap-1.5 px-3.5 py-1.5 text-xs font-medium text-slate-600 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors border border-transparent hover:border-red-100"
            title="Logout"
          >
            <LogOut className="w-4 h-4" />
            <span className="hidden sm:inline">Logout</span>
          </button>
        </div>
      </div>
    </header>
  );
}
