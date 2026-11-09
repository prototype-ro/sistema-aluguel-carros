import { useEffect, useState } from "react";
import api from "../api/client";
import { useAuth } from "../context/AuthContext";
import Layout from "../components/Layout";
import StatusBadge from "../components/StatusBadge";

export default function Dashboard() {
  const { role } = useAuth();
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
    <Layout>
      <div className="page-header">
        <h1>{role === "AGENTE" ? "Pedidos pendentes" : "Meus pedidos"}</h1>
      </div>
      {pedidos.length === 0 && <div className="card empty-state">Nenhum pedido por aqui.</div>}
      <div className="list">
        {pedidos.map((p) => (
          <div className="list-item" key={p.id}>
            <div className="list-item-main">
              <span className="list-item-title">Pedido #{p.id}</span>
              <span className="list-item-sub">Automóvel #{p.automovelId}</span>
            </div>
            <StatusBadge status={p.status} />
            <div className="list-item-actions">
              {role === "AGENTE" && (
                <>
                  <button className="btn btn-success btn-sm" onClick={() => avaliar(p.id, true)}>Aprovar</button>
                  <button className="btn btn-danger btn-sm" onClick={() => avaliar(p.id, false)}>Rejeitar</button>
                </>
              )}
              {role === "CLIENTE" && (p.status === "INTRODUZIDO" || p.status === "EM_ANALISE") && (
                <button className="btn btn-danger btn-sm" onClick={() => cancelar(p.id)}>Cancelar</button>
              )}
            </div>
          </div>
        ))}
      </div>
    </Layout>
  );
}
