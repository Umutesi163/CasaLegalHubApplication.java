import { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

const getStored = (key) => localStorage.getItem(key);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(getStored('token'));
  const [username, setUsername] = useState(getStored('username'));
  const [role, setRole] = useState(getStored('role'));

  const login = (jwt, user, userRole) => {
    localStorage.setItem('token', jwt);
    localStorage.setItem('username', user);
    localStorage.setItem('role', userRole);
    setToken(jwt);
    setUsername(user);
    setRole(userRole);
  };

  const logout = () => {
    ['token', 'username', 'role'].forEach((k) => localStorage.removeItem(k));
    setToken(null);
    setUsername(null);
    setRole(null);
  };

  return (
    <AuthContext.Provider value={{ token, username, role, login, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
