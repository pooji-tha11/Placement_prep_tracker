import { useState, useEffect } from 'react';

export default function ResumeForm({ initialValues, onSubmit, onCancel }) {
  const [formData, setFormData] = useState({
    label: '', version: '', filename: '', dateAdded: ''
  });
  const [selectedFile, setSelectedFile] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (initialValues) {
      setFormData({
        label: initialValues.label || '',
        version: initialValues.version || '',
        filename: initialValues.filename || '',
        dateAdded: initialValues.dateAdded || ''
      });
    }
  }, [initialValues]);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        setError('File size exceeds 5MB limit.');
        setSelectedFile(null);
        e.target.value = '';
        return;
      }
      if (!['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'].includes(file.type)) {
        setError('Only PDF and DOCX files are allowed.');
        setSelectedFile(null);
        e.target.value = '';
        return;
      }
      setError(null);
      setSelectedFile(file);
      if (!formData.filename) {
        setFormData({ ...formData, filename: file.name });
      }
    } else {
      setSelectedFile(null);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const data = new FormData();
    data.append('label', formData.label);
    data.append('version', formData.version);
    data.append('dateAdded', formData.dateAdded);
    if (selectedFile) {
      data.append('file', selectedFile);
    }
    
    // Pass filename as well in case backend needs it or we just ignore it
    // Wait, the backend has been updated to remove 'filename' from CreateResumeRequest,
    // so we don't need to append 'filename'. Wait, let's check backend ResumeController! 
    // Wait, ResumeController update actually removed filename! No it didn't, the patch updated ResumeController to not need filename.
    onSubmit(data);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <div className="p-3 bg-danger/10 text-danger rounded-xl text-sm">{error}</div>}
      <div><label className="block text-sm font-medium text-ink mb-1">Label</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.label} onChange={e => setFormData({...formData, label: e.target.value})} /></div>
      <div><label className="block text-sm font-medium text-ink mb-1">Version</label><input required type="text" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.version} onChange={e => setFormData({...formData, version: e.target.value})} /></div>
      <div><label className="block text-sm font-medium text-ink mb-1">Date Added</label><input required type="date" className="w-full p-2 rounded-2xl bg-surfaceAlt border border-border" value={formData.dateAdded} onChange={e => setFormData({...formData, dateAdded: e.target.value})} /></div>
      
      <div>
        <label className="block text-sm font-medium text-ink mb-1">Resume File (PDF/DOCX, max 5MB)</label>
        <div className="w-full p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-center items-center">
          <input 
            type="file" 
            accept=".pdf,.docx,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document" 
            onChange={handleFileChange}
            className="w-full text-sm text-inkMuted file:mr-4 file:py-2 file:px-4 file:rounded-xl file:border-0 file:text-sm file:font-semibold file:bg-plum file:text-white hover:file:bg-plumDark cursor-pointer"
          />
        </div>
        {initialValues?.filename && !selectedFile && (
          <p className="text-xs text-inkMuted mt-1">Current file: {initialValues.filename}</p>
        )}
      </div>

      <div className="flex justify-end space-x-3 mt-6">
        <button type="button" onClick={onCancel} className="px-4 py-2 rounded-2xl bg-surfaceAlt text-ink hover:bg-border">Cancel</button>
        <button type="submit" disabled={!!error} className="px-4 py-2 rounded-2xl bg-plum text-white hover:bg-plumDark disabled:opacity-50">Save</button>
      </div>
    </form>
  );
}
