import React, { useState, useEffect } from 'react';
import API from '../services/api';
import { 
  Building2, PlusCircle, Users, CheckCircle2, AlertCircle, 
  MapPin, DollarSign, Calendar, Sparkles, RefreshCw, ChevronRight 
} from 'lucide-react';

export default function RecruiterDashboard({ user }) {
  const [jobs, setJobs] = useState([]);
  const [selectedJob, setSelectedJob] = useState(null);
  const [applicants, setApplicants] = useState([]);
  const [loadingApplicants, setLoadingApplicants] = useState(false);

  const [showPostModal, setShowPostModal] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });
  const [submittingJob, setSubmittingJob] = useState(false);

  // New Job Drive Form State
  const [newJob, setNewJob] = useState({
    title: '',
    description: '',
    packageDetails: '',
    location: '',
    minimumCgpa: '',
    eligibleBranches: 'CS, IT',
    graduationYear: 2026,
    requiredSkills: '',
    deadline: '',
  });

  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      const res = await API.get('/jobs');
      if (res.data && res.data.data) {
        setJobs(res.data.data);
      }
    } catch (err) {
      console.error('Error fetching jobs', err);
    }
  };

  const handleSelectJob = async (job) => {
    setSelectedJob(job);
    setLoadingApplicants(true);
    try {
      const res = await API.get(`/applications/job/${job.id}`);
      if (res.data && res.data.data) {
        // Sort candidates by AI Match Score descending!
        const sorted = [...res.data.data].sort((a, b) => (b.aiMatchScore || 0) - (a.aiMatchScore || 0));
        setApplicants(sorted);
      }
    } catch (err) {
      console.error('Error fetching applicants', err);
      setMessage({ type: 'error', text: 'Failed to load applicants for this job.' });
    } finally {
      setLoadingApplicants(false);
    }
  };

  const handlePostJob = async (e) => {
    e.preventDefault();
    setSubmittingJob(true);
    setMessage({ type: '', text: '' });

    try {
      const payload = {
        ...newJob,
        minimumCgpa: newJob.minimumCgpa ? parseFloat(newJob.minimumCgpa) : null,
        graduationYear: newJob.graduationYear ? parseInt(newJob.graduationYear, 10) : null,
        deadline: newJob.deadline ? `${newJob.deadline}T23:59:59` : null,
      };

      await API.post('/jobs', payload);
      setMessage({ type: 'success', text: 'Job drive posted successfully!' });
      setShowPostModal(false);
      setNewJob({
        title: '',
        description: '',
        packageDetails: '',
        location: '',
        minimumCgpa: '',
        eligibleBranches: 'CS, IT',
        graduationYear: 2026,
        requiredSkills: '',
        deadline: '',
      });
      fetchJobs();
    } catch (err) {
      console.error(err);
      setMessage({
        type: 'error',
        text: err.response?.data?.message || 'Failed to post job drive. Make sure your Recruiter Profile is set up!',
      });
    } finally {
      setSubmittingJob(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      
      {/* Alert Banner */}
      {message.text && (
        <div className={`p-4 rounded-xl flex items-start gap-3 border shadow-sm ${
          message.type === 'success' 
            ? 'bg-emerald-50 border-emerald-200 text-emerald-800' 
            : 'bg-red-50 border-red-200 text-red-800'
        }`}>
          {message.type === 'success' ? (
            <CheckCircle2 className="w-5 h-5 text-emerald-600 mt-0.5 shrink-0" />
          ) : (
            <AlertCircle className="w-5 h-5 text-red-600 mt-0.5 shrink-0" />
          )}
          <div className="text-sm font-medium">{message.text}</div>
        </div>
      )}

      {/* Top Action Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-white p-6 rounded-2xl border border-slate-200/80 shadow-sm">
        <div>
          <h2 className="text-xl font-bold text-slate-900">Recruiter Management Portal</h2>
          <p className="text-xs text-slate-500 mt-1">
            Publish campus drives with hard eligibility rules and evaluate AI-ranked talent.
          </p>
        </div>

        <button
          onClick={() => setShowPostModal(true)}
          className="inline-flex items-center gap-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-semibold rounded-xl transition-colors shadow-sm shrink-0"
        >
          <PlusCircle className="w-4 h-4" /> Create New Job Drive
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Left Column: List of Jobs */}
        <div className="lg:col-span-1 space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-bold text-slate-800 uppercase tracking-wider">
              Your Drives ({jobs.length})
            </h3>
            <button
              onClick={fetchJobs}
              className="text-slate-400 hover:text-slate-600 transition-colors p-1"
              title="Refresh"
            >
              <RefreshCw className="w-4 h-4" />
            </button>
          </div>

          {jobs.length === 0 ? (
            <div className="p-6 text-center bg-white rounded-xl border border-slate-200 text-xs text-slate-400">
              No job drives published yet. Click "Create New Job Drive" above!
            </div>
          ) : (
            <div className="space-y-3">
              {jobs.map((job) => (
                <div
                  key={job.id}
                  onClick={() => handleSelectJob(job)}
                  className={`p-4 rounded-xl border cursor-pointer transition-all ${
                    selectedJob?.id === job.id
                      ? 'bg-indigo-50/70 border-indigo-400 shadow-sm ring-2 ring-indigo-500/10'
                      : 'bg-white border-slate-200/80 hover:border-slate-300'
                  }`}
                >
                  <div className="flex items-start justify-between">
                    <div>
                      <div className="text-xs font-semibold text-indigo-600">{job.companyName}</div>
                      <h4 className="text-sm font-bold text-slate-900">{job.title}</h4>
                    </div>
                    <ChevronRight className="w-4 h-4 text-slate-400 mt-1" />
                  </div>

                  <div className="flex items-center gap-3 mt-3 text-[11px] text-slate-500">
                    <span>{job.packageDetails || 'N/A'}</span>
                    <span>•</span>
                    <span>Min CGPA: {job.minimumCgpa || 'Any'}</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Right Column: Applicants Table */}
        <div className="lg:col-span-2">
          {selectedJob ? (
            <div className="bg-white rounded-2xl border border-slate-200/80 shadow-sm p-6 space-y-6">
              <div className="flex items-start justify-between border-b border-slate-100 pb-4">
                <div>
                  <span className="text-xs font-semibold text-indigo-600 uppercase tracking-wider">
                    Applicants for:
                  </span>
                  <h3 className="text-lg font-bold text-slate-900">{selectedJob.title}</h3>
                  <div className="flex items-center gap-4 text-xs text-slate-500 mt-1">
                    <span>Target Branches: {selectedJob.eligibleBranches}</span>
                    <span>•</span>
                    <span>Min CGPA: {selectedJob.minimumCgpa}</span>
                  </div>
                </div>

                <div className="text-right">
                  <div className="text-xs text-slate-400">Total Applicants</div>
                  <div className="text-2xl font-black text-slate-900">{applicants.length}</div>
                </div>
              </div>

              {loadingApplicants ? (
                <div className="py-12 text-center text-xs text-slate-400">
                  <span className="w-5 h-5 border-2 border-indigo-600 border-t-transparent rounded-full animate-spin inline-block mb-2"></span>
                  <p>Loading candidate evaluation rankings...</p>
                </div>
              ) : applicants.length === 0 ? (
                <div className="py-12 text-center text-slate-400 text-xs">
                  <Users className="w-8 h-8 mx-auto mb-2 text-slate-300" />
                  No students have applied to this job drive yet.
                </div>
              ) : (
                <div className="space-y-4">
                  <div className="text-xs font-semibold text-slate-500 flex items-center gap-1.5">
                    <Sparkles className="w-4 h-4 text-indigo-600" />
                    Candidates Automatically Ranked by Gemini AI Match Score
                  </div>

                  <div className="divide-y divide-slate-100 border border-slate-100 rounded-xl overflow-hidden">
                    {applicants.map((cand) => (
                      <div key={cand.id} className="p-4 hover:bg-slate-50/60 transition-colors space-y-2">
                        <div className="flex items-center justify-between">
                          <div>
                            <span className="text-sm font-bold text-slate-900">{cand.studentName}</span>
                            <span className="text-xs text-slate-400 ml-2">({cand.studentEmail})</span>
                          </div>

                          <div className="flex items-center gap-2">
                            <span className="text-xs text-slate-500">AI Match:</span>
                            <span className={`px-2.5 py-0.5 rounded-full text-xs font-bold ${
                              cand.aiMatchScore >= 80
                                ? 'bg-emerald-100 text-emerald-800'
                                : cand.aiMatchScore >= 60
                                ? 'bg-indigo-100 text-indigo-800'
                                : 'bg-amber-100 text-amber-800'
                            }`}>
                              {cand.aiMatchScore !== null ? `${cand.aiMatchScore}%` : 'N/A'}
                            </span>
                          </div>
                        </div>

                        {cand.aiFeedback && (
                          <div className="text-xs text-slate-600 bg-slate-50 p-2.5 rounded-lg border border-slate-200/60">
                            <strong>AI Summary:</strong> {cand.aiFeedback}
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="h-full min-h-[300px] flex flex-col items-center justify-center p-8 bg-white rounded-2xl border border-dashed border-slate-300 text-slate-400 text-center">
              <Users className="w-10 h-10 text-slate-300 mb-2" />
              <p className="text-sm font-medium text-slate-600">Select a Job Drive on the left</p>
              <p className="text-xs text-slate-400 mt-1">
                View candidate submissions ranked by our Gemini Matchmaking engine.
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Modal: Post New Job Drive */}
      {showPostModal && (
        <div className="fixed inset-0 z-50 bg-black/40 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl max-w-xl w-full p-6 shadow-2xl max-h-[90vh] overflow-y-auto space-y-5">
            <div className="flex items-center justify-between border-b border-slate-100 pb-3">
              <h3 className="text-base font-bold text-slate-900">Post New Campus Drive</h3>
              <button
                onClick={() => setShowPostModal(false)}
                className="text-slate-400 hover:text-slate-600 text-lg font-bold"
              >
                &times;
              </button>
            </div>

            <form onSubmit={handlePostJob} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Job Title *</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. SDE-1 / Cloud Engineer"
                  value={newJob.title}
                  onChange={(e) => setNewJob({ ...newJob, title: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Job Description *</label>
                <textarea
                  rows="3"
                  required
                  placeholder="Describe the role, responsibilities, and team..."
                  value={newJob.description}
                  onChange={(e) => setNewJob({ ...newJob, description: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Package (CTC)</label>
                  <input
                    type="text"
                    placeholder="e.g. 14 LPA"
                    value={newJob.packageDetails}
                    onChange={(e) => setNewJob({ ...newJob, packageDetails: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Location</label>
                  <input
                    type="text"
                    placeholder="e.g. Bangalore / Pune / Hybrid"
                    value={newJob.location}
                    onChange={(e) => setNewJob({ ...newJob, location: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
              </div>

              <div className="p-4 bg-slate-50 rounded-xl border border-slate-200/80 space-y-3">
                <span className="text-xs font-bold text-slate-800 uppercase tracking-wider block">
                  Eligibility Rule Engine Settings
                </span>

                <div className="grid grid-cols-3 gap-3">
                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Min CGPA</label>
                    <input
                      type="number"
                      step="0.1"
                      placeholder="e.g. 7.5"
                      value={newJob.minimumCgpa}
                      onChange={(e) => setNewJob({ ...newJob, minimumCgpa: e.target.value })}
                      className="w-full px-2.5 py-1.5 text-xs border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                    />
                  </div>
                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Eligible Branches</label>
                    <input
                      type="text"
                      placeholder="e.g. CS, IT"
                      value={newJob.eligibleBranches}
                      onChange={(e) => setNewJob({ ...newJob, eligibleBranches: e.target.value })}
                      className="w-full px-2.5 py-1.5 text-xs border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                    />
                  </div>
                  <div>
                    <label className="block text-[11px] font-semibold text-slate-600 mb-1">Batch Year</label>
                    <input
                      type="number"
                      placeholder="e.g. 2026"
                      value={newJob.graduationYear}
                      onChange={(e) => setNewJob({ ...newJob, graduationYear: e.target.value })}
                      className="w-full px-2.5 py-1.5 text-xs border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-[11px] font-semibold text-slate-600 mb-1">Required Skills</label>
                  <input
                    type="text"
                    placeholder="e.g. Java, Spring Boot, MySQL, Docker"
                    value={newJob.requiredSkills}
                    onChange={(e) => setNewJob({ ...newJob, requiredSkills: e.target.value })}
                    className="w-full px-2.5 py-1.5 text-xs border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Application Deadline</label>
                <input
                  type="date"
                  value={newJob.deadline}
                  onChange={(e) => setNewJob({ ...newJob, deadline: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <div className="flex justify-end gap-3 pt-3 border-t border-slate-100">
                <button
                  type="button"
                  onClick={() => setShowPostModal(false)}
                  className="px-4 py-2 text-xs font-semibold text-slate-600 hover:bg-slate-100 rounded-lg transition-colors"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submittingJob}
                  className="px-5 py-2 text-xs font-semibold text-white bg-indigo-600 hover:bg-indigo-700 rounded-lg transition-colors shadow-sm disabled:opacity-60"
                >
                  {submittingJob ? 'Publishing...' : 'Publish Drive'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
