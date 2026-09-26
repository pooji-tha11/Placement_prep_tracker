import { useState, useEffect } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiBriefcase, FiSearch, FiChevronDown, FiChevronUp, FiExternalLink, FiMaximize2 } from 'react-icons/fi';
import { api } from '../api/client';
import DetailModal from '../components/DetailModal';
import ApplicationForm from '../components/ApplicationForm';

export default function Applications() {
  const { data: applications, loading, deleteData, fetchData, postData, putData } = useApi('/applications');
  const [resumes, setResumes] = useState([]);
  const [skillsList, setSkillsList] = useState([]);
  
  const [showModal, setShowModal] = useState(false);
  const [showSearch, setShowSearch] = useState(false);
  const [searchParams, setSearchParams] = useState({ company: '', status: '', skill: '' });
  
  const [editingRecord, setEditingRecord] = useState(null);
  const [viewDetail, setViewDetail] = useState(null);

  useEffect(() => {
    api.get('/resumes').then(setResumes).catch(console.error);
    api.get('/applications/skills').then(setSkillsList).catch(console.error);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    const query = new URLSearchParams();
    if (searchParams.company) query.append('company', searchParams.company);
    if (searchParams.status) query.append('status', searchParams.status);
    if (searchParams.skill) query.append('skill', searchParams.skill);
    fetchData(`/applications/search?${query.toString()}`);
  };

  const handleReset = () => {
    setSearchParams({ company: '', status: '', skill: '' });
    fetchData('/applications');
  };

  const handleSave = async (data) => {
    try {
      if (editingRecord) {
        await putData(`/applications/${editingRecord.id}`, data);
      } else {
        await postData('/applications', data);
      }
      setShowModal(false);
      setEditingRecord(null);
      api.get('/applications/skills').then(setSkillsList).catch(console.error);
    } catch (err) {
      alert(err.message);
    }
  };

  const handleStatusChange = async (id, newStatus) => {
    try {
      await api.patch(`/applications/${id}/status`, { status: newStatus });
      fetchData();
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Applications</h1>
        <button onClick={() => { setEditingRecord(null); setShowModal(true); }} className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm">
          <FiPlus className="mr-2" /> Add Application
        </button>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border space-y-4">
        <button onClick={() => setShowSearch(!showSearch)} className="flex items-center text-plum font-medium hover:text-plumDark transition-colors">
          <FiSearch className="mr-2" /> Advanced Search {showSearch ? <FiChevronUp className="ml-1"/> : <FiChevronDown className="ml-1"/>}
        </button>
        
        {showSearch && (
          <form onSubmit={handleSearch} className="grid grid-cols-1 md:grid-cols-4 gap-4 p-4 bg-surfaceAlt rounded-2xl border border-border">
            <div><label className="block text-xs mb-1 text-inkMuted">Company</label><input type="text" className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.company} onChange={e => setSearchParams({...searchParams, company: e.target.value})} /></div>
            <div>
              <label className="block text-xs mb-1 text-inkMuted">Status</label>
              <select className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.status} onChange={e => setSearchParams({...searchParams, status: e.target.value})}>
                <option value="">Any</option>
                <option value="SAVED">Saved</option>
                <option value="APPLIED">Applied</option>
                <option value="ASSESSMENT">Assessment</option>
                <option value="INTERVIEW">Interview</option>
                <option value="SELECTED">Selected</option>
                <option value="REJECTED">Rejected</option>
              </select>
            </div>
            <div>
              <label className="block text-xs mb-1 text-inkMuted">Skill</label>
              <select className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.skill} onChange={e => setSearchParams({...searchParams, skill: e.target.value})}>
                <option value="">Any</option>
                {skillsList.map(s => <option key={s} value={s}>{s}</option>)}
              </select>
            </div>
            <div className="flex items-end space-x-2">
              <button type="submit" className="w-full bg-plum text-white p-2 rounded-xl hover:bg-plumDark">Search</button>
              <button type="button" onClick={handleReset} className="w-full bg-white text-ink p-2 rounded-xl hover:bg-border border border-border">Reset</button>
            </div>
          </form>
        )}

        {loading ? <div className="text-center py-8">Loading...</div> : applications?.length === 0 ? <div className="text-center text-inkMuted py-8">No applications found. Go apply to something!</div> : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {applications?.map(app => (
              <div key={app.id} className="p-5 rounded-2xl bg-surfaceAlt border border-border group relative flex flex-col justify-between">
                <div>
                  <div className="flex justify-between items-start mb-2">
                    <div className="flex items-center space-x-3">
                      <div className="p-3 bg-surface text-clay rounded-xl"><FiBriefcase size={20} /></div>
                      <div>
                        <h3 className="font-semibold text-lg leading-tight flex items-center">
                          {app.company}
                          {app.jobLink && (
                            <a href={app.jobLink} target="_blank" rel="noopener noreferrer" className="ml-2 text-inkMuted hover:text-plum transition-colors" title="View Job Description">
                              <FiExternalLink size={16} />
                            </a>
                          )}
                        </h3>
                        <p className="text-sm text-plum font-medium">{app.role}</p>
                      </div>
                    </div>
                    <div className="flex space-x-2">
                      <button onClick={() => setViewDetail(app)} className="p-2 text-inkMuted opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-surface hover:text-ink transition-all"><FiMaximize2 size={16} /></button>
                      <button onClick={() => window.confirm("Delete?") && deleteData(`/applications/${app.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 size={16} /></button>
                    </div>
                  </div>
                  <p className="text-xs text-inkMuted mb-3">Applied: {app.dateApplied}</p>
                </div>
                
                <div className="mt-2 pt-4 border-t border-border flex justify-between items-center">
                  <select 
                    value={app.status} 
                    onChange={e => handleStatusChange(app.id, e.target.value)}
                    className={`text-xs px-3 py-1.5 rounded-full font-medium appearance-none cursor-pointer outline-none ${
                      app.status === 'APPLIED' ? 'bg-surface border border-border text-ink' :
                      (app.status === 'INTERVIEW' || app.status === 'ASSESSMENT') ? 'bg-clay/20 text-clay' :
                      app.status === 'SELECTED' ? 'bg-success/20 text-[#6a8756]' : 
                      app.status === 'REJECTED' ? 'bg-danger/20 text-danger' : 'bg-surfaceAlt text-inkMuted'
                    }`}
                  >
                    <option value="SAVED">Saved</option>
                    <option value="APPLIED">Applied</option>
                    <option value="ASSESSMENT">Assessment</option>
                    <option value="INTERVIEW">Interview</option>
                    <option value="SELECTED">Selected</option>
                    <option value="REJECTED">Rejected</option>
                  </select>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md max-h-[90vh] overflow-y-auto">
            <h2 className="text-2xl font-bold text-plum mb-4">{editingRecord ? 'Edit Application' : 'Add Application'}</h2>
            <ApplicationForm 
              initialValues={editingRecord} 
              resumes={resumes}
              onSubmit={handleSave} 
              onCancel={() => { setShowModal(false); setEditingRecord(null); }} 
            />
          </div>
        </div>
      )}

      {viewDetail && (
        <DetailModal
          title={`Application: ${viewDetail.company} - ${viewDetail.role}`}
          fields={[
            { label: 'Company', value: viewDetail.company },
            { label: 'Role', value: viewDetail.role },
            { label: 'Status', value: viewDetail.status },
            { label: 'Date Applied', value: viewDetail.dateApplied },
            { label: 'Required Skills', value: viewDetail.requiredSkills?.join(', ') },
            { label: 'Job Link', value: viewDetail.jobLink },
            { label: 'Job Description', value: viewDetail.jobDescription },
            { label: 'Notes', value: viewDetail.notes },
            { label: 'Resume Used', value: resumes.find(r => r.id === viewDetail.resumeId)?.label }
          ]}
          actions={
            <button 
              onClick={() => { setEditingRecord(viewDetail); setShowModal(true); setViewDetail(null); }} 
              className="px-4 py-2 bg-plum text-white rounded-2xl hover:bg-plumDark"
            >
              Edit
            </button>
          }
          onClose={() => setViewDetail(null)}
        />
      )}
    </div>
  );
}
