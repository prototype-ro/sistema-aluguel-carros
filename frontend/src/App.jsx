import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import RegistroCliente from "./pages/RegistroCliente";
import Dashboard from "./pages/Dashboard";
import PerfilCliente from "./pages/PerfilCliente";
import NovoPedido from "./pages/NovoPedido";
import Automoveis from "./pages/Automoveis";
import ProtectedRoute from "./routes/ProtectedRoute";

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/registro" element={<RegistroCliente />} />
      <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/perfil" element={<ProtectedRoute allowedRole="CLIENTE"><PerfilCliente /></ProtectedRoute>} />
      <Route path="/pedidos/novo" element={<ProtectedRoute allowedRole="CLIENTE"><NovoPedido /></ProtectedRoute>} />
      <Route path="/automoveis" element={<ProtectedRoute allowedRole="AGENTE"><Automoveis /></ProtectedRoute>} />
    </Routes>
  );
}
