import { useState, useEffect } from 'react';
import { api } from '../api/client';
import { FiCalendar, FiClock, FiTrendingUp, FiAward, FiPlus } from 'react-icons/fi';

export default function Study() {
  const [sessions, setSessions] = useState([]);
  const [stats, setStats] = useState({ current: 0, longest: 0, weekly: 0 });
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({ date: '', durationMinutes: '', topic: '' });

  const fetchData = async () => {
    setLoading(true);
    try {
      const [sess, cur, lon, week] = await Promise.all([
        api.get('/study'),
        api.get('/study/streak/current').catch(()=>0),
        api.get('/study/streak/longest').catch(()=>0),
        api.get('/study/weekly-minutes').catch(()=>0)
      ]);
      setSessions(sess);
      setStats({ current: cur, longest: lon, weekly: week });
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchData(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/study', { ...formData, durationMinutes: parseInt(formData.durationMinutes, 10) });
      setShowModal(false);
      setFormData({ date: '', durationMinutes: '', topic: '' });
      fetchData();
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Study Streaks</h1>
        <button onClick={() => setShowModal(true)} className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm transition-colors">
          <FiPlus className="mr-2" /> Log Session
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border flex items-center space-x-4">
          <div className="p-3 bg-success/20 text-success rounded-2xl"><FiTrendingUp size={24} /></div>
          <div><p className="text-sm text-inkMuted">Current Streak</p><h3 className="text-2xl font-bold">{stats.current}</h3></div>
        </div>
        <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border flex items-center space-x-4">
          <div className="p-3 bg-plum/20 text-plum rounded-2xl"><FiAward size={24} /></div>
          <div><p className="text-sm text-inkMuted">Longest Streak</p><h3 className="text-2xl font-bold">{stats.longest}</h3></div>
        </div>
        <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border flex items-center space-x-4">
          <div className="p-3 bg-clay/20 text-clay rounded-2xl"><FiClock size={24} /></div>
          <div><p className="text-sm text-inkMuted">Weekly Minutes</p><h3 className="text-2xl font-bold">{stats.weekly}</h3></div>
        </div>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border">
        <h2 className="text-xl font-semibold text-ink mb-4">Study History</h2>
        {loading ? <div className="text-center py-4">Loading...</div> : sessions.length === 0 ? <div className="text-center text-inkMuted py-4">No sessions logged yet.</div> : (
          <div className="space-y-3">
            {sessions.map(s => (
              <div key={s.id} className="flex justify-between items-center p-4 bg-surfaceAlt rounded-2xl border border-border">
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-surface text-inkMuted rounded-xl"><FiCalendar /></div>
                  <div>
                    <p className="font-semibold">{s.topic}</p>
                    <p className="text-xs text-inkMuted">{s.date}</p>
                  </div>
                </div>
                <div className="font-medium text-plum">{s.durationMinutes} mins</div>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md">
            <h2 className="text-2xl font-bold text-plum mb-4">Log Session</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Date</label>
                <input required type="date" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.date} onChange={e => setFormData({...formData, date: e.target.value})} />
              </div>
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Topic</label>
                <input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.topic} onChange={e => setFormData({...formData, topic: e.target.value})} />
              </div>
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Duration (minutes)</label>
                <input required type="number" min="1" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.durationMinutes} onChange={e => setFormData({...formData, durationMinutes: e.target.value})} />
              </div>
              <div className="flex justify-end space-x-3 mt-6">
                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 rounded-2xl bg-surfaceAlt text-ink hover:bg-border">Cancel</button>
                <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Log Study</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
