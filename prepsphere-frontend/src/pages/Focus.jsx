import { useState, useEffect } from 'react';
import { api } from '../api/client';
import { FiClock, FiPlus, FiPlay } from 'react-icons/fi';

export default function Focus() {
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({ topic: '', durationMinutes: '25' });
  
  // Timer state
  const [activeTimer, setActiveTimer] = useState(null);
  const [timeLeft, setTimeLeft] = useState(0);

  const fetchHistory = async () => {
    try {
      const data = await api.get('/focus-sessions');
      setSessions(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchHistory(); }, []);

  useEffect(() => {
    let interval = null;
    if (activeTimer && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft(t => t - 1);
      }, 1000);
    } else if (activeTimer && timeLeft === 0) {
      setActiveTimer(null);
      fetchHistory(); // Refresh history when done
    }
    return () => clearInterval(interval);
  }, [activeTimer, timeLeft]);

  const startSession = async (e) => {
    e.preventDefault();
    const duration = parseInt(formData.durationMinutes, 10);
    try {
      await api.post('/focus-sessions', { topic: formData.topic, durationMinutes: duration });
      setShowModal(false);
      setFormData({ topic: '', durationMinutes: '25' });
      
      // Start local countdown
      setActiveTimer(formData.topic);
      setTimeLeft(duration * 60);
    } catch (err) {
      alert(err.message);
    }
  };

  const formatTime = (seconds) => {
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Focus Timer</h1>
        <button 
          onClick={() => setShowModal(true)} 
          disabled={activeTimer !== null}
          className={`px-4 py-2 rounded-2xl flex items-center shadow-sm transition-colors ${
            activeTimer ? 'bg-surfaceAlt text-inkMuted cursor-not-allowed' : 'bg-plum hover:bg-plumDark text-white'
          }`}
        >
          <FiPlay className="mr-2" /> Start Session
        </button>
      </div>

      {activeTimer && (
        <div className="bg-plum/10 p-8 rounded-3xl border border-plum/20 flex flex-col items-center justify-center space-y-4">
          <h2 className="text-xl font-medium text-plum">Focusing on: {activeTimer}</h2>
          <div className="text-6xl font-bold text-plum tabular-nums">
            {formatTime(timeLeft)}
          </div>
          <p className="text-inkMuted text-sm">Stay focused! Session was already recorded.</p>
        </div>
      )}

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border">
        <h2 className="text-xl font-semibold text-ink mb-4">Session History</h2>
        {loading ? <div className="text-center py-4">Loading...</div> : sessions.length === 0 ? <div className="text-center text-inkMuted py-4">No sessions yet. Time to focus!</div> : (
          <div className="space-y-3">
            {sessions.map(s => (
              <div key={s.id} className="flex justify-between items-center p-4 bg-surfaceAlt rounded-2xl border border-border">
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-surface text-inkMuted rounded-xl"><FiClock /></div>
                  <p className="font-semibold">{s.topic}</p>
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
            <h2 className="text-2xl font-bold text-plum mb-4">Start Focus Session</h2>
            <form onSubmit={startSession} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Topic</label>
                <input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.topic} onChange={e => setFormData({...formData, topic: e.target.value})} />
              </div>
              <div>
                <label className="block text-sm font-medium text-ink mb-1">Duration (minutes)</label>
                <select className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.durationMinutes} onChange={e => setFormData({...formData, durationMinutes: e.target.value})}>
                  <option value="15">15 minutes</option>
                  <option value="25">25 minutes (Pomodoro)</option>
                  <option value="45">45 minutes</option>
                  <option value="60">60 minutes</option>
                </select>
              </div>
              <div className="flex justify-end space-x-3 mt-6">
                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 rounded-2xl bg-surfaceAlt text-ink hover:bg-border">Cancel</button>
                <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Start</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
