import { useState, useEffect } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiCode, FiSearch, FiChevronDown, FiChevronUp } from 'react-icons/fi';
import { api } from '../api/client';

export default function DSA() {
  const { data: dsa, loading, deleteData, fetchData, postData } = useApi('/dsa');
  const [avgConf, setAvgConf] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [showSearch, setShowSearch] = useState(false);
  
  const [searchParams, setSearchParams] = useState({ tag: '', difficulty: '', minConfidence: '' });
  const [formData, setFormData] = useState({ platform: '', dsaTag: '', difficulty: 'EASY', confidenceLevel: '3', solvedDate: '' });

  useEffect(() => {
    api.get('/dsa/average-confidence').then(res => setAvgConf(res)).catch(()=>setAvgConf(0));
  }, [dsa]);

  const handleSearch = (e) => {
    e.preventDefault();
    const query = new URLSearchParams();
    if (searchParams.tag) query.append('tag', searchParams.tag);
    if (searchParams.difficulty) query.append('difficulty', searchParams.difficulty);
    if (searchParams.minConfidence) query.append('minConfidence', searchParams.minConfidence);
    fetchData(`/dsa/search?${query.toString()}`);
  };

  const handleReset = () => {
    setSearchParams({ tag: '', difficulty: '', minConfidence: '' });
    fetchData('/dsa');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await postData('/dsa', { ...formData, confidenceLevel: parseInt(formData.confidenceLevel, 10) });
      setShowModal(false);
      setFormData({ platform: '', dsaTag: '', difficulty: 'EASY', confidenceLevel: '3', solvedDate: '' });
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">DSA Tracker</h1>
        <div className="flex space-x-4 items-center">
          <div className="hidden sm:block bg-surfaceAlt px-4 py-2 rounded-2xl text-inkMuted font-medium shadow-sm">
            Avg Confidence: <span className="text-plum">{avgConf.toFixed(1)}/5</span>
          </div>
          <button onClick={() => setShowModal(true)} className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm">
            <FiPlus className="mr-2" /> Add Problem
          </button>
        </div>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border space-y-4">
        <button onClick={() => setShowSearch(!showSearch)} className="flex items-center text-plum font-medium hover:text-plumDark transition-colors">
          <FiSearch className="mr-2" /> Advanced Search {showSearch ? <FiChevronUp className="ml-1"/> : <FiChevronDown className="ml-1"/>}
        </button>
        
        {showSearch && (
          <form onSubmit={handleSearch} className="grid grid-cols-1 md:grid-cols-4 gap-4 p-4 bg-surfaceAlt rounded-2xl border border-border">
            <div><label className="block text-xs mb-1 text-inkMuted">Tag</label><input type="text" className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.tag} onChange={e => setSearchParams({...searchParams, tag: e.target.value})} /></div>
            <div>
              <label className="block text-xs mb-1 text-inkMuted">Difficulty</label>
              <select className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.difficulty} onChange={e => setSearchParams({...searchParams, difficulty: e.target.value})}>
                <option value="">Any</option>
                <option value="EASY">Easy</option><option value="MEDIUM">Medium</option><option value="HARD">Hard</option>
              </select>
            </div>
            <div><label className="block text-xs mb-1 text-inkMuted">Min Confidence</label><input type="number" min="1" max="5" className="w-full p-2 rounded-xl border focus:border-plum" value={searchParams.minConfidence} onChange={e => setSearchParams({...searchParams, minConfidence: e.target.value})} /></div>
            <div className="flex items-end space-x-2">
              <button type="submit" className="w-full bg-plum text-white p-2 rounded-xl hover:bg-plumDark">Search</button>
              <button type="button" onClick={handleReset} className="w-full bg-white text-ink p-2 rounded-xl hover:bg-border border border-border">Reset</button>
            </div>
          </form>
        )}

        {loading ? <div className="text-center py-8">Loading...</div> : dsa?.length === 0 ? <div className="text-center text-inkMuted py-8">No problems found. <br/> <span className="text-sm">Nice, one more problem solved! 🎉</span></div> : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {dsa?.map(p => (
              <div key={p.id} className="p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-between items-center group relative overflow-hidden">
                <div className={`absolute left-0 top-0 bottom-0 w-2 ${p.difficulty==='EASY'?'bg-success':p.difficulty==='MEDIUM'?'bg-clay':'bg-danger'}`} />
                <div className="flex items-center space-x-4 pl-3">
                  <div className="p-3 bg-surface text-plum rounded-2xl"><FiCode size={24} /></div>
                  <div>
                    <h3 className="font-semibold text-lg">{p.platform} - {p.dsaTag}</h3>
                    <p className="text-sm text-inkMuted">Diff: {p.difficulty} • Conf: {p.confidenceLevel}/5</p>
                    <p className="text-xs text-inkMuted mt-1">{p.solvedDate}</p>
                  </div>
                </div>
                <button onClick={() => window.confirm("Delete?") && deleteData(`/dsa/${p.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 size={18} /></button>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md">
            <h2 className="text-2xl font-bold text-plum mb-4">Log Problem</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div><label className="block text-sm mb-1">Platform</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border focus:border-plum" value={formData.platform} onChange={e => setFormData({...formData, platform: e.target.value})} /></div>
              <div><label className="block text-sm mb-1">Topic/Tag</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border focus:border-plum" value={formData.dsaTag} onChange={e => setFormData({...formData, dsaTag: e.target.value})} /></div>
              <div>
                <label className="block text-sm mb-1">Difficulty</label>
                <select className="w-full p-2 rounded-2xl bg-surfaceAlt border focus:border-plum" value={formData.difficulty} onChange={e => setFormData({...formData, difficulty: e.target.value})}>
                  <option value="EASY">Easy</option><option value="MEDIUM">Medium</option><option value="HARD">Hard</option>
                </select>
              </div>
              <div><label className="block text-sm mb-1">Confidence (1-5)</label><input required type="number" min="1" max="5" className="w-full p-2 rounded-2xl bg-surfaceAlt border focus:border-plum" value={formData.confidenceLevel} onChange={e => setFormData({...formData, confidenceLevel: e.target.value})} /></div>
              <div><label className="block text-sm mb-1">Date Solved</label><input required type="date" className="w-full p-2 rounded-2xl bg-surfaceAlt border focus:border-plum" value={formData.solvedDate} onChange={e => setFormData({...formData, solvedDate: e.target.value})} /></div>
              
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
