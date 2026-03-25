import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import api from '../../services/api';

export default function Dashboard() {
  const { username } = useAuth();
  const [stats, setStats] = useState({ cases: 0, clients: 0, lawyers: 0, documents: 0, payments: 0, users: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.allSettled([
      api.get('/cases'),
      api.get('/clients'),
      api.get('/lawyers'),
      api.get('/documents'),
      api.get('/payments'),
      api.get('/users'),
    ]).then(([cases, clients, lawyers, documents, payments, users]) => {
      setStats({
        cases:     cases.value?.data?.length     ?? 0,
        clients:   clients.value?.data?.length   ?? 0,
        lawyers:   lawyers.value?.data?.length   ?? 0,
        documents: documents.value?.data?.length ?? 0,
        payments:  payments.value?.data?.length  ?? 0,
        users:     users.value?.data?.length     ?? 0,
      });
      setLoading(false);
    });
  }, []);

  const cards = [
    { label: 'Total Cases',     value: stats.cases,     icon: '⚖️',  color: 'blue',   to: '/cases' },
    { label: 'Clients',         value: stats.clients,   icon: '👥',  color: 'green',  to: '/clients' },
    { label: 'Lawyers',         value: stats.lawyers,   icon: '👨⚖️', color: 'purple', to: '/lawyers' },
    { label: 'Documents',       value: stats.documents, icon: '📄',  color: 'yellow', to: '/documents' },
    { label: 'Payments',        value: stats.payments,  icon: '💳',  color: 'teal',   to: '/payments' },
    { label: 'System Users',    value: stats.users,     icon: '🔑',  color: 'red',    to: '/users' },
  ];

  return (
    <div>
      <div className="welcome-banner">
        <div>
          <h2>Welcome back, {username || 'User'} 👋</h2>
          <p>Here's an overview of your legal management system.</p>
        </div>
        <div className="banner-icon">⚖️</div>
      </div>

      <div className="stats-grid">
        {cards.map(({ label, value, icon, color, to }) => (
          <Link to={to} key={label} style={{ textDecoration: 'none' }}>
            <div className="stat-card">
              <div className={`stat-icon ${color}`}>{icon}</div>
              <div className="stat-info">
                <strong>{loading ? '—' : value}</strong>
                <span>{label}</span>
              </div>
            </div>
          </Link>
        ))}
      </div>

      <div className="card">
        <div className="card-body">
          <h3 style={{ marginBottom: 16, color: 'var(--primary)', fontSize: 15 }}>Quick Actions</h3>
          <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
            <Link to="/cases"    className="btn btn-primary">➕ New Case</Link>
            <Link to="/clients"  className="btn btn-outline">👤 Add Client</Link>
            <Link to="/lawyers"  className="btn btn-outline">👨⚖️ Add Lawyer</Link>
            <Link to="/payments" className="btn btn-outline">💳 Record Payment</Link>
          </div>
        </div>
      </div>
    </div>
  );
}
