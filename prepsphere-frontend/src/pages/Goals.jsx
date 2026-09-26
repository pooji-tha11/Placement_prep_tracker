import { useState } from 'react';
import { useApi } from '../hooks/useApi';
import { FiTrash2, FiPlus, FiTarget, FiCheck, FiMaximize2 } from 'react-icons/fi';
import { api } from '../api/client';
import DetailModal from '../components/DetailModal';
import GoalForm from '../components/GoalForm';

export default function Goals() {
  const { data: goals, loading, error, postData, putData, deleteData, fetchData } = useApi('/goals');
  const [showModal, setShowModal] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [viewDetail, setViewDetail] = useState(null);

  const handleSave = async (data) => {
    try {
      if (editingRecord) {
        await putData(`/goals/${editingRecord.id}`, data);
      } else {
        await postData('/goals', data);
      }
      setShowModal(false);
      setEditingRecord(null);
    } catch (err) {
      alert(err.message);
    }
  };

  const updateProgress = async (id, currentCount) => {
    try {
      await api.patch(`/goals/${id}/progress`, { currentCount: parseInt(currentCount, 10) });
      fetchData();
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-plum">Goals</h1>
        <button onClick={() => { setEditingRecord(null); setShowModal(true); }} className="bg-plum hover:bg-plumDark text-white px-4 py-2 rounded-2xl flex items-center shadow-sm">
          <FiPlus className="mr-2" /> Add Goal
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {loading ? <div className="col-span-full text-center py-8">Loading...</div> : goals?.length === 0 ? (
          <div className="col-span-full text-center text-inkMuted py-8">No goals set yet.</div>
        ) : goals?.map(goal => (
          <div key={goal.id} className="p-6 rounded-3xl bg-surface border border-border shadow-sm flex flex-col space-y-4 relative group">
            <div className="absolute top-4 right-4 flex space-x-2">
              <button onClick={() => setViewDetail(goal)} className="p-2 text-inkMuted opacity-0 lg:group-hover:opacity-100 bg-surfaceAlt rounded-full hover:bg-white hover:text-ink transition-all"><FiMaximize2 /></button>
              <button onClick={() => window.confirm("Delete goal?") && deleteData(`/goals/${goal.id}`)} className="p-2 text-danger opacity-0 lg:group-hover:opacity-100 bg-surfaceAlt rounded-full hover:bg-danger hover:text-white transition-all"><FiTrash2 /></button>
            </div>
            <div className="flex items-start space-x-3 pr-8">
              <div className="p-2 bg-clay/20 text-clay rounded-xl"><FiTarget /></div>
              <div>
                <h3 className="font-semibold text-lg">{goal.description}</h3>
                <p className="text-sm text-inkMuted">Deadline: {goal.deadline}</p>
              </div>
            </div>
            
            <div className="space-y-2">
              <div className="flex justify-between text-sm">
                <span>Progress: {goal.currentCount} / {goal.targetCount}</span>
                <span className="font-medium">{Math.round(goal.progressPercentage)}%</span>
              </div>
              <div className="w-full bg-surfaceAlt h-3 rounded-full overflow-hidden">
                <div className={`h-full rounded-full transition-all duration-500 ${goal.achieved ? 'bg-success' : 'bg-clay'}`} style={{ width: `${Math.min(goal.progressPercentage, 100)}%` }} />
              </div>
            </div>

            <div className="flex items-center space-x-2 pt-2">
              <input 
                type="number" 
                min="0" 
                className="w-20 p-2 text-sm rounded-xl bg-surfaceAlt border border-border" 
                defaultValue={goal.currentCount}
                onBlur={(e) => {
                  if (e.target.value !== String(goal.currentCount)) {
                    updateProgress(goal.id, e.target.value);
                  }
                }}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    e.target.blur();
                  }
                }}
              />
              <span className="text-xs text-inkMuted">Update progress & press Enter</span>
            </div>
            {goal.achieved && <div className="absolute -top-3 -right-3 bg-success text-white text-xs px-2 py-1 rounded-full flex items-center shadow-sm border border-success"><FiCheck className="mr-1"/> Achieved</div>}
          </div>
        ))}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4">
          <div className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-md">
            <h2 className="text-2xl font-bold text-plum mb-4">{editingRecord ? 'Edit Goal' : 'Add Goal'}</h2>
            <GoalForm 
              initialValues={editingRecord} 
              onSubmit={handleSave} 
              onCancel={() => { setShowModal(false); setEditingRecord(null); }} 
            />
          </div>
        </div>
      )}

      {viewDetail && (
        <DetailModal
          title="Goal Details"
          fields={[
            { label: 'Description', value: viewDetail.description },
            { label: 'Target Date', value: viewDetail.deadline },
            { label: 'Progress', value: `${viewDetail.currentCount} / ${viewDetail.targetCount} (${Math.round(viewDetail.progressPercentage)}%)` },
            { label: 'Status', value: viewDetail.achieved ? 'Achieved' : 'In Progress' }
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
