import { useState, useEffect } from 'react';

export default function ApplicationForm({ initialValues, resumes = [], onSubmit, onCancel }) {
  const [formData, setFormData] = useState({
    company: '', role: '', dateApplied: '', status: 'APPLIED', requiredSkills: '', resumeId: '', jobLink: '', jobDescription: '', notes: ''
  });

  useEffect(() => {
    if (initialValues) {
      setFormData({
        company: initialValues.company || '',
        role: initialValues.role || '',
        dateApplied: initialValues.dateApplied || '',
        status: initialValues.status || 'APPLIED',
        requiredSkills: initialValues.requiredSkills ? initialValues.requiredSkills.join(', ') : '',
        resumeId: initialValues.resumeId || '',
        jobLink: initialValues.jobLink || '',
        jobDescription: initialValues.jobDescription || '',
        notes: initialValues.notes || ''
      });
    }
  }, [initialValues]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({
      ...formData,
      requiredSkills: formData.requiredSkills.split(',').map(s=>s.trim()).filter(Boolean)
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div><label className="block text-sm mb-1">Company</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.company} onChange={e => setFormData({...formData, company: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Role</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.role} onChange={e => setFormData({...formData, role: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Date Applied</label><input required type="date" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.dateApplied} onChange={e => setFormData({...formData, dateApplied: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Job Link</label><input type="url" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.jobLink} onChange={e => setFormData({...formData, jobLink: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Required Skills (comma separated)</label><input type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.requiredSkills} onChange={e => setFormData({...formData, requiredSkills: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Job Description</label><textarea className="w-full p-2 rounded-2xl bg-surfaceAlt border min-h-[80px]" value={formData.jobDescription} onChange={e => setFormData({...formData, jobDescription: e.target.value})} /></div>
      <div><label className="block text-sm mb-1">Notes</label><textarea className="w-full p-2 rounded-2xl bg-surfaceAlt border min-h-[60px]" value={formData.notes} onChange={e => setFormData({...formData, notes: e.target.value})} /></div>
      <div>
        <label className="block text-sm mb-1">Resume Used</label>
        <select required className="w-full p-2 rounded-2xl bg-surfaceAlt border" value={formData.resumeId} onChange={e => setFormData({...formData, resumeId: e.target.value})}>
          <option value="">Select a resume...</option>
          {resumes.map(r => <option key={r.id} value={r.id}>{r.label} ({r.version})</option>)}
        </select>
      </div>
      <div className="flex justify-end space-x-3 mt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 rounded-2xl bg-surfaceAlt hover:bg-border">Cancel</button>
        <button type="submit" className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark">Save</button>
      </div>
    </form>
  );
}
