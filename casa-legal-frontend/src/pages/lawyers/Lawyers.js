import { useEffect, useState } from 'react';
import api from '../../services/api';

const EMPTY = { fullName: '', specialization: '', email: '' };

export default function Lawyers() {
  const [lawyers, setLawyers] = useState([]);
  const [search, setSearch]   = useState('');
  const [loading, setLoading] = useState(true);
  const [modal, setModal]     = useState(false);
  const [form, setForm]       = useState(EMPTY);
  const [saving, setSaving]   = useState(false);

  const load = () => {
    setLoading(true);
    api.get('/lawyers').then(({ data }) => { setLawyers(data); setLoading(false); });
  };

  useEffect(() => { load(); }, []);

  const filtered = lawyers.filter(l =>
    l.fullName?.toLowerCase().includes(search.toLowerCase()) ||
    l.specialization?.toLowerCase().includes(search.toLowerCase())
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await api.post('/lawyers', form);
      setModal(false); setForm(EMPTY); load();
    } finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this lawyer?')) return;
    await api.delete(`/lawyers/${id}`);
    load();
  };

  return (
    <div>
      <div className="page-header">
        <div><h2>Lawyers</h2><p>{lawyers.length} registered lawyers</p></div>
        <button className="btn btn-primary" onClick={() => setModal(true)}>➕ Add Lawyer</button>
      </div>

      <div className="table-wrapper">
        <div className="table-toolbar">
          <input className="search-input" placeholder="Search lawyers…" value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div className="table-scroll">
          <table>
            <thead><tr><th>#</th><th>Full Name</th><th>Specialization</th><th>Email</th><th>Actions</th></tr></thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={5}><div className="state-box"><div className="spinner" /><p>Loading…</p></div></td></tr>
              ) : filtered.length === 0 ? (
                <tr><td colSpan={5}><div className="state-box"><div className="state-icon">👨‍⚖️</div><p>No lawyers found</p></div></td></tr>
              ) : filtered.map(l => (
                <tr key={l.id}>
                  <td>{l.id}</td>
                  <td><strong>{l.fullName}</strong></td>
                  <td><span className="badge badge-default">{l.specialization}</span></td>
                  <td>{l.email}</td>
                  <td><div className="td-actions"><button className="btn btn-danger btn-sm" onClick={() => handleDelete(l.id)}>🗑</button></div></td>
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
              <h3>Add Lawyer</h3>
              <button className="modal-close" onClick={() => setModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group"><label>Full Name</label><input className="form-control" required value={form.fullName} onChange={e => setForm({...form, fullName: e.target.value})} /></div>
                <div className="form-group"><label>Specialization</label><input className="form-control" required value={form.specialization} onChange={e => setForm({...form, specialization: e.target.value})} /></div>
                <div className="form-group"><label>Email</label><input type="email" className="form-control" required value={form.email} onChange={e => setForm({...form, email: e.target.value})} /></div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving…' : 'Add Lawyer'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
