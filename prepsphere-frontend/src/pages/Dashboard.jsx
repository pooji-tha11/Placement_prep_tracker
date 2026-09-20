import { useEffect, useState } from 'react';
import { api } from '../api/client';
import { FiCode, FiBriefcase, FiCalendar } from 'react-icons/fi';

export default function Dashboard() {
  const [readiness, setReadiness] = useState(null);
  const [dsa, setDsa] = useState(null);
  const [apps, setApps] = useState(null);
  const [study, setStudy] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get('/reports/readiness').catch(() => null),
      api.get('/reports/dsa/difficulty').catch(() => null),
      api.get('/reports/applications/status').catch(() => null),
      api.get('/reports/study/summary').catch(() => null)
    ]).then(([r, d, a, s]) => {
      setReadiness(r);
      setDsa(d);
      setApps(a);
      setStudy(s);
    }).finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-8 flex justify-center text-plum font-medium">Loading Dashboard...</div>;

  const totalDsa = dsa ? Object.values(dsa).reduce((a, b) => a + b, 0) : 0;
  const totalApps = apps ? Object.values(apps).reduce((a, b) => a + b, 0) : 0;
  const streak = study?.currentStreak || 0;
  const overall = readiness?.overall || 0;
  const breakdown = readiness?.breakdown || {};

  const radius = 60;
  const circumference = 2 * Math.PI * radius;
  const offset = circumference - (Math.min(overall, 100) / 100) * circumference;

  return (
    <div className="max-w-5xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold text-plum mb-8">Dashboard</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Readiness Score Card */}
        <div className="col-span-1 md:col-span-3 lg:col-span-1 bg-surface p-6 rounded-3xl shadow-sm border border-border flex flex-col items-center justify-center">
          <h2 className="text-xl font-semibold text-ink mb-6">Readiness Score</h2>
          
          <div className="relative flex items-center justify-center mb-6">
            <svg className="transform -rotate-90 w-40 h-40">
              <circle
                cx="80"
                cy="80"
                r={radius}
                stroke="currentColor"
                strokeWidth="12"
                fill="transparent"
                className="text-surfaceAlt"
              />
              <circle
                cx="80"
                cy="80"
                r={radius}
                stroke="currentColor"
                strokeWidth="12"
                fill="transparent"
                strokeDasharray={circumference}
                strokeDashoffset={offset}
                className="text-plum transition-all duration-1000 ease-out"
                strokeLinecap="round"
              />
            </svg>
            <div className="absolute flex flex-col items-center justify-center text-center">
              <span className="text-3xl font-bold text-plum">{Math.round(overall)}</span>
              <span className="text-sm text-inkMuted">/ 100</span>
            </div>
          </div>
          
          <div className="w-full space-y-3">
            {Object.entries(breakdown).map(([key, val]) => (
              <div key={key} className="flex flex-col">
                <div className="flex justify-between text-sm mb-1 text-inkMuted font-medium">
                  <span>{key}</span>
                  <span>{Math.round(val)}</span>
                </div>
                <div className="w-full bg-surfaceAlt h-2 rounded-full overflow-hidden">
                  <div 
                    className="bg-clay h-full rounded-full transition-all duration-1000" 
                    style={{ width: `${Math.min(val, 100)}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Stat Cards */}
        <div className="col-span-1 md:col-span-3 lg:col-span-2 grid grid-cols-1 sm:grid-cols-2 gap-6 content-start">
          <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border flex items-start space-x-4">
            <div className="p-4 bg-plum/10 text-plum rounded-2xl">
              <FiCode size={28} />
            </div>
            <div>
              <p className="text-inkMuted font-medium text-sm">Problems Solved</p>
              <h3 className="text-3xl font-bold text-ink mt-1">{totalDsa}</h3>
            </div>
          </div>
          
          <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border flex items-start space-x-4">
            <div className="p-4 bg-clay/10 text-clay rounded-2xl">
              <FiBriefcase size={28} />
            </div>
            <div>
              <p className="text-inkMuted font-medium text-sm">Total Applications</p>
              <h3 className="text-3xl font-bold text-ink mt-1">{totalApps}</h3>
            </div>
          </div>

          <div className="bg-surface p-6 rounded-3xl shadow-sm border border-border flex items-start space-x-4">
            <div className="p-4 bg-success/20 text-[#6a8756] rounded-2xl">
              <FiCalendar size={28} />
            </div>
            <div>
              <p className="text-inkMuted font-medium text-sm">Current Streak</p>
              <h3 className="text-3xl font-bold text-ink mt-1">{streak} <span className="text-base font-normal text-inkMuted">days</span></h3>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
