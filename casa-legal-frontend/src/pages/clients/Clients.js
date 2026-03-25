import { useEffect, useState } from 'react';
import api from '../../services/api';

const EMPTY = { fullName: '', email: '', phone: '' };

export default function Clients() {
  const [clients, setClients] = useState([]);
  const [search, setSearch]   = useState('');
  const [loading, setLoading] = useState(true);
  const [modal, setModal]     = useState(false);
  const [form, setForm]       = useState(EMPTY);
  const [saving, setSaving]   = useState(false);

  const load = () => {
    setLoading(true);
    api.get('/clients').then(({ data }) => { setClients(data); setLoading(false); });
  };

  useEffect(() => { load(); }, []);

  const filtered = clients.filter(c =>
    c.fullName?.toLowerCase().includes(search.toLowerCase()) ||
    c.email?.toLowerCase().includes(search.toLowerCase())
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await api.post('/clients', form);
      setModal(false); setForm(EMPTY); load();
    } finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this client?')) return;
    await api.delete(`/clients/${id}`);
    load();
  };

  return (
    <div>
      <div className="page-header">
        <div><h2>Clients</h2><p>{clients.length} registered clients</p></div>
        <button className="btn btn-primary" onClick={() => setModal(true)}>➕ Add Client</button>
      </div>

      <div className="table-wrapper">
        <div className="table-toolbar">
          <input className="search-input" placeholder="Search clients…" value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div className="table-scroll">
          <table>
            <thead><tr><th>#</th><th>Full Name</th><th>Email</th><th>Phone</th><th>Actions</th></tr></thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={5}><div className="state-box"><div className="spinner" /><p>Loading…</p></div></td></tr>
              ) : filtered.length === 0 ? (
                <tr><td colSpan={5}><div className="state-box"><div className="state-icon">👥</div><p>No clients found</p></div></td></tr>
              ) : filtered.map(c => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td><strong>{c.fullName}</strong></td>
                  <td>{c.email}</td>
                  <td>{c.phone}</td>
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
              <h3>Add Client</h3>
              <button className="modal-close" onClick={() => setModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group"><label>Full Name</label><input className="form-control" required value={form.fullName} onChange={e => setForm({...form, fullName: e.target.value})} /></div>
                <div className="form-group"><label>Email</label><input type="email" className="form-control" required value={form.email} onChange={e => setForm({...form, email: e.target.value})} /></div>
                <div className="form-group"><label>Phone</label><input className="form-control" value={form.phone} onChange={e => setForm({...form, phone: e.target.value})} /></div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving…' : 'Add Client'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
