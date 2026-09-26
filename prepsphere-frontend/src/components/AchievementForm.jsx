import { useState, useEffect } from 'react';

export default function AchievementForm({ initialValues, onSubmit, onCancel }) {
  const [type, setType] = useState('HACKATHON');
  const [formData, setFormData] = useState({
    name: '', organizer: '', result: '', date: '', issuingOrg: '', rank: ''
  });

  useEffect(() => {
    if (initialValues) {
      setType(initialValues.type || 'HACKATHON');
      setFormData({
        name: initialValues.name || '',
        organizer: initialValues.organizer || '',
        result: initialValues.result || '',
        date: initialValues.date || '',
        issuingOrg: initialValues.issuingOrg || '',
        rank: initialValues.rank || ''
      });
    }
  }, [initialValues]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(type, formData);
  };

  return (
    <div className="space-y-4">
      <div className="mb-4">
        <label className="block text-sm mb-1">Type</label>
        <select disabled={!!initialValues} className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={type} onChange={e => setType(e.target.value)}>
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
          <div><label className="block text-sm mb-1">Rank</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.rank} onChange={e => setFormData({...formData, rank: e.target.value})} /></div>
        )}
        <div className="flex justify-end space-x-3 mt-6">
          <button type="button" onClick={onCancel} className="px-4 py-2 rounded-2xl bg-surfaceAlt hover:bg-border">Cancel</button>
          <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
        </div>
      </form>
    </div>
  );
}
