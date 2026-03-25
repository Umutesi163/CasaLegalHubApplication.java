import { useState } from 'react';
import { BrowserRouter, NavLink, useLocation } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import AppRoutes from './routes/AppRoutes';
import './App.css';

const NAV_ITEMS = [
  { label: 'Dashboard',  path: '/',           icon: '🏠' },
  { label: 'Cases',      path: '/cases',       icon: '⚖️' },
  { label: 'Clients',    path: '/clients',     icon: '👥' },
  { label: 'Lawyers',    path: '/lawyers',     icon: '👨‍⚖️' },
  { label: 'Documents',  path: '/documents',   icon: '📄' },
  { label: 'Payments',   path: '/payments',    icon: '💳' },
  { label: 'Users',      path: '/users',       icon: '🔑' },
  { label: 'Audit Logs', path: '/audit-logs',  icon: '📋' },
];

const PAGE_TITLES = {
  '/':            'Dashboard',
  '/cases':       'Legal Cases',
  '/clients':     'Clients',
  '/lawyers':     'Lawyers',
  '/documents':   'Documents',
  '/payments':    'Payments',
  '/users':       'Users',
  '/audit-logs':  'Audit Logs',
};

function Sidebar({ open, onClose }) {
  const { username, role, logout } = useAuth();
  return (
    <>
      <div className={`sidebar-overlay ${open ? 'open' : ''}`} onClick={onClose} />
      <aside className={`sidebar ${open ? 'open' : ''}`}>
        <div className="sidebar-logo">
          <h1>⚖️ Casa Legal Hub</h1>
          <span>Legal Management System</span>
        </div>
        <nav className="sidebar-nav">
          <div className="nav-section-label">Menu</div>
          {NAV_ITEMS.map(({ label, path, icon }) => (
            <NavLink
              key={path}
              to={path}
              end={path === '/'}
              className={({ isActive }) => isActive ? 'active' : ''}
              onClick={onClose}
            >
              <span className="nav-icon">{icon}</span>
              {label}
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-footer">
          <div className="user-info">
            <div className="user-avatar">{username ? username[0].toUpperCase() : 'U'}</div>
            <div className="user-details">
              <strong>{username || 'User'}</strong>
              <small>{role || ''}</small>
            </div>
          </div>
          <button className="btn-logout" onClick={logout}>🚪 Logout</button>
        </div>
      </aside>
    </>
  );
}

function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const title = PAGE_TITLES[location.pathname] || 'Casa Legal Hub';

  if (!isAuthenticated) return <AppRoutes />;

  return (
    <div className="app-shell">
      <Sidebar open={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      <div className="main-content">
        <header className="topbar">
          <button className="hamburger" onClick={() => setSidebarOpen(true)}>☰</button>
          <span className="topbar-title">{title}</span>
        </header>
        <div className="page-body">
          <AppRoutes />
        </div>
      </div>
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Layout />
      </AuthProvider>
    </BrowserRouter>
  );
}
