import { useEffect, useState } from 'react';
import api from '../../services/api';

export default function Users() {
  const [users, setUsers]   = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/users').then(({ data }) => { setUsers(data); setLoading(false); });
  }, []);

  const filtered = users.filter(u =>
    u.username?.toLowerCase().includes(search.toLowerCase()) ||
    u.email?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div>
      <div className="page-header">
        <div><h2>System Users</h2><p>{users.length} users registered</p></div>
      </div>

      <div className="table-wrapper">
        <div className="table-toolbar">
          <input className="search-input" placeholder="Search users…" value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div className="table-scroll">
          <table>
            <thead><tr><th>#</th><th>Username</th><th>Email</th><th>Role</th></tr></thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={4}><div className="state-box"><div className="spinner" /><p>Loading…</p></div></td></tr>
              ) : filtered.length === 0 ? (
                <tr><td colSpan={4}><div className="state-box"><div className="state-icon">🔑</div><p>No users found</p></div></td></tr>
              ) : filtered.map(u => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td><strong>{u.username}</strong></td>
                  <td>{u.email}</td>
                  <td><span className={`badge badge-${u.role?.toLowerCase()}`}>{u.role}</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
