import React, { useState, useEffect } from 'react';
import API from '../services/api';
import { 
  FileText, Upload, Sparkles, CheckCircle2, AlertCircle, 
  Briefcase, Award, MapPin, DollarSign, Calendar, RefreshCw 
} from 'lucide-react';

export default function StudentDashboard({ user }) {
  const [profile, setProfile] = useState({
    phone: '',
    branch: '',
    cgpa: '',
    graduationYear: '',
    skills: '',
    resumeAtsScore: null,
    extractedSkills: '',
    aiFeedback: '',
  });

  const [jobs, setJobs] = useState([]);
  const [myApplications, setMyApplications] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  
  const [profileLoading, setProfileLoading] = useState(false);
  const [resumeLoading, setResumeLoading] = useState(false);
  const [applyingJobId, setApplyingJobId] = useState(null);
  
  const [message, setMessage] = useState({ type: '', text: '' });
  const [activeTab, setActiveTab] = useState('drives'); // 'drives' | 'applications' | 'profile'

  useEffect(() => {
    fetchProfile();
    fetchJobs();
    fetchMyApplications();
  }, []);

  const fetchProfile = async () => {
    try {
      const res = await API.get('/student/profile');
      if (res.data && res.data.data) {
        setProfile(res.data.data);
      }
    } catch (err) {
      console.log('Profile not created yet or error', err);
    }
  };

  const fetchJobs = async () => {
    try {
      const res = await API.get('/jobs');
      if (res.data && res.data.data) {
        setJobs(res.data.data);
      }
    } catch (err) {
      console.error('Failed to fetch jobs', err);
    }
  };

  const fetchMyApplications = async () => {
    try {
      const res = await API.get('/applications/my-applications');
      if (res.data && res.data.data) {
        setMyApplications(res.data.data);
      }
    } catch (err) {
      console.error('Failed to fetch applications', err);
    }
  };

  const handleProfileSubmit = async (e) => {
    e.preventDefault();
    setProfileLoading(true);
    setMessage({ type: '', text: '' });

    try {
      // If profile already exists, update (PUT), otherwise create (POST)
      const res = await API.put('/student/profile', {
        phone: profile.phone,
        branch: profile.branch,
        cgpa: parseFloat(profile.cgpa),
        graduationYear: parseInt(profile.graduationYear, 10),
        skills: profile.skills,
      });

      setProfile(res.data.data);
      setMessage({ type: 'success', text: 'Profile updated successfully!' });
    } catch (err) {
      // If PUT fails because it doesn't exist yet, try POST
      try {
        const res = await API.post('/student/profile', {
          phone: profile.phone,
          branch: profile.branch,
          cgpa: parseFloat(profile.cgpa),
          graduationYear: parseInt(profile.graduationYear, 10),
          skills: profile.skills,
        });
        setProfile(res.data.data);
        setMessage({ type: 'success', text: 'Profile created successfully!' });
      } catch (postErr) {
        setMessage({ 
          type: 'error', 
          text: postErr.response?.data?.message || 'Failed to save profile' 
        });
      }
    } finally {
      setProfileLoading(false);
    }
  };

  const handleResumeUpload = async (e) => {
    e.preventDefault();
    if (!selectedFile) {
      setMessage({ type: 'error', text: 'Please select a PDF resume first!' });
      return;
    }

    setResumeLoading(true);
    setMessage({ type: '', text: '' });

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const res = await API.post('/student/profile/resume', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      if (res.data && res.data.data) {
        setProfile(res.data.data);
        setMessage({ type: 'success', text: 'Resume analyzed by Gemini AI successfully!' });
      }
    } catch (err) {
      console.error(err);
      setMessage({ 
        type: 'error', 
        text: err.response?.data?.message || 'Failed to upload or analyze resume.' 
      });
    } finally {
      setResumeLoading(false);
    }
  };

  const handleApply = async (jobId) => {
    setApplyingJobId(jobId);
    setMessage({ type: '', text: '' });

    try {
      const res = await API.post(`/applications/apply/${jobId}`);
      if (res.data && res.data.data) {
        setMessage({ 
          type: 'success', 
          text: `Applied successfully! AI Match Score: ${res.data.data.aiMatchScore}%` 
        });
        fetchMyApplications();
      }
    } catch (err) {
      console.error(err);
      setMessage({ 
        type: 'error', 
        text: err.response?.data?.message || 'Application rejected by rule engine or server error.' 
      });
    } finally {
      setApplyingJobId(null);
    }
  };

  const hasApplied = (jobId) => {
    return myApplications.some((app) => app.jobDriveId === jobId);
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

      {/* Tabs */}
      <div className="flex border-b border-slate-200 gap-6">
        <button
          onClick={() => setActiveTab('drives')}
          className={`pb-3 text-sm font-semibold border-b-2 transition-all flex items-center gap-2 ${
            activeTab === 'drives'
              ? 'border-indigo-600 text-indigo-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <Briefcase className="w-4 h-4" /> Available Job Drives ({jobs.length})
        </button>
        <button
          onClick={() => setActiveTab('applications')}
          className={`pb-3 text-sm font-semibold border-b-2 transition-all flex items-center gap-2 ${
            activeTab === 'applications'
              ? 'border-indigo-600 text-indigo-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <Award className="w-4 h-4" /> My Applications ({myApplications.length})
        </button>
        <button
          onClick={() => setActiveTab('profile')}
          className={`pb-3 text-sm font-semibold border-b-2 transition-all flex items-center gap-2 ${
            activeTab === 'profile'
              ? 'border-indigo-600 text-indigo-600'
              : 'border-transparent text-slate-500 hover:text-slate-800'
          }`}
        >
          <FileText className="w-4 h-4" /> Profile & AI Resume
        </button>
      </div>

      {/* TAB 1: AVAILABLE DRIVES */}
      {activeTab === 'drives' && (
        <div className="space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-bold text-slate-900">Campus Placement Drives</h2>
            <button
              onClick={fetchJobs}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium text-slate-600 hover:text-indigo-600 bg-white border border-slate-200 rounded-lg hover:border-indigo-200 transition-colors shadow-sm"
            >
              <RefreshCw className="w-3.5 h-3.5" /> Refresh
            </button>
          </div>

          {jobs.length === 0 ? (
            <div className="text-center py-12 bg-white rounded-2xl border border-slate-200/80 p-8">
              <Briefcase className="w-12 h-12 text-slate-300 mx-auto mb-3" />
              <p className="text-slate-600 font-medium">No job drives posted yet.</p>
              <p className="text-xs text-slate-400 mt-1">Check back later when recruiters publish new openings!</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {jobs.map((job) => {
                const applied = hasApplied(job.id);
                return (
                  <div
                    key={job.id}
                    className="bg-white rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-shadow p-6 flex flex-col justify-between"
                  >
                    <div>
                      <div className="flex items-start justify-between gap-4">
                        <div>
                          <span className="inline-block px-2.5 py-0.5 rounded-full text-xs font-semibold bg-indigo-50 text-indigo-700 border border-indigo-100 mb-2">
                            {job.companyName}
                          </span>
                          <h3 className="text-base font-bold text-slate-900">{job.title}</h3>
                        </div>
                        {applied && (
                          <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium bg-emerald-100 text-emerald-800 border border-emerald-200">
                            <CheckCircle2 className="w-3 h-3" /> Applied
                          </span>
                        )}
                      </div>

                      <p className="text-xs text-slate-600 mt-3 line-clamp-3 leading-relaxed">
                        {job.description}
                      </p>

                      <div className="grid grid-cols-2 gap-3 mt-4 pt-4 border-t border-slate-100 text-xs">
                        <div className="flex items-center gap-1.5 text-slate-600">
                          <DollarSign className="w-3.5 h-3.5 text-slate-400" />
                          <span><strong>Package:</strong> {job.packageDetails || 'Not specified'}</span>
                        </div>
                        <div className="flex items-center gap-1.5 text-slate-600">
                          <MapPin className="w-3.5 h-3.5 text-slate-400" />
                          <span><strong>Location:</strong> {job.location || 'Remote/Campus'}</span>
                        </div>
                      </div>

                      {/* Rule Engine Badges */}
                      <div className="mt-4 p-3 bg-slate-50 rounded-xl border border-slate-100 space-y-1.5 text-xs">
                        <div className="font-semibold text-slate-700">Eligibility Engine Criteria:</div>
                        <div className="flex flex-wrap gap-2 text-[11px]">
                          <span className="px-2 py-0.5 bg-white border border-slate-200 rounded text-slate-700">
                            Min CGPA: <strong>{job.minimumCgpa || 'Any'}</strong>
                          </span>
                          <span className="px-2 py-0.5 bg-white border border-slate-200 rounded text-slate-700">
                            Branches: <strong>{job.eligibleBranches || 'All'}</strong>
                          </span>
                          <span className="px-2 py-0.5 bg-white border border-slate-200 rounded text-slate-700">
                            Year: <strong>{job.graduationYear || 'Any'}</strong>
                          </span>
                        </div>
                        {job.requiredSkills && (
                          <div className="text-[11px] text-slate-500 pt-1">
                            Skills: <em>{job.requiredSkills}</em>
                          </div>
                        )}
                      </div>
                    </div>

                    <div className="mt-6 pt-4 border-t border-slate-100 flex items-center justify-between">
                      <div className="text-[11px] text-slate-400 flex items-center gap-1">
                        <Calendar className="w-3 h-3" />
                        Deadline: {job.deadline ? new Date(job.deadline).toLocaleDateString() : 'Rolling'}
                      </div>

                      <button
                        onClick={() => handleApply(job.id)}
                        disabled={applied || applyingJobId === job.id}
                        className={`px-4 py-2 text-xs font-semibold rounded-lg transition-all flex items-center gap-1.5 shadow-sm ${
                          applied
                            ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
                            : 'bg-indigo-600 hover:bg-indigo-700 text-white shadow-indigo-100'
                        }`}
                      >
                        {applyingJobId === job.id ? (
                          <>
                            <span className="w-3 h-3 border-2 border-white border-t-transparent rounded-full animate-spin"></span>
                            AI Matching...
                          </>
                        ) : applied ? (
                          'Already Applied'
                        ) : (
                          <>
                            <Sparkles className="w-3.5 h-3.5" /> Apply with AI Check
                          </>
                        )}
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      )}

      {/* TAB 2: MY APPLICATIONS */}
      {activeTab === 'applications' && (
        <div className="space-y-6">
          <h2 className="text-lg font-bold text-slate-900">Your Submitted Applications</h2>

          {myApplications.length === 0 ? (
            <div className="text-center py-12 bg-white rounded-2xl border border-slate-200/80 p-8">
              <Award className="w-12 h-12 text-slate-300 mx-auto mb-3" />
              <p className="text-slate-600 font-medium">You haven't applied to any drives yet.</p>
              <button
                onClick={() => setActiveTab('drives')}
                className="mt-3 inline-flex items-center gap-1.5 text-xs text-indigo-600 font-semibold hover:underline"
              >
                Browse available drives &rarr;
              </button>
            </div>
          ) : (
            <div className="grid grid-cols-1 gap-4">
              {myApplications.map((app) => (
                <div
                  key={app.id}
                  className="bg-white rounded-xl border border-slate-200/80 p-5 shadow-sm hover:shadow-md transition-shadow flex flex-col md:flex-row md:items-center justify-between gap-4"
                >
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-semibold px-2.5 py-0.5 rounded-full bg-slate-100 text-slate-700">
                        {app.companyName}
                      </span>
                      <span className="text-xs text-slate-400">•</span>
                      <span className="text-xs text-slate-500">
                        Applied on {new Date(app.applicationDate).toLocaleDateString()}
                      </span>
                    </div>
                    <h3 className="text-base font-bold text-slate-900">{app.jobTitle}</h3>
                    {app.aiFeedback && (
                      <p className="text-xs text-slate-600 italic bg-indigo-50/50 p-2.5 rounded-lg border border-indigo-100/60 mt-2">
                        <strong>AI Evaluation:</strong> "{app.aiFeedback}"
                      </p>
                    )}
                  </div>

                  <div className="flex items-center gap-4 shrink-0">
                    <div className="text-right">
                      <div className="text-xs text-slate-400 font-medium">Gemini Match Score</div>
                      <div className={`text-xl font-black ${
                        app.aiMatchScore >= 80 ? 'text-emerald-600' : app.aiMatchScore >= 60 ? 'text-indigo-600' : 'text-amber-600'
                      }`}>
                        {app.aiMatchScore !== null ? `${app.aiMatchScore}%` : 'N/A'}
                      </div>
                    </div>

                    <span className="px-3 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800 border border-blue-200">
                      {app.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* TAB 3: PROFILE & AI RESUME */}
      {activeTab === 'profile' && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          
          {/* Profile Details Form */}
          <div className="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-sm">
            <h3 className="text-base font-bold text-slate-900 mb-1">Academic Profile</h3>
            <p className="text-xs text-slate-500 mb-6">
              This information is evaluated by the Eligibility Engine before you can apply.
            </p>

            <form onSubmit={handleProfileSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Branch</label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. CS, IT, ENTC"
                    value={profile.branch || ''}
                    onChange={(e) => setProfile({ ...profile, branch: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Current CGPA</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    max="10"
                    required
                    placeholder="e.g. 8.5"
                    value={profile.cgpa || ''}
                    onChange={(e) => setProfile({ ...profile, cgpa: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Graduation Year</label>
                  <input
                    type="number"
                    required
                    placeholder="e.g. 2026"
                    value={profile.graduationYear || ''}
                    onChange={(e) => setProfile({ ...profile, graduationYear: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Phone Number</label>
                  <input
                    type="text"
                    placeholder="e.g. 9876543210"
                    value={profile.phone || ''}
                    onChange={(e) => setProfile({ ...profile, phone: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Key Technical Skills</label>
                <input
                  type="text"
                  placeholder="e.g. Java, Spring Boot, MySQL, React"
                  value={profile.skills || ''}
                  onChange={(e) => setProfile({ ...profile, skills: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:outline-none"
                />
              </div>

              <button
                type="submit"
                disabled={profileLoading}
                className="w-full py-2.5 px-4 bg-indigo-600 hover:bg-indigo-700 text-white font-medium text-xs rounded-lg transition-colors shadow-sm disabled:opacity-60"
              >
                {profileLoading ? 'Saving Profile...' : 'Save Profile Details'}
              </button>
            </form>
          </div>

          {/* AI Resume Upload & Analysis */}
          <div className="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-sm flex flex-col justify-between">
            <div>
              <div className="flex items-center justify-between mb-1">
                <h3 className="text-base font-bold text-slate-900">Gemini AI Resume Review</h3>
                <span className="inline-flex items-center gap-1 text-[11px] font-semibold text-indigo-600 bg-indigo-50 px-2.5 py-0.5 rounded-full border border-indigo-100">
                  <Sparkles className="w-3 h-3" /> Powered by Gemini
                </span>
              </div>
              <p className="text-xs text-slate-500 mb-6">
                Upload your PDF resume to extract skills and score against industry standards.
              </p>

              <form onSubmit={handleResumeUpload} className="space-y-4">
                <div className="border-2 border-dashed border-slate-300 hover:border-indigo-400 rounded-xl p-6 text-center transition-colors">
                  <Upload className="w-8 h-8 text-slate-400 mx-auto mb-2" />
                  <label className="block text-xs font-semibold text-indigo-600 hover:text-indigo-700 cursor-pointer">
                    <span>{selectedFile ? selectedFile.name : 'Choose PDF Resume'}</span>
                    <input
                      type="file"
                      accept=".pdf"
                      onChange={(e) => setSelectedFile(e.target.files[0])}
                      className="hidden"
                    />
                  </label>
                  <p className="text-[11px] text-slate-400 mt-1">PDF format up to 5MB</p>
                </div>

                <button
                  type="submit"
                  disabled={resumeLoading || !selectedFile}
                  className="w-full py-2.5 px-4 bg-gradient-to-r from-indigo-600 to-violet-600 hover:from-indigo-700 hover:to-violet-700 text-white font-medium text-xs rounded-lg transition-all shadow-sm disabled:opacity-60 flex items-center justify-center gap-2"
                >
                  {resumeLoading ? (
                    <>
                      <span className="w-3.5 h-3.5 border-2 border-white border-t-transparent rounded-full animate-spin"></span>
                      Gemini is Reviewing...
                    </>
                  ) : (
                    <>
                      <Sparkles className="w-4 h-4" /> Upload & Run AI Analysis
                    </>
                  )}
                </button>
              </form>
            </div>

            {/* AI Results Display */}
            {profile.resumeAtsScore !== null && profile.resumeAtsScore !== undefined && (
              <div className="mt-6 pt-6 border-t border-slate-100 space-y-4">
                <div className="flex items-center justify-between p-3.5 bg-indigo-50/70 border border-indigo-100 rounded-xl">
                  <div>
                    <div className="text-xs font-medium text-indigo-900">Resume ATS Score</div>
                    <div className="text-[11px] text-indigo-600">Calculated by Google Gemini</div>
                  </div>
                  <div className="text-2xl font-black text-indigo-600">
                    {profile.resumeAtsScore}/100
                  </div>
                </div>

                {profile.extractedSkills && (
                  <div>
                    <div className="text-xs font-semibold text-slate-700 mb-1.5">AI-Extracted Skills:</div>
                    <div className="flex flex-wrap gap-1.5">
                      {profile.extractedSkills.split(',').map((skill, idx) => (
                        <span
                          key={idx}
                          className="px-2 py-0.5 rounded text-[11px] bg-slate-100 text-slate-700 font-medium border border-slate-200"
                        >
                          {skill.trim()}
                        </span>
                      ))}
                    </div>
                  </div>
                )}

                {profile.aiFeedback && (
                  <div className="text-xs text-slate-600 bg-slate-50 p-3 rounded-lg border border-slate-200/80">
                    <strong className="text-slate-800">Gemini Feedback:</strong> {profile.aiFeedback}
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
