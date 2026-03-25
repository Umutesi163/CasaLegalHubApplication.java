import { useEffect, useState } from 'react';
import api from '../../services/api';

export default function AuditLogs() {
  const [logs, setLogs]     = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/audit-logs').then(({ data }) => { setLogs(data); setLoading(false); });
  }, []);

  const filtered = logs.filter(l =>
    l.action?.toLowerCase().includes(search.toLowerCase()) ||
    l.entity?.toLowerCase().includes(search.toLowerCase()) ||
    l.performedBy?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div>
      <div className="page-header">
        <div><h2>Audit Logs</h2><p>{logs.length} log entries</p></div>
      </div>

      <div className="table-wrapper">
        <div className="table-toolbar">
          <input className="search-input" placeholder="Search logs…" value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div className="table-scroll">
          <table>
            <thead><tr><th>#</th><th>Action</th><th>Entity</th><th>Details</th><th>Performed By</th><th>Timestamp</th></tr></thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={6}><div className="state-box"><div className="spinner" /><p>Loading…</p></div></td></tr>
              ) : filtered.length === 0 ? (
                <tr><td colSpan={6}><div className="state-box"><div className="state-icon">📋</div><p>No audit logs found</p></div></td></tr>
              ) : filtered.map(l => (
                <tr key={l.id}>
                  <td>{l.id}</td>
                  <td><span className="badge badge-default">{l.action}</span></td>
                  <td>{l.entity}</td>
                  <td style={{fontSize:12,color:'var(--text-muted)',maxWidth:200}}>{l.details}</td>
                  <td>{l.performedBy}</td>
                  <td>{l.timestamp ? new Date(l.timestamp).toLocaleString() : '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
