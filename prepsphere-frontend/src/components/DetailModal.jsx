import { useEffect } from 'react';
import { FiX } from 'react-icons/fi';

export default function DetailModal({ title, fields, actions, onClose }) {
  useEffect(() => {
    const handleEsc = (e) => {
      if (e.key === 'Escape') onClose();
    };
    window.addEventListener('keydown', handleEsc);
    return () => window.removeEventListener('keydown', handleEsc);
  }, [onClose]);

  return (
    <div className="fixed inset-0 bg-ink/20 flex items-center justify-center z-50 p-4" onClick={onClose}>
      <div 
        className="bg-surface p-6 rounded-3xl shadow-lg border border-border w-full max-w-2xl max-h-[80vh] overflow-y-auto relative"
        onClick={(e) => e.stopPropagation()}
      >
        <button 
          onClick={onClose} 
          className="absolute top-6 right-6 p-2 rounded-full hover:bg-surfaceAlt text-inkMuted hover:text-ink transition-colors"
        >
          <FiX size={20} />
        </button>
        <h2 className="text-2xl font-bold text-plum mb-6 pr-8">{title}</h2>
        <div className="space-y-4">
          {fields.map((field, i) => field.value ? (
            <div key={i} className="p-4 bg-surfaceAlt rounded-2xl border border-border">
              <h3 className="font-bold text-ink mb-2">{field.label}</h3>
              {typeof field.value === 'string' ? (
                <p className="text-inkMuted text-sm whitespace-pre-wrap">{field.value}</p>
              ) : (
                <div className="text-inkMuted text-sm">{field.value}</div>
              )}
            </div>
          ) : null)}
        </div>
        {actions && (
          <div className="flex justify-end mt-6 space-x-3">
            {actions}
          </div>
        )}
      </div>
    </div>
  );
}
