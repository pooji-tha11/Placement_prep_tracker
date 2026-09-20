import { useState } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiFileText } from 'react-icons/fi';

export default function Resumes() {
  const { data: resumes, loading, error, postData, deleteData } = useApi('/resumes');
  const [filter, setFilter] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({ label: '', version: '', filename: '', dateAdded: '' });

  const filteredResumes = resumes?.filter(r => 
    r.label.toLowerCase().includes(filter.toLowerCase()) || 
    r.version.toLowerCase().includes(filter.toLowerCase())
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await postData('/resumes', formData);
      setShowModal(false);
      setFormData({ label: '', version: '', filename: '', dateAdded: '' });
    } catch (err) {
      alert(err.message);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this resume?")) {
      await deleteData(`/resumes/${id}`);
    }
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Resumes</h1>
        <button 
          onClick={() => setShowModal(true)}
          className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm transition-colors"
        >
          <FiPlus className="mr-2" /> Add Resume
        </button>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border">
        <input 
          type="text" 
          placeholder="Filter resumes by label or version..." 
          className="w-full mb-6 p-3 rounded-2xl bg-surfaceAlt border border-border focus:outline-none focus:border-plum text-ink"
          value={filter}
          onChange={e => setFilter(e.target.value)}
        />

        {loading ? (
          <div className="text-center text-plum py-8">Loading...</div>
        ) : error ? (
          <div className="text-center text-danger py-8">{error}</div>
        ) : filteredResumes?.length === 0 ? (
          <div className="text-center text-inkMuted py-8">No resumes found. Add your first one!</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {filteredResumes?.map(resume => (
              <div key={resume.id} className="p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-between items-center group">
                <div className="flex items-center space-x-4">
                  <div className="p-3 bg-surface text-clay rounded-2xl">
                    <FiFileText size={24} />
                  </div>
                  <div>
                    <h3 className="font-semibold text-ink text-lg">{resume.label}</h3>
                    <p className="text-sm text-inkMuted">{resume.filename} • {resume.version}</p>
                    <p className="text-xs text-inkMuted mt-1">Added: {resume.dateAdded}</p>
                  </div>
                </div>
                <button 
                  onClick={() => handleDelete(resume.id)}
                  className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 transition-opacity bg-white hover:bg-danger hover:text-white rounded-full"
                  title="Delete"
                >
                  <FiTrash2 size={18} />
                </button>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md">
            <h2 className="text-2xl font-bold text-plum mb-4">Add Resume</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Label</label>
                <input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.label} onChange={e => setFormData({...formData, label: e.target.value})} />
              </div>
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Version</label>
                <input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.version} onChange={e => setFormData({...formData, version: e.target.value})} />
              </div>
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Filename</label>
                <input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.filename} onChange={e => setFormData({...formData, filename: e.target.value})} />
              </div>
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Date Added</label>
                <input required type="date" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.dateAdded} onChange={e => setFormData({...formData, dateAdded: e.target.value})} />
              </div>
              <div className="flex justify-end space-x-3 mt-6">
                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 rounded-2xl bg-surfaceAlt text-ink hover:bg-border">Cancel</button>
                <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
