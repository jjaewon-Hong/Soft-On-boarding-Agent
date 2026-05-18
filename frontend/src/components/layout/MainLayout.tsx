import { Outlet, Navigate, useLocation } from 'react-router-dom';
import { Sidebar } from './Sidebar';

export function MainLayout() {
  const location = useLocation();

  // Redirect root to /functional by default
  if (location.pathname === '/') {
    return <Navigate to="/functional" replace />;
  }

  return (
    <div className="flex h-screen w-full bg-white overflow-hidden">
      <Sidebar />
      <main className="flex-1 relative overflow-auto bg-dot-pattern">
        <Outlet />
      </main>
    </div>
  );
}
