import { useState } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiAward, FiFlag, FiFileText, FiMaximize2 } from 'react-icons/fi';
import { api } from '../api/client';
import DetailModal from '../components/DetailModal';
import AchievementForm from '../components/AchievementForm';

export default function Achievements() {
  const { data: achievements, loading, error, postData, putData, deleteData, fetchData } = useApi('/achievements');
  const [filter, setFilter] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [viewDetail, setViewDetail] = useState(null);

  const filtered = achievements?.filter(a => a.name?.toLowerCase().includes(filter.toLowerCase()));

  const hackathons = filtered?.filter(a => a.type === 'HACKATHON') || [];
  const certifications = filtered?.filter(a => a.type === 'CERTIFICATION') || [];
  const awards = filtered?.filter(a => a.type === 'AWARD') || [];

  const handleSave = async (submittedType, data) => {
    try {
      if (editingRecord) {
        if (submittedType === 'HACKATHON') {
          await putData(`/achievements/hackathon/${editingRecord.id}`, { name: data.name, organizer: data.organizer, result: data.result, date: data.date });
        } else if (submittedType === 'CERTIFICATION') {
          await putData(`/achievements/certification/${editingRecord.id}`, { name: data.name, issuingOrg: data.issuingOrg, date: data.date });
        } else if (submittedType === 'AWARD') {
          await putData(`/achievements/award/${editingRecord.id}`, { competitionName: data.name, rank: data.rank.toString(), date: data.date });
        }
      } else {
        if (submittedType === 'HACKATHON') {
          await postData('/achievements/hackathon', { name: data.name, organizer: data.organizer, result: data.result, date: data.date });
        } else if (submittedType === 'CERTIFICATION') {
          await postData('/achievements/certification', { name: data.name, issuingOrg: data.issuingOrg, date: data.date });
        } else if (submittedType === 'AWARD') {
          await postData('/achievements/award', { competitionName: data.name, rank: data.rank.toString(), date: data.date });
        }
      }
      setShowModal(false);
      setEditingRecord(null);
      fetchData();
    } catch (err) {
      alert(err.message);
    }
  };

  const getIcon = (typeStr) => {
    if (typeStr === 'HACKATHON') return <FiFlag size={24} />;
    if (typeStr === 'CERTIFICATION') return <FiFileText size={24} />;
    return <FiAward size={24} />;
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Achievements</h1>
        <button onClick={() => { setEditingRecord(null); setShowModal(true); }} className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm">
          <FiPlus className="mr-2" /> Add Achievement
        </button>
      </div>

      <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border">
        <input type="text" placeholder="Filter achievements..." className="w-full mb-6 p-3 rounded-2xl bg-surfaceAlt border border-border focus:border-plum focus:outline-none" value={filter} onChange={e => setFilter(e.target.value)} />
        
        {loading ? <div className="text-center py-8">Loading...</div> : filtered?.length === 0 ? <div className="text-center text-inkMuted py-8">No achievements found.</div> : (
          <div className="space-y-8">
            {hackathons.length > 0 && (
              <div>
                <h2 className="text-xl font-bold text-ink mb-4 flex items-center"><FiFlag className="mr-2 text-plum" /> Hackathons</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {hackathons.map(a => (
                    <div key={a.id} className="p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-between items-center group">
                      <div className="flex items-center space-x-4">
                        <div className="p-3 bg-surface text-clay rounded-2xl">{getIcon(a.type)}</div>
                        <div>
                          <h3 className="font-semibold text-lg">{a.name}</h3>
                          <p className="text-sm text-inkMuted">{a.organizer} • {a.result}</p>
                          <p className="text-xs text-inkMuted mt-1">{a.date}</p>
                        </div>
                      </div>
                      <div className="flex space-x-2">
                        <button onClick={() => setViewDetail(a)} className="p-2 text-inkMuted opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-surface hover:text-ink transition-all"><FiMaximize2 size={18} /></button>
                        <button onClick={() => window.confirm("Delete?") && deleteData(`/achievements/${a.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 size={18} /></button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
            
            {certifications.length > 0 && (
              <div>
                <h2 className="text-xl font-bold text-ink mb-4 flex items-center"><FiFileText className="mr-2 text-plum" /> Certifications</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {certifications.map(a => (
                    <div key={a.id} className="p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-between items-center group">
                      <div className="flex items-center space-x-4">
                        <div className="p-3 bg-surface text-clay rounded-2xl">{getIcon(a.type)}</div>
                        <div>
                          <h3 className="font-semibold text-lg">{a.name}</h3>
                          <p className="text-sm text-inkMuted">{a.issuingOrg}</p>
                          <p className="text-xs text-inkMuted mt-1">{a.date}</p>
                        </div>
                      </div>
                      <div className="flex space-x-2">
                        <button onClick={() => setViewDetail(a)} className="p-2 text-inkMuted opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-surface hover:text-ink transition-all"><FiMaximize2 size={18} /></button>
                        <button onClick={() => window.confirm("Delete?") && deleteData(`/achievements/${a.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 size={18} /></button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {awards.length > 0 && (
              <div>
                <h2 className="text-xl font-bold text-ink mb-4 flex items-center"><FiAward className="mr-2 text-plum" /> Competition Awards</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {awards.map(a => (
                    <div key={a.id} className="p-4 rounded-2xl bg-surfaceAlt border border-border flex justify-between items-center group">
                      <div className="flex items-center space-x-4">
                        <div className="p-3 bg-surface text-clay rounded-2xl">{getIcon(a.type)}</div>
                        <div>
                          <h3 className="font-semibold text-lg">{a.name}</h3>
                          <p className="text-sm text-inkMuted">Rank: {a.rank}</p>
                          <p className="text-xs text-inkMuted mt-1">{a.date}</p>
                        </div>
                      </div>
                      <div className="flex space-x-2">
                        <button onClick={() => setViewDetail(a)} className="p-2 text-inkMuted opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-surface hover:text-ink transition-all"><FiMaximize2 size={18} /></button>
                        <button onClick={() => window.confirm("Delete?") && deleteData(`/achievements/${a.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-white rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 size={18} /></button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md overflow-y-auto max-h-screen">
            <h2 className="text-2xl font-bold text-plum mb-4">{editingRecord ? 'Edit Achievement' : 'Add Achievement'}</h2>
            <AchievementForm 
              initialValues={editingRecord} 
              onSubmit={handleSave} 
              onCancel={() => { setShowModal(false); setEditingRecord(null); }} 
            />
          </div>
        </div>
      )}

      {viewDetail && (
        <DetailModal
          title={`${viewDetail.type === 'HACKATHON' ? 'Hackathon' : viewDetail.type === 'CERTIFICATION' ? 'Certification' : 'Award'}: ${viewDetail.name}`}
          fields={[
            { label: 'Name', value: viewDetail.name },
            { label: 'Category', value: viewDetail.type },
            { label: 'Date', value: viewDetail.date },
            { label: 'Organizer / Issuing Org', value: viewDetail.organizer || viewDetail.issuingOrg },
            { label: 'Result / Rank', value: viewDetail.result || (viewDetail.rank ? `Rank ${viewDetail.rank}` : null) }
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
