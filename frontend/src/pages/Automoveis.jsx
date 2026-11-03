import { useEffect, useState } from "react";
import api from "../api/client";

export default function Automoveis() {
  const [automoveis, setAutomoveis] = useState([]);
  const [form, setForm] = useState({
    matricula: "", ano: "", marca: "", modelo: "", placa: "",
    tipoProprietario: "EMPRESA", proprietarioId: "",
  });

  function carregar() {
    api.get("/automoveis").then((res) => setAutomoveis(res.data));
  }

  useEffect(carregar, []);

  function update(field) {
    return (e) => setForm({ ...form, [field]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    await api.post("/automoveis", { ...form, ano: Number(form.ano), proprietarioId: Number(form.proprietarioId) });
    carregar();
  }

  return (
    <div>
      <h1>Automóveis</h1>
      <form onSubmit={handleSubmit}>
        <input placeholder="Matrícula" value={form.matricula} onChange={update("matricula")} />
        <input placeholder="Ano" type="number" value={form.ano} onChange={update("ano")} />
        <input placeholder="Marca" value={form.marca} onChange={update("marca")} />
        <input placeholder="Modelo" value={form.modelo} onChange={update("modelo")} />
        <input placeholder="Placa" value={form.placa} onChange={update("placa")} />
        <select value={form.tipoProprietario} onChange={update("tipoProprietario")}>
          <option value="EMPRESA">Empresa</option>
          <option value="BANCO">Banco</option>
          <option value="CLIENTE">Cliente</option>
        </select>
        <input placeholder="ID do proprietário" type="number" value={form.proprietarioId} onChange={update("proprietarioId")} />
        <button type="submit">Cadastrar</button>
      </form>
      <ul>
        {automoveis.map((a) => (
          <li key={a.id}>{a.marca} {a.modelo} ({a.ano}) — {a.placa}</li>
        ))}
      </ul>
    </div>
  );
}
