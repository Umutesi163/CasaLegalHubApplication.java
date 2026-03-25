import { useEffect, useState } from 'react';
import api from '../../services/api';

const EMPTY = { title: '', description: '', status: 'OPEN', clientId: '', lawyerId: '' };

function StatusBadge({ status }) {
  const cls = { OPEN: 'badge-open', IN_PROGRESS: 'badge-in_progress', CLOSED: 'badge-closed' };
  return <span className={`badge ${cls[status] || 'badge-default'}`}>{status?.replace('_', ' ')}</span>;
}

export default function Cases() {
  const [cases, setCases]     = useState([]);
  const [clients, setClients] = useState([]);
  const [lawyers, setLawyers] = useState([]);
  const [search, setSearch]   = useState('');
  const [loading, setLoading] = useState(true);
  const [modal, setModal]     = useState(false);
  const [form, setForm]       = useState(EMPTY);
  const [saving, setSaving]   = useState(false);

  const load = () => {
    setLoading(true);
    Promise.all([
      api.get('/cases'),
      api.get('/clients'),
      api.get('/lawyers'),
    ]).then(([c, cl, l]) => {
      setCases(c.data);
      setClients(cl.data);
      setLawyers(l.data);
      setLoading(false);
    });
  };

  useEffect(() => { load(); }, []);

  const filtered = cases.filter(c =>
    c.title?.toLowerCase().includes(search.toLowerCase()) ||
    c.clientName?.toLowerCase().includes(search.toLowerCase()) ||
    c.lawyerName?.toLowerCase().includes(search.toLowerCase())
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await api.post('/cases', { ...form, clientId: Number(form.clientId), lawyerId: Number(form.lawyerId) });
      setModal(false);
      setForm(EMPTY);
      load();
    } finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this case?')) return;
    await api.delete(`/cases/${id}`);
    load();
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h2>Legal Cases</h2>
          <p>{cases.length} total cases</p>
        </div>
        <button className="btn btn-primary" onClick={() => setModal(true)}>➕ New Case</button>
      </div>

      <div className="table-wrapper">
        <div className="table-toolbar">
          <input className="search-input" placeholder="Search cases…" value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div className="table-scroll">
          <table>
            <thead>
              <tr><th>#</th><th>Title</th><th>Status</th><th>Client</th><th>Lawyer</th><th>Created</th><th>Actions</th></tr>
            </thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={7}><div className="state-box"><div className="spinner" /><p>Loading…</p></div></td></tr>
              ) : filtered.length === 0 ? (
                <tr><td colSpan={7}><div className="state-box"><div className="state-icon">⚖️</div><p>No cases found</p></div></td></tr>
              ) : filtered.map(c => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td><strong>{c.title}</strong>{c.description && <div style={{color:'var(--text-muted)',fontSize:12}}>{c.description}</div>}</td>
                  <td><StatusBadge status={c.status} /></td>
                  <td>{c.clientName}</td>
                  <td>{c.lawyerName}</td>
                  <td>{c.createdDate}</td>
                  <td><div className="td-actions"><button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id)}>🗑</button></div></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {modal && (
        <div className="modal-overlay" onClick={() => setModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>New Legal Case</h3>
              <button className="modal-close" onClick={() => setModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group"><label>Title</label><input className="form-control" required value={form.title} onChange={e => setForm({...form, title: e.target.value})} /></div>
                <div className="form-group"><label>Description</label><textarea className="form-control" rows={3} value={form.description} onChange={e => setForm({...form, description: e.target.value})} /></div>
                <div className="form-group">
                  <label>Status</label>
                  <select className="form-control" value={form.status} onChange={e => setForm({...form, status: e.target.value})}>
                    <option value="OPEN">Open</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="CLOSED">Closed</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Client</label>
                  <select className="form-control" required value={form.clientId} onChange={e => setForm({...form, clientId: e.target.value})}>
                    <option value="">Select client…</option>
                    {clients.map(c => <option key={c.id} value={c.id}>{c.fullName}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Lawyer</label>
                  <select className="form-control" required value={form.lawyerId} onChange={e => setForm({...form, lawyerId: e.target.value})}>
                    <option value="">Select lawyer…</option>
                    {lawyers.map(l => <option key={l.id} value={l.id}>{l.fullName}</option>)}
                  </select>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving…' : 'Create Case'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
