import { useState } from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";
import { FiMenu } from "react-icons/fi";

export default function Layout() {
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <div className="flex h-screen bg-cream overflow-hidden">
      <Sidebar mobileOpen={mobileOpen} setMobileOpen={setMobileOpen} />
      
      <div className="flex-1 flex flex-col h-screen overflow-hidden">
        {/* Mobile Header */}
        <header className="lg:hidden bg-surfaceAlt border-b border-border p-4 flex items-center shadow-sm">
          <button 
            onClick={() => setMobileOpen(true)}
            className="text-ink hover:text-plum p-1 rounded-lg"
          >
            <FiMenu size={24} />
          </button>
          <h1 className="ml-4 text-xl font-bold text-plum">PrepSphere</h1>
        </header>

        {/* Main Content Area */}
        <main className="flex-1 overflow-y-auto p-4 md:p-8">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
