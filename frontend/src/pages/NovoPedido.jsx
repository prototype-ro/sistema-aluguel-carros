import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/client";

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
    <form onSubmit={handleSubmit}>
      <h1>Novo Pedido</h1>
      <select value={automovelId} onChange={(e) => setAutomovelId(e.target.value)}>
        <option value="">Selecione um automóvel</option>
        {automoveis.map((a) => (
          <option key={a.id} value={a.id}>{a.marca} {a.modelo} — {a.placa}</option>
        ))}
      </select>
      <button type="submit" disabled={!automovelId}>Criar pedido</button>
    </form>
  );
}
