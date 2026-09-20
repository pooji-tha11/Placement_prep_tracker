import { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import { 
  FiHome, FiCode, FiFolder, FiFileText, 
  FiBriefcase, FiCalendar, FiAward, 
  FiTarget, FiClock, FiMenu, FiX, FiChevronLeft, FiChevronRight
} from "react-icons/fi";

const navItems = [
  { path: "/", label: "Dashboard", icon: FiHome },
  { path: "/dsa", label: "DSA Tracker", icon: FiCode },
  { path: "/projects", label: "Projects", icon: FiFolder },
  { path: "/resumes", label: "Resumes", icon: FiFileText },
  { path: "/applications", label: "Applications", icon: FiBriefcase },
  { path: "/study", label: "Study Streaks", icon: FiCalendar },
  { path: "/achievements", label: "Achievements", icon: FiAward },
  { path: "/goals", label: "Goals", icon: FiTarget },
  { path: "/focus", label: "Focus Timer", icon: FiClock },
];

export default function Sidebar({ mobileOpen, setMobileOpen }) {
  const [collapsed, setCollapsed] = useState(() => {
    return localStorage.getItem("sidebar_collapsed") === "true";
  });
  const location = useLocation();

  useEffect(() => {
    localStorage.setItem("sidebar_collapsed", collapsed);
  }, [collapsed]);

  const toggleCollapsed = () => setCollapsed(!collapsed);
  
  const content = (
    <div className={`h-full bg-surfaceAlt border-r border-border flex flex-col transition-all duration-300 ${collapsed ? 'w-20' : 'w-64'}`}>
      <div className="p-4 flex items-center justify-between border-b border-border">
        {!collapsed && <h1 className="text-xl font-bold text-plum">PrepSphere</h1>}
        {collapsed && <div className="text-xl font-bold text-plum mx-auto">P</div>}
        
        {/* Desktop collapse toggle */}
        <button 
          onClick={toggleCollapsed} 
          className="hidden lg:block text-inkMuted hover:text-plum p-1 rounded-full hover:bg-surface transition-colors"
        >
          {collapsed ? <FiChevronRight /> : <FiChevronLeft />}
        </button>
        
        {/* Mobile close button */}
        <button 
          onClick={() => setMobileOpen(false)} 
          className="lg:hidden text-inkMuted hover:text-plum p-1"
        >
          <FiX size={20} />
        </button>
      </div>
      
      <nav className="flex-1 py-4 overflow-y-auto">
        <ul className="space-y-1 px-2">
          {navItems.map((item) => {
            const active = location.pathname === item.path;
            const Icon = item.icon;
            return (
              <li key={item.path}>
                <Link
                  to={item.path}
                  onClick={() => setMobileOpen(false)}
                  className={`flex items-center p-3 rounded-2xl transition-colors group relative ${
                    active 
                      ? 'bg-plum text-white' 
                      : 'text-inkMuted hover:bg-surface hover:text-plum'
                  }`}
                >
                  <Icon size={20} className={collapsed ? "mx-auto" : "mr-4"} />
                  {!collapsed && <span className="font-medium whitespace-nowrap">{item.label}</span>}
                  
                  {/* Tooltip for collapsed state */}
                  {collapsed && (
                    <div className="absolute left-full ml-2 px-3 py-1 bg-surface text-ink text-sm rounded-2xl shadow-sm border border-border opacity-0 group-hover:opacity-100 pointer-events-none whitespace-nowrap z-50">
                      {item.label}
                    </div>
                  )}
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>
    </div>
  );

  return (
    <>
      {/* Mobile overlay */}
      {mobileOpen && (
        <div 
          className="fixed inset-0 bg-ink/20 z-40 lg:hidden"
          onClick={() => setMobileOpen(false)}
        />
      )}
      
      {/* Sidebar container */}
      <aside className={`fixed lg:static inset-y-0 left-0 z-50 transform transition-transform duration-300 ${
        mobileOpen ? "translate-x-0" : "-translate-x-full lg:translate-x-0"
      }`}>
        {content}
      </aside>
    </>
  );
}
