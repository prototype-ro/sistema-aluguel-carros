import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/client";
import Layout from "../components/Layout";

export default function NovoPedido() {
  const [automoveis, setAutomoveis] = useState([]);
  const [automovelId, setAutomovelId] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    api.get("/automoveis").then((res) => setAutomoveis(res.data));
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();
    await api.post("/pedidos", { automovelId: Number(automovelId) });
    navigate("/");
  }

  return (
    <Layout>
      <div className="page-header">
        <h1>Novo Pedido</h1>
      </div>
      <form className="card" onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="automovel">Automóvel</label>
          <select id="automovel" value={automovelId} onChange={(e) => setAutomovelId(e.target.value)}>
            <option value="">Selecione um automóvel</option>
            {automoveis.map((a) => (
              <option key={a.id} value={a.id}>{a.marca} {a.modelo} — {a.placa}</option>
            ))}
          </select>
        </div>
        <button className="btn btn-primary" type="submit" disabled={!automovelId}>Criar pedido</button>
      </form>
    </Layout>
  );
}
