import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Login from '../pages/auth/Login';
import Dashboard from '../pages/dashboard/Dashboard';
import Cases from '../pages/cases/Cases';
import Clients from '../pages/clients/Clients';
import Lawyers from '../pages/lawyers/Lawyers';
import Documents from '../pages/documents/Documents';
import Payments from '../pages/payments/Payments';
import Users from '../pages/users/Users';
import AuditLogs from '../pages/audit/AuditLogs';

function Private({ children }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" replace />;
}

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/"           element={<Private><Dashboard /></Private>} />
      <Route path="/cases"      element={<Private><Cases /></Private>} />
      <Route path="/clients"    element={<Private><Clients /></Private>} />
      <Route path="/lawyers"    element={<Private><Lawyers /></Private>} />
      <Route path="/documents"  element={<Private><Documents /></Private>} />
      <Route path="/payments"   element={<Private><Payments /></Private>} />
      <Route path="/users"      element={<Private><Users /></Private>} />
      <Route path="/audit-logs" element={<Private><AuditLogs /></Private>} />
      <Route path="*"           element={<Navigate to="/" replace />} />
    </Routes>
  );
}
