import { useEffect, useState } from 'react';
import api from '../../services/api';

const EMPTY = { amount: '', paymentType: '', description: '', caseId: '', clientId: '', lawyerId: '' };

export default function Payments() {
  const [payments, setPayments] = useState([]);
  const [cases, setCases]       = useState([]);
  const [clients, setClients]   = useState([]);
  const [lawyers, setLawyers]   = useState([]);
  const [search, setSearch]     = useState('');
  const [loading, setLoading]   = useState(true);
  const [modal, setModal]       = useState(false);
  const [form, setForm]         = useState(EMPTY);
  const [saving, setSaving]     = useState(false);

  const load = () => {
    setLoading(true);
    Promise.all([
      api.get('/payments'),
      api.get('/cases'),
      api.get('/clients'),
      api.get('/lawyers'),
    ]).then(([p, c, cl, l]) => {
      setPayments(p.data); setCases(c.data); setClients(cl.data); setLawyers(l.data);
      setLoading(false);
    });
  };

  useEffect(() => { load(); }, []);

  const filtered = payments.filter(p =>
    p.paymentType?.toLowerCase().includes(search.toLowerCase()) ||
    p.description?.toLowerCase().includes(search.toLowerCase())
  );

  const total = payments.reduce((sum, p) => sum + (p.amount || 0), 0);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await api.post('/payments', {
        ...form,
        amount: Number(form.amount),
        caseId: Number(form.caseId),
        clientId: Number(form.clientId),
        lawyerId: Number(form.lawyerId),
      });
      setModal(false); setForm(EMPTY); load();
    } finally { setSaving(false); }
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h2>Payments</h2>
          <p>{payments.length} records · Total: <strong>${total.toLocaleString()}</strong></p>
        </div>
        <button className="btn btn-primary" onClick={() => setModal(true)}>➕ Record Payment</button>
      </div>

      <div className="table-wrapper">
        <div className="table-toolbar">
          <input className="search-input" placeholder="Search payments…" value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div className="table-scroll">
          <table>
            <thead><tr><th>#</th><th>Amount</th><th>Type</th><th>Description</th><th>Date</th></tr></thead>
            <tbody>
              {loading ? (
                <tr><td colSpan={5}><div className="state-box"><div className="spinner" /><p>Loading…</p></div></td></tr>
              ) : filtered.length === 0 ? (
                <tr><td colSpan={5}><div className="state-box"><div className="state-icon">💳</div><p>No payments found</p></div></td></tr>
              ) : filtered.map(p => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td><strong style={{color:'var(--success)'}}>$ {Number(p.amount).toLocaleString()}</strong></td>
                  <td><span className="badge badge-default">{p.paymentType}</span></td>
                  <td>{p.description}</td>
                  <td>{p.paymentDate ? new Date(p.paymentDate).toLocaleDateString() : '—'}</td>
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
              <h3>Record Payment</h3>
              <button className="modal-close" onClick={() => setModal(false)}>×</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group"><label>Amount ($)</label><input type="number" step="0.01" className="form-control" required value={form.amount} onChange={e => setForm({...form, amount: e.target.value})} /></div>
                <div className="form-group"><label>Payment Type</label><input className="form-control" required value={form.paymentType} onChange={e => setForm({...form, paymentType: e.target.value})} /></div>
                <div className="form-group"><label>Description</label><input className="form-control" value={form.description} onChange={e => setForm({...form, description: e.target.value})} /></div>
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
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving…' : 'Record Payment'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
