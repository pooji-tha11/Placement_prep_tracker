import { useState } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiFolder, FiSearch, FiChevronDown, FiChevronUp, FiStar, FiCheckCircle, FiArrowRight, FiExternalLink } from 'react-icons/fi';
import { api } from '../api/client';

export default function Projects() {
  const { data: projects, loading, deleteData, fetchData, postData } = useApi('/projects');
  const [showModal, setShowModal] = useState(false);
  const [showSearch, setShowSearch] = useState(false);
  const [searchParams, setSearchParams] = useState({ domain: '', technology: '' });
  const [formData, setFormData] = useState({ title: '', domain: '', techStack: '', repoLink: '', status: 'PLANNED' });

  // STAR Wizard State
  const [starWizardProj, setStarWizardProj] = useState(null);
  const [starStep, setStarStep] = useState(0); // 0=Situation, 1=Task, 2=Action, 3=Result
  const [starData, setStarData] = useState({ situation: '', task: '', action: '', result: '' });
  const [starError, setStarError] = useState('');
  const [viewStarProj, setViewStarProj] = useState(null);

  const handleSearch = (e) => {
    e.preventDefault();
    const query = new URLSearchParams();
    if (searchParams.domain) query.append('domain', searchParams.domain);
    if (searchParams.technology) query.append('technology', searchParams.technology);
    fetchData(`/projects/search?${query.toString()}`);
  };

  const handleReset = () => {
    setSearchParams({ domain: '', technology: '' });
    fetchData('/projects');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await postData('/projects', { ...formData, techStack: formData.techStack.split(',').map(s=>s.trim()).filter(Boolean) });
      setShowModal(false);
      setFormData({ title: '', domain: '', techStack: '', repoLink: '', status: 'PLANNED' });
    } catch (err) {
      alert(err.message);
    }
  };

  const openStarWizard = (project) => {
    setStarWizardProj(project);
    setStarStep(0);
    setStarError('');
    if (project.starForm) {
      setStarData({
        situation: project.starForm.situation || '',
        task: project.starForm.task || '',
        action: project.starForm.action || '',
        result: project.starForm.result || ''
      });
    } else {
      setStarData({ situation: '', task: '', action: '', result: '' });
    }
  };

  const handleStatusChange = async (id, newStatus) => {
    try {
      await api.patch(`/projects/${id}/status`, { status: newStatus });
      fetchData();
    } catch (err) {
      alert(err.message);
    }
  };

  const submitStar = async () => {
    setStarError('');
    try {
      await api.post(`/projects/${starWizardProj.id}/star`, starData);
      setStarWizardProj(null);
      fetchData();
    } catch (err) {
      setStarError(err.message);
    }
  };

  const renderStarStep = () => {
    const steps = ['situation', 'task', 'action', 'result'];
    const currentKey = steps[starStep];
    const titles = ['Situation', 'Task', 'Action', 'Result'];
    
    return (
      <div className="space-y-4">
        <div className="flex justify-center mb-6 space-x-2">
          {steps.map((_, idx) => (
            <div key={idx} className={`h-2 w-2 rounded-full ${idx <= starStep ? 'bg-plum' : 'bg-surfaceAlt border border-border'}`} />
          ))}
        </div>
        <h2 className="text-xl font-bold text-plum text-center">{titles[starStep]}</h2>
        {starError && <div className="text-sm text-danger text-center bg-danger/10 p-2 rounded-xl">{starError}</div>}
        <textarea 
          className="w-full p-3 rounded-2xl bg-surfaceAlt border border-border focus:border-plum h-32 outline-none"
          placeholder={`Describe the ${titles[starStep].toLowerCase()}...`}
          value={starData[currentKey]}
          onChange={e => setStarData({...starData, [currentKey]: e.target.value})}
        />
        <div className="flex justify-between mt-6">
          <button onClick={() => { if(starStep > 0) setStarStep(s=>s-1); else setStarWizardProj(null); }} className="px-4 py-2 text-ink hover:bg-surfaceAlt rounded-2xl transition-colors">
            {starStep === 0 ? 'Cancel' : 'Back'}
          </button>
          <button 
            onClick={() => {
              if (starStep < 3) setStarStep(s=>s+1);
              else submitStar();
            }}
            className="px-4 py-2 bg-plum text-white rounded-2xl flex items-center hover:bg-plumDark transition-colors"
          >
            {starStep === 3 ? 'Finish' : 'Next'} {starStep < 3 && <FiArrowRight className="ml-2" />}
          </button>
        </div>
      </div>
    );
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Projects</h1>
        <button onClick={() => setShowModal(true)} className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm">
          <FiPlus className="mr-2" /> Add Project
        </button>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border space-y-4">
        <button onClick={() => setShowSearch(!showSearch)} className="flex items-center text-plum font-medium hover:text-plumDark transition-colors">
          <FiSearch className="mr-2" /> Advanced Search {showSearch ? <FiChevronUp className="ml-1"/> : <FiChevronDown className="ml-1"/>}
        </button>
        
        {showSearch && (
          <form onSubmit={handleSearch} className="grid grid-cols-1 md:grid-cols-3 gap-4 p-4 bg-surfaceAlt rounded-2xl border border-border">
            <div><label className="block text-xs mb-1 text-inkMuted">Domain</label><input type="text" className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.domain} onChange={e => setSearchParams({...searchParams, domain: e.target.value})} /></div>
            <div><label className="block text-xs mb-1 text-inkMuted">Technology</label><input type="text" className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.technology} onChange={e => setSearchParams({...searchParams, technology: e.target.value})} /></div>
            <div className="flex items-end space-x-2">
              <button type="submit" className="w-full bg-plum text-white p-2 rounded-xl hover:bg-plumDark">Search</button>
              <button type="button" onClick={handleReset} className="w-full bg-white text-ink p-2 rounded-xl hover:bg-border border border-border">Reset</button>
            </div>
          </form>
        )}

        {loading ? <div className="text-center py-8">Loading...</div> : projects?.length === 0 ? <div className="text-center text-inkMuted py-8">No projects found.</div> : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {projects?.map(p => (
              <div key={p.id} className="p-5 rounded-2xl bg-surfaceAlt border border-border group relative flex flex-col justify-between">
                <div>
                  <div className="flex justify-between items-start mb-2">
                    <div className="flex items-center space-x-3">
                      <div className="p-3 bg-surface text-plum rounded-xl"><FiFolder size={20} /></div>
                      <h3 className="font-semibold text-lg flex items-center">
                        {p.title}
                        {p.repoLink && (
                          <a href={p.repoLink} target="_blank" rel="noopener noreferrer" className="ml-2 text-inkMuted hover:text-plum transition-colors" title="View Repository">
                            <FiExternalLink size={16} />
                          </a>
                        )}
                      </h3>
                    </div>
                    <button onClick={() => window.confirm("Delete?") && deleteData(`/projects/${p.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 size={16} /></button>
                  </div>
                  <div className="text-sm text-inkMuted mb-2 flex items-center space-x-2">
                    <span>{p.domain} • Status:</span>
                    <select 
                      value={p.status} 
                      onChange={e => handleStatusChange(p.id, e.target.value)}
                      className={`text-xs px-2 py-1 rounded-md font-medium cursor-pointer outline-none border border-border ${
                        p.status === 'PLANNED' ? 'bg-surface text-ink' :
                        p.status === 'IN_PROGRESS' ? 'bg-clay/20 text-clay' : 'bg-success/20 text-[#6a8756]'
                      }`}
                    >
                      <option value="PLANNED">Planned</option>
                      <option value="IN_PROGRESS">In Progress</option>
                      <option value="COMPLETED">Completed</option>
                    </select>
                  </div>
                  <div className="flex flex-wrap gap-1 mb-4">
                    {p.techStack?.map(t => <span key={t} className="px-2 py-1 bg-surface text-xs text-inkMuted rounded-md border border-border">{t}</span>)}
                  </div>
                </div>
                
                <div className="mt-2 pt-4 border-t border-border flex justify-between items-center">
                  {p.starForm?.complete ? (
                    <div className="flex items-center space-x-2">
                      <span className="text-xs bg-success/20 text-[#6a8756] px-2 py-1 rounded-full flex items-center font-medium"><FiCheckCircle className="mr-1"/> Interview-Ready</span>
                      <button onClick={() => setViewStarProj(p)} className="text-xs text-inkMuted hover:text-plum font-medium underline">View STAR</button>
                    </div>
                  ) : (
                    <span className="text-xs text-inkMuted bg-surface px-2 py-1 rounded-full border border-border">STAR incomplete</span>
                  )}
                  <button onClick={() => openStarWizard(p)} className="text-sm text-plum flex items-center hover:text-plumDark font-medium bg-plum/10 px-2 py-1 rounded-lg">
                    <FiStar className="mr-1"/> {p.starForm?.complete ? 'Edit STAR' : 'Add STAR'}
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md">
            <h2 className="text-2xl font-bold text-plum mb-4">Add Project</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div><label className="block text-sm mb-1">Title</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.title} onChange={e => setFormData({...formData, title: e.target.value})} /></div>
              <div><label className="block text-sm mb-1">Domain</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.domain} onChange={e => setFormData({...formData, domain: e.target.value})} /></div>
              <div><label className="block text-sm mb-1">Tech Stack (comma separated)</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.techStack} onChange={e => setFormData({...formData, techStack: e.target.value})} /></div>
              <div><label className="block text-sm mb-1">Repository Link</label><input required type="url" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.repoLink} onChange={e => setFormData({...formData, repoLink: e.target.value})} /></div>
              <div>
                <label className="block text-sm mb-1">Status</label>
                <select className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.status} onChange={e => setFormData({...formData, status: e.target.value})}>
                  <option value="PLANNED">Planned</option><option value="IN_PROGRESS">In Progress</option><option value="COMPLETED">Completed</option>
                </select>
              </div>
              <div className="flex justify-end space-x-3 mt-6">
                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 rounded-2xl bg-surfaceAlt hover:bg-border">Cancel</button>
                <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {starWizardProj && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md">
            {renderStarStep()}
          </div>
        </div>
      )}

      {viewStarProj && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-2xl max-h-[80vh] overflow-y-auto">
            <h2 className="text-2xl font-bold text-plum mb-6">{viewStarProj.title} - STAR Method</h2>
            <div className="space-y-4">
              <div className="p-4 bg-surfaceAlt rounded-2xl border border-border">
                <h3 className="font-bold text-ink mb-2">Situation</h3>
                <p className="text-inkMuted text-sm whitespace-pre-wrap">{viewStarProj.starForm.situation}</p>
              </div>
              <div className="p-4 bg-surfaceAlt rounded-2xl border border-border">
                <h3 className="font-bold text-ink mb-2">Task</h3>
                <p className="text-inkMuted text-sm whitespace-pre-wrap">{viewStarProj.starForm.task}</p>
              </div>
              <div className="p-4 bg-surfaceAlt rounded-2xl border border-border">
                <h3 className="font-bold text-ink mb-2">Action</h3>
                <p className="text-inkMuted text-sm whitespace-pre-wrap">{viewStarProj.starForm.action}</p>
              </div>
              <div className="p-4 bg-surfaceAlt rounded-2xl border border-border">
                <h3 className="font-bold text-ink mb-2">Result</h3>
                <p className="text-inkMuted text-sm whitespace-pre-wrap">{viewStarProj.starForm.result}</p>
              </div>
            </div>
            <div className="flex justify-end mt-6">
              <button onClick={() => setViewStarProj(null)} className="px-4 py-2 bg-plum text-white rounded-2xl hover:bg-plumDark">Close</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
