import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import api from '../../services/api';

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const { data } = await api.post('/auth/login', form);
      login(data.token, data.username, data.role);
      navigate('/');
    } catch {
      setError('Invalid username or password. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-left">
        <div className="login-brand">
          <h1>Casa <span>Legal</span> Hub</h1>
          <p>A complete legal case management platform for modern law firms.</p>
          <div className="brand-features">
            <div className="feature"><span>⚖️</span><span>Manage cases end-to-end</span></div>
            <div className="feature"><span>👥</span><span>Track clients & lawyers</span></div>
            <div className="feature"><span>📄</span><span>Secure document storage</span></div>
            <div className="feature"><span>💳</span><span>Payment tracking & reports</span></div>
          </div>
        </div>
      </div>
      <div className="login-right">
        <div className="login-card">
          <div className="logo-mark">⚖️</div>
          <h2>Welcome back</h2>
          <p className="subtitle">Sign in to your account to continue</p>
          {error && <div className="alert-error">{error}</div>}
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Username</label>
              <input
                className="form-control"
                placeholder="Enter your username"
                value={form.username}
                onChange={(e) => setForm({ ...form, username: e.target.value })}
                required
                autoFocus
              />
            </div>
            <div className="form-group">
              <label>Password</label>
              <input
                type="password"
                className="form-control"
                placeholder="Enter your password"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                required
              />
            </div>
            <button type="submit" className="btn-login" disabled={loading}>
              {loading ? 'Signing in…' : 'Sign In'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
