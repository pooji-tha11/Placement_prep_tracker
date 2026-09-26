import { useState, useEffect } from 'react';

export default function DsaForm({ initialValues, onSubmit, onCancel }) {
  const [formData, setFormData] = useState({
    platform: '', dsaTag: '', difficulty: 'EASY', confidenceLevel: '3', solvedDate: ''
  });

  useEffect(() => {
    if (initialValues) {
      setFormData({
        platform: initialValues.platform || '',
        dsaTag: initialValues.dsaTag || '',
        difficulty: initialValues.difficulty || 'EASY',
        confidenceLevel: initialValues.confidenceLevel?.toString() || '3',
        solvedDate: initialValues.solvedDate || ''
      });
    }
  }, [initialValues]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({
      ...formData,
      confidenceLevel: parseInt(formData.confidenceLevel, 10)
    });
  };

  return (
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
        <button type="button" onClick={onCancel} className="px-4 py-2 rounded-2xl bg-surfaceAlt hover:bg-border">Cancel</button>
        <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
      </div>
    </form>
  );
}
