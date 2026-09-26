import { useState } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiFileText, FiMaximize2 } from 'react-icons/fi';
import DetailModal from '../components/DetailModal';
import ResumeForm from '../components/ResumeForm';

export default function Resumes() {
  const { data: resumes, loading, error, postData, putData, deleteData } = useApi('/resumes');
  const [filter, setFilter] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [viewDetail, setViewDetail] = useState(null);
  const [isUploading, setIsUploading] = useState(false);

  const filteredResumes = resumes?.filter(r => 
    r.label.toLowerCase().includes(filter.toLowerCase()) || 
    r.version.toLowerCase().includes(filter.toLowerCase())
  );

  const handleSave = async (data) => {
    setIsUploading(true);
    try {
      if (editingRecord) {
        await putData(`/resumes/${editingRecord.id}`, data);
      } else {
        await postData('/resumes', data);
      }
      setShowModal(false);
      setEditingRecord(null);
    } catch (err) {
      alert(err.message);
    } finally {
      setIsUploading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this resume?")) {
      try {
        await deleteData(`/resumes/${id}`);
      } catch (err) {
        alert(err.message);
      }
    }
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Resumes</h1>
        <button 
          onClick={() => { setEditingRecord(null); setShowModal(true); }}
          className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm transition-colors"
        >
          <FiPlus className="mr-2" /> Add Resume
        </button>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border">
        <input 
          type="text" 
          placeholder="Filter resumes by label or version..." 
          className="w-full mb-6 p-3 rounded-2xl bg-surfaceAlt border border-border focus:outline-none focus:border-plum text-ink"
          value={filter}
          onChange={e => setFilter(e.target.value)}
        />

        {loading ? (
          <div className="text-center text-plum py-8">Loading...</div>
        ) : error ? (
          <div className="text-center text-danger py-8">{error}</div>
        ) : filteredResumes?.length === 0 ? (
          <div className="text-center text-inkMuted py-8">No resumes found. Add your first one!</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {filteredResumes?.map(resume => (
              <div key={resume.id} className="p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-between items-center group">
                <div className="flex items-center space-x-4">
                  <div className="p-3 bg-surface text-clay rounded-2xl">
                    <FiFileText size={24} />
                  </div>
                  <div>
                    <h3 className="font-semibold text-ink text-lg">{resume.label}</h3>
                    <p className="text-sm text-inkMuted">{resume.filename} • {resume.version}</p>
                    <p className="text-xs text-inkMuted mt-1">Added: {resume.dateAdded}</p>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <button onClick={() => window.open(`http://localhost:8080/api/resumes/${resume.id}/file`, '_blank')} className="p-2 text-inkMuted opacity-0 lg:group-hover:opacity-100 transition-opacity bg-white hover:bg-surface hover:text-ink rounded-full" title="Download"><FiFileText size={18} /></button>
                  <button onClick={() => setViewDetail(resume)} className="p-2 text-inkMuted opacity-0 lg:group-hover:opacity-100 transition-opacity bg-white hover:bg-surface hover:text-ink rounded-full" title="View Details"><FiMaximize2 size={18} /></button>
                  <button 
                    onClick={() => handleDelete(resume.id)}
                    className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 transition-opacity bg-white hover:bg-danger hover:text-white rounded-full"
                    title="Delete"
                  >
                    <FiTrash2 size={18} />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md">
            <h2 className="text-2xl font-bold text-plum mb-4">{editingRecord ? 'Edit Resume' : 'Add Resume'}</h2>
            {isUploading && <div className="mb-4 p-3 bg-clay/20 text-clay rounded-xl text-center">Uploading file, please wait...</div>}
            <ResumeForm 
              initialValues={editingRecord} 
              onSubmit={handleSave} 
              onCancel={() => { setShowModal(false); setEditingRecord(null); }} 
            />
          </div>
        </div>
      )}

      {viewDetail && (
        <DetailModal
          title={`Resume: ${viewDetail.label}`}
          fields={[
            { label: 'Label', value: viewDetail.label },
            { label: 'Version', value: viewDetail.version },
            { label: 'Filename', value: viewDetail.filename },
            { label: 'Date Added', value: viewDetail.dateAdded },
          ]}
          actions={
            <button 
              onClick={() => { setEditingRecord(viewDetail); setShowModal(true); setViewDetail(null); }} 
              className="px-4 py-2 bg-plum text-white rounded-2xl hover:bg-plumDark"
            >
              Edit
            </button>
          }
          onClose={() => setViewDetail(null)}
        />
      )}
    </div>
  );
}
