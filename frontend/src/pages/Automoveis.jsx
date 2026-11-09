import { useEffect, useState } from "react";
import api from "../api/client";
import Layout from "../components/Layout";

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
    <Layout>
      <div className="page-header">
        <h1>Automóveis</h1>
      </div>

      <form className="card" onSubmit={handleSubmit} style={{ marginBottom: 24 }}>
        <h2>Cadastrar automóvel</h2>
        <div className="form-grid" style={{ marginTop: 14 }}>
          <div className="field">
            <label htmlFor="matricula">Matrícula</label>
            <input id="matricula" value={form.matricula} onChange={update("matricula")} />
          </div>
          <div className="field">
            <label htmlFor="ano">Ano</label>
            <input id="ano" type="number" value={form.ano} onChange={update("ano")} />
          </div>
          <div className="field">
            <label htmlFor="marca">Marca</label>
            <input id="marca" value={form.marca} onChange={update("marca")} />
          </div>
          <div className="field">
            <label htmlFor="modelo">Modelo</label>
            <input id="modelo" value={form.modelo} onChange={update("modelo")} />
          </div>
          <div className="field">
            <label htmlFor="placa">Placa</label>
            <input id="placa" value={form.placa} onChange={update("placa")} />
          </div>
          <div className="field">
            <label htmlFor="tipoProprietario">Tipo de proprietário</label>
            <select id="tipoProprietario" value={form.tipoProprietario} onChange={update("tipoProprietario")}>
              <option value="EMPRESA">Empresa</option>
              <option value="BANCO">Banco</option>
              <option value="CLIENTE">Cliente</option>
            </select>
          </div>
          <div className="field field-full">
            <label htmlFor="proprietarioId">ID do proprietário</label>
            <input id="proprietarioId" type="number" value={form.proprietarioId} onChange={update("proprietarioId")} />
          </div>
        </div>
        <button className="btn btn-primary" type="submit">Cadastrar</button>
      </form>

      <div className="list">
        {automoveis.map((a) => (
          <div className="list-item" key={a.id}>
            <div className="list-item-main">
              <span className="list-item-title">{a.marca} {a.modelo} ({a.ano})</span>
              <span className="list-item-sub">Placa {a.placa}</span>
            </div>
          </div>
        ))}
      </div>
    </Layout>
  );
}
