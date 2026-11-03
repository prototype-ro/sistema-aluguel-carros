import { createContext, useContext, useState } from "react";
import api from "../api/client";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [role, setRole] = useState(localStorage.getItem("role"));

  async function login(login, senha) {
    const { data } = await api.post("/auth/login", { login, senha });
    localStorage.setItem("token", data.token);
    localStorage.setItem("role", data.role);
    setToken(data.token);
    setRole(data.role);
  }

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    setToken(null);
    setRole(null);
  }

  return (
    <AuthContext.Provider value={{ token, role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
