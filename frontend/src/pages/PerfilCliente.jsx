import { useEffect, useState } from "react";
import api from "../api/client";
import Layout from "../components/Layout";

export default function PerfilCliente() {
  const [perfil, setPerfil] = useState(null);
  const [salvo, setSalvo] = useState(false);

  useEffect(() => {
    api.get("/clientes/me").then((res) => setPerfil(res.data));
  }, []);

  function update(field) {
    return (e) => setPerfil({ ...perfil, [field]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSalvo(false);
    const { rg, endereco, profissao, empregadores } = perfil;
    await api.put("/clientes/me", { rg, endereco, profissao, empregadores });
    setSalvo(true);
  }

  if (!perfil) {
    return (
      <Layout>
        <p className="empty-state">Carregando...</p>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="page-header">
        <h1>Meu Perfil</h1>
      </div>
      <form className="card" onSubmit={handleSubmit}>
        {salvo && <p className="alert alert-success" role="status">Perfil atualizado</p>}
        <div className="form-row" style={{ marginBottom: 18 }}>
          <div className="field">
            <label>Nome</label>
            <input value={perfil.nome} disabled />
          </div>
          <div className="field">
            <label>CPF</label>
            <input value={perfil.cpf} disabled />
          </div>
        </div>
        <div className="field">
          <label htmlFor="rg">RG</label>
          <input id="rg" value={perfil.rg} onChange={update("rg")} />
        </div>
        <div className="field">
          <label htmlFor="endereco">Endereço</label>
          <input id="endereco" value={perfil.endereco} onChange={update("endereco")} />
        </div>
        <div className="field">
          <label htmlFor="profissao">Profissão</label>
          <input id="profissao" value={perfil.profissao || ""} onChange={update("profissao")} />
        </div>
        <button className="btn btn-primary" type="submit">Salvar</button>
      </form>
    </Layout>
  );
}
