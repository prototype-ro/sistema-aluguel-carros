import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/client";
import { useAuth } from "../context/AuthContext";

export default function Dashboard() {
  const { role, logout } = useAuth();
  const [pedidos, setPedidos] = useState([]);

  useEffect(() => {
    const endpoint = role === "AGENTE" ? "/pedidos/pendentes" : "/pedidos/meus";
    api.get(endpoint).then((res) => setPedidos(res.data));
  }, [role]);

  async function avaliar(id, aprovado) {
    await api.put(`/pedidos/avaliar/${id}`, { aprovado });
    setPedidos((prev) => prev.filter((p) => p.id !== id));
  }

  async function cancelar(id) {
    await api.delete(`/pedidos/${id}`);
    setPedidos((prev) => prev.map((p) => (p.id === id ? { ...p, status: "CANCELADO" } : p)));
  }

  return (
    <div>
      <h1>Pedidos</h1>
      <nav>
        {role === "CLIENTE" && <Link to="/perfil">Meu perfil</Link>}
        {role === "CLIENTE" && <Link to="/pedidos/novo">Novo pedido</Link>}
        {role === "AGENTE" && <Link to="/automoveis">Automóveis</Link>}
        <button onClick={logout}>Sair</button>
      </nav>
      <ul>
        {pedidos.map((p) => (
          <li key={p.id}>
            Pedido #{p.id} — automóvel {p.automovelId} — status {p.status}
            {role === "AGENTE" && (
              <>
                <button onClick={() => avaliar(p.id, true)}>Aprovar</button>
                <button onClick={() => avaliar(p.id, false)}>Rejeitar</button>
              </>
            )}
            {role === "CLIENTE" && (p.status === "INTRODUZIDO" || p.status === "EM_ANALISE") && (
              <button onClick={() => cancelar(p.id)}>Cancelar</button>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
