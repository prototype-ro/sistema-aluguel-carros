import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
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
    <div className="auth-shell">
      <form className="auth-card auth-card-wide" onSubmit={handleSubmit}>
        <h1>Registro de Cliente</h1>
        {erro && <p className="alert alert-error" role="alert">{erro}</p>}
        <div className="form-grid">
          <div className="field">
            <label htmlFor="login">Login</label>
            <input id="login" value={form.login} onChange={update("login")} />
          </div>
          <div className="field">
            <label htmlFor="senha">Senha</label>
            <input id="senha" type="password" value={form.senha} onChange={update("senha")} />
          </div>
          <div className="field">
            <label htmlFor="rg">RG</label>
            <input id="rg" value={form.rg} onChange={update("rg")} />
          </div>
          <div className="field">
            <label htmlFor="cpf">CPF</label>
            <input id="cpf" value={form.cpf} onChange={update("cpf")} />
          </div>
          <div className="field field-full">
            <label htmlFor="nome">Nome</label>
            <input id="nome" value={form.nome} onChange={update("nome")} />
          </div>
          <div className="field field-full">
            <label htmlFor="endereco">Endereço</label>
            <input id="endereco" value={form.endereco} onChange={update("endereco")} />
          </div>
          <div className="field field-full">
            <label htmlFor="profissao">Profissão</label>
            <input id="profissao" value={form.profissao} onChange={update("profissao")} />
          </div>
        </div>
        <button className="btn btn-primary" type="submit" style={{ width: "100%", justifyContent: "center" }}>Registrar</button>
        <p className="auth-footer"><Link to="/login">Já tem conta? Entrar</Link></p>
      </form>
    </div>
  );
}
