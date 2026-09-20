import { useState } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiAward, FiFlag, FiFileText } from 'react-icons/fi';
import { api } from '../api/client';

export default function Achievements() {
  const { data: achievements, loading, error, deleteData, fetchData } = useApi('/achievements');
  const [filter, setFilter] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [type, setType] = useState('HACKATHON');
  const [formData, setFormData] = useState({ name: '', organizer: '', result: '', date: '', issuingOrg: '', rank: '' });

  const filtered = achievements?.filter(a => a.name?.toLowerCase().includes(filter.toLowerCase()));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (type === 'HACKATHON') {
        await api.post('/achievements/hackathon', { name: formData.name, organizer: formData.organizer, result: formData.result, date: formData.date });
      } else if (type === 'CERTIFICATION') {
        await api.post('/achievements/certification', { name: formData.name, issuingOrg: formData.issuingOrg, date: formData.date });
      } else if (type === 'AWARD') {
        await api.post('/achievements/award', { name: formData.name, organizer: formData.organizer, rank: parseInt(formData.rank, 10), date: formData.date });
      }
      setShowModal(false);
      setFormData({ name: '', organizer: '', result: '', date: '', issuingOrg: '', rank: '' });
      fetchData();
    } catch (err) {
      alert(err.message);
    }
  };

  const getIcon = (typeStr) => {
    if (typeStr === 'HACKATHON') return <FiFlag size={24} />;
    if (typeStr === 'CERTIFICATION') return <FiFileText size={24} />;
    return <FiAward size={24} />;
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Achievements</h1>
        <button onClick={() => setShowModal(true)} className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm">
          <FiPlus className="mr-2" /> Add Achievement
        </button>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border">
        <input type="text" placeholder="Filter achievements..." className="w-full mb-6 p-3 rounded-2xl bg-surfaceAlt border border-border focus:border-plum focus:outline-none" value={filter} onChange={e => setFilter(e.target.value)} />
        
        {loading ? <div className="text-center py-8">Loading...</div> : filtered?.length === 0 ? <div className="text-center text-inkMuted py-8">No achievements found.</div> : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {filtered?.map(a => (
              <div key={a.id} className="p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-between items-center group">
                <div className="flex items-center space-x-4">
                  <div className="p-3 bg-surface text-clay rounded-2xl">{getIcon(a.type)}</div>
                  <div>
                    <h3 className="font-semibold text-lg">{a.name}</h3>
                    <p className="text-xs font-bold text-plum mb-1">{a.type}</p>
                    <p className="text-sm text-inkMuted">
                      {a.type === 'HACKATHON' && `${a.organizer} • ${a.result}`}
                      {a.type === 'CERTIFICATION' && `${a.issuingOrg}`}
                      {a.type === 'AWARD' && `${a.organizer} • Rank: ${a.rank}`}
                    </p>
                    <p className="text-xs text-inkMuted mt-1">{a.date}</p>
                  </div>
                </div>
                <button onClick={() => window.confirm("Delete?") && deleteData(`/achievements/${a.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 size={18} /></button>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md overflow-y-auto max-h-screen">
            <h2 className="text-2xl font-bold text-plum mb-4">Add Achievement</h2>
            <div className="mb-4">
              <label className="block text-sm mb-1">Type</label>
              <select className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={type} onChange={e => setType(e.target.value)}>
                <option value="HACKATHON">Hackathon</option>
                <option value="CERTIFICATION">Certification</option>
                <option value="AWARD">Competition Award</option>
              </select>
            </div>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div><label className="block text-sm mb-1">Name</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} /></div>
              <div><label className="block text-sm mb-1">Date</label><input required type="date" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.date} onChange={e => setFormData({...formData, date: e.target.value})} /></div>
              
              {type === 'HACKATHON' && (
                <>
                  <div><label className="block text-sm mb-1">Organizer</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.organizer} onChange={e => setFormData({...formData, organizer: e.target.value})} /></div>
                  <div><label className="block text-sm mb-1">Result</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.result} onChange={e => setFormData({...formData, result: e.target.value})} /></div>
                </>
              )}
              {type === 'CERTIFICATION' && (
                <div><label className="block text-sm mb-1">Issuing Org</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.issuingOrg} onChange={e => setFormData({...formData, issuingOrg: e.target.value})} /></div>
              )}
              {type === 'AWARD' && (
                <>
                  <div><label className="block text-sm mb-1">Organizer</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.organizer} onChange={e => setFormData({...formData, organizer: e.target.value})} /></div>
                  <div><label className="block text-sm mb-1">Rank</label><input required type="number" min="1" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.rank} onChange={e => setFormData({...formData, rank: e.target.value})} /></div>
                </>
              )}
              <div className="flex justify-end space-x-3 mt-6">
                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 rounded-2xl bg-surfaceAlt hover:bg-border">Cancel</button>
                <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
