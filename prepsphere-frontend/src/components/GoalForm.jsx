import { useState, useEffect } from 'react';

export default function GoalForm({ initialValues, onSubmit, onCancel }) {
  const [formData, setFormData] = useState({
    description: '', targetCount: '', deadline: ''
  });

  useEffect(() => {
    if (initialValues) {
      setFormData({
        description: initialValues.description || '',
        targetCount: initialValues.targetCount?.toString() || '',
        deadline: initialValues.deadline || ''
      });
    }
  }, [initialValues]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({
      ...formData,
      targetCount: parseInt(formData.targetCount, 10)
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div><label className="block text-sm mb-1">Description</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.description} onChange={e => setFormData({...formData, description: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Target Count</label><input required type="number" min="1" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.targetCount} onChange={e => setFormData({...formData, targetCount: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Deadline</label><input required type="date" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.deadline} onChange={e => setFormData({...formData, deadline: e.target.value})} /></div>
      <div className="flex justify-end space-x-3 mt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 rounded-2xl bg-surfaceAlt hover:bg-border">Cancel</button>
        <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
      </div>
    </form>
  );
}
