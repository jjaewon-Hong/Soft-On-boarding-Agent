import { Server, Users, Shield, Database, Settings } from 'lucide-react';

export function FunctionalView() {
  return (
    <div className="w-full h-full flex items-center justify-center p-8">
      {/* Temporary Placeholder for the Diagram */}
      <div className="relative w-full max-w-4xl h-[600px] flex items-center justify-center">
        
        {/* API Gateway Node */}
        <div className="absolute top-10 left-1/2 -translate-x-1/2 flex flex-col items-center justify-center w-48 py-4 bg-white rounded-xl shadow-[0_2px_8px_rgba(0,0,0,0.08)] border border-gray-100">
          <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center mb-2">
            <Server className="w-5 h-5 text-gray-700" />
          </div>
          <span className="text-sm font-medium text-gray-800">API Gateway</span>
        </div>

        {/* User Service Node */}
        <div className="absolute top-64 left-1/4 -translate-x-1/2 flex flex-col items-center justify-center w-48 py-4 bg-white rounded-xl shadow-[0_2px_8px_rgba(0,0,0,0.08)] border border-gray-100">
          <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center mb-2">
            <Users className="w-5 h-5 text-gray-700" />
          </div>
          <span className="text-sm font-medium text-gray-800">User Service</span>
        </div>

        {/* Auth Module Node */}
        <div className="absolute top-64 right-1/4 translate-x-1/2 flex flex-col items-center justify-center w-48 py-4 bg-white rounded-xl shadow-[0_8px_16px_rgba(0,0,0,0.06)] border border-blue-50 z-10">
          {/* Handover Badge */}
          <div className="absolute -top-3 right-4 bg-gray-200 text-gray-700 text-[10px] font-semibold px-2 py-0.5 rounded-full flex items-center gap-1">
            <Settings className="w-3 h-3" /> Handover
          </div>
          <div className="w-10 h-10 rounded-full bg-blue-50 flex items-center justify-center mb-2">
            <Shield className="w-5 h-5 text-blue-600" />
          </div>
          <span className="text-sm font-medium text-gray-900">Auth Module</span>
        </div>

        {/* Core Database Node */}
        <div className="absolute bottom-10 left-1/2 -translate-x-1/2 flex flex-col items-center justify-center w-48 py-4 bg-white rounded-xl shadow-[0_2px_8px_rgba(0,0,0,0.08)] border border-gray-100">
          <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center mb-2">
            <Database className="w-5 h-5 text-gray-700" />
          </div>
          <span className="text-sm font-medium text-gray-800">Core Database</span>
        </div>

        {/* Connecting Lines (CSS representation for placeholder) */}
        {/* In a real implementation, React Flow or SVG should be used here */}
        <svg className="absolute inset-0 w-full h-full pointer-events-none" style={{ zIndex: 0 }}>
          {/* API to Auth - Solid Line */}
          <line x1="50%" y1="120" x2="70%" y2="256" stroke="#9CA3AF" strokeWidth="1.5" />
          
          {/* API to User - Dashed Line */}
          <line x1="50%" y1="120" x2="30%" y2="256" stroke="#D1D5DB" strokeWidth="1.5" strokeDasharray="4 4" />
          
          {/* User to DB - Dashed Line */}
          <line x1="30%" y1="336" x2="50%" y2="480" stroke="#D1D5DB" strokeWidth="1.5" strokeDasharray="4 4" />
          
          {/* Auth to DB - Dashed Line */}
          <line x1="70%" y1="336" x2="50%" y2="480" stroke="#D1D5DB" strokeWidth="1.5" strokeDasharray="4 4" />
        </svg>
      </div>
    </div>
  );
}
