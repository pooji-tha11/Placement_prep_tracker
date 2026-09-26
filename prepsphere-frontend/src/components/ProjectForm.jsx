import { useState, useEffect } from 'react';

export default function ProjectForm({ initialValues, onSubmit, onCancel }) {
  const [formData, setFormData] = useState({
    title: '', domain: '', techStack: '', repoLink: '', status: 'PLANNED'
  });

  useEffect(() => {
    if (initialValues) {
      setFormData({
        title: initialValues.title || '',
        domain: initialValues.domain || '',
        techStack: initialValues.techStack ? initialValues.techStack.join(', ') : '',
        repoLink: initialValues.repoLink || '',
        status: initialValues.status || 'PLANNED'
      });
    }
  }, [initialValues]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({
      ...formData,
      techStack: formData.techStack.split(',').map(s=>s.trim()).filter(Boolean)
    });
  };

  return (
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
        <button type="button" onClick={onCancel} className="px-4 py-2 rounded-2xl bg-surfaceAlt hover:bg-border">Cancel</button>
        <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
      </div>
    </form>
  );
}
