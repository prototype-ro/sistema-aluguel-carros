import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/client";

export default function RegistroCliente() {
  const [form, setForm] = useState({
    login: "", senha: "", rg: "", cpf: "", nome: "", endereco: "", profissao: "",
  });
  const [erro, setErro] = useState("");
  const navigate = useNavigate();

  function update(field) {
    return (e) => setForm({ ...form, [field]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    try {
      await api.post("/auth/registrar/cliente", { ...form, empregadores: [] });
      navigate("/login");
    } catch {
      setErro("Não foi possível registrar. Verifique os dados.");
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h1>Registro de Cliente</h1>
      {erro && <p role="alert">{erro}</p>}
      <input placeholder="Login" value={form.login} onChange={update("login")} />
      <input placeholder="Senha" type="password" value={form.senha} onChange={update("senha")} />
      <input placeholder="RG" value={form.rg} onChange={update("rg")} />
      <input placeholder="CPF" value={form.cpf} onChange={update("cpf")} />
      <input placeholder="Nome" value={form.nome} onChange={update("nome")} />
      <input placeholder="Endereço" value={form.endereco} onChange={update("endereco")} />
      <input placeholder="Profissão" value={form.profissao} onChange={update("profissao")} />
      <button type="submit">Registrar</button>
    </form>
  );
}
