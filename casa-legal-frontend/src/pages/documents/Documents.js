import { useEffect, useRef, useState } from 'react';
import api from '../../services/api';

const EMPTY = { title: '', documentType: '', caseId: '', clientId: '', lawyerId: '', file: null };

export default function Documents() {
  const [docs, setDocs]       = useState([]);
  const [cases, setCases]     = useState([]);
  const [clients, setClients] = useState([]);
  const [lawyers, setLawyers] = useState([]);
  const [search, setSearch]   = useState('');
  const [loading, setLoading] = useState(true);
  const [modal, setModal]     = useState(false);
  const [form, setForm]       = useState(EMPTY);
  const [saving, setSaving]   = useState(false);
  const fileRef = useRef();

  const load = () => {
    setLoading(true);
    Promise.all([
      api.get('/documents'),
      api.get('/cases'),
      api.get('/clients'),
      api.get('/lawyers'),
    ]).then(([d, c, cl, l]) => {
      setDocs(d.data); setCases(c.data); setClients(cl.data); setLawyers(l.data);
      setLoading(false);
    });
  };

  useEffect(() => { load(); }, []);

  const filtered = docs.filter(d =>
    d.title?.toLowerCase().includes(search.toLowerCase()) ||
    d.documentType?.toLowerCase().includes(search.toLowerCase())
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.file) return;
    setSaving(true);
    try {
      const fd = new FormData();
      fd.append('title', form.title);
      fd.append('documentType', form.documentType);
      fd.append('caseId', form.caseId);
      fd.append('clientId', form.clientId);
      fd.append('lawyerId', form.lawyerId);
      fd.append('file', form.file);
      await api.post('/documents', fd, { headers: { 'Content-Type': 'multipart/form-data' } });
      setModal(false); setForm(EMPTY); load();
    } finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this document?')) return;
    await api.delete(`/documents/${id}`);
    load();
  };

  const handleDownload = (id, fileName) => {
    api.get(`/documents/${id}/download`, { responseType: 'blob' }).then(({ data }) => {
      const url = URL.createObjectURL(data);
      const a = document.createElement('a');
      a.href = url; a.download = fileName; a.click();
      URL.revokeObjectURL(url);
    });
  };

  return (
    <div>
      <div className="page-header">
        <div><h2>Documents</h2><p>{docs.length} documents stored</p></div>
        <button className="btn btn-primary" onClick={() => setModal(true)}>➕ Upload Document</button>
      </div>

      <div className="table-wrapper">
        <div className="table-toolbar">
          <input className="search-input" placeholder="Search documents…" value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div className="table-scroll">
          <table>
            <thead><tr><th>#</th><th>Title</th><th>Type</th><th>File</th><th>Uploaded At</th><th>Actions</th></tr></thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={6}><div className="state-box"><div className="spinner" /><p>Loading…</p></div></td></tr>
              ) : filtered.length === 0 ? (
                <tr><td colSpan={6}><div className="state-box"><div className="state-icon">📄</div><p>No documents found</p></div></td></tr>
              ) : filtered.map(d => (
                <tr key={d.id}>
                  <td>{d.id}</td>
                  <td><strong>{d.title}</strong></td>
                  <td><span className="badge badge-default">{d.documentType}</span></td>
                  <td style={{fontSize:12,color:'var(--text-muted)'}}>{d.fileName}</td>
                  <td>{d.uploadedAt ? new Date(d.uploadedAt).toLocaleDateString() : '—'}</td>
                  <td>
                    <div className="td-actions">
                      <button className="btn btn-outline btn-sm" onClick={() => handleDownload(d.id, d.fileName)}>⬇</button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(d.id)}>🗑</button>
                    </div>
                  </td>
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
              <h3>Upload Document</h3>
              <button className="modal-close" onClick={() => setModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group"><label>Title</label><input className="form-control" required value={form.title} onChange={e => setForm({...form, title: e.target.value})} /></div>
                <div className="form-group"><label>Document Type</label><input className="form-control" required value={form.documentType} onChange={e => setForm({...form, documentType: e.target.value})} /></div>
                <div className="form-group">
                  <label>Case</label>
                  <select className="form-control" required value={form.caseId} onChange={e => setForm({...form, caseId: e.target.value})}>
                    <option value="">Select case…</option>
                    {cases.map(c => <option key={c.id} value={c.id}>{c.title}</option>)}
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
                <div className="form-group">
                  <label>File</label>
                  <input ref={fileRef} type="file" className="form-control" required onChange={e => setForm({...form, file: e.target.files[0]})} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Uploading…' : 'Upload'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
