import { useEffect, useState } from "react";
import api from "../api/client";

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

  if (!perfil) return <p>Carregando...</p>;

  return (
    <form onSubmit={handleSubmit}>
      <h1>Meu Perfil</h1>
      {salvo && <p role="status">Perfil atualizado</p>}
      <p>Nome: {perfil.nome}</p>
      <p>CPF: {perfil.cpf}</p>
      <input placeholder="RG" value={perfil.rg} onChange={update("rg")} />
      <input placeholder="Endereço" value={perfil.endereco} onChange={update("endereco")} />
      <input placeholder="Profissão" value={perfil.profissao || ""} onChange={update("profissao")} />
      <button type="submit">Salvar</button>
    </form>
  );
}
