import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const { login: fazerLogin } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    try {
      await fazerLogin(login, senha);
      navigate("/");
    } catch {
      setErro("Login ou senha inválidos");
    }
  }

  return (
    <div className="auth-shell">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Aluguel de Carros</h1>
        {erro && <p className="alert alert-error" role="alert">{erro}</p>}
        <div className="field">
          <label htmlFor="login">Login</label>
          <input id="login" value={login} onChange={(e) => setLogin(e.target.value)} />
        </div>
        <div className="field">
          <label htmlFor="senha">Senha</label>
          <input id="senha" type="password" value={senha} onChange={(e) => setSenha(e.target.value)} />
        </div>
        <button className="btn btn-primary" type="submit" style={{ width: "100%", justifyContent: "center" }}>Entrar</button>
        <p className="auth-footer"><Link to="/registro">Criar conta de cliente</Link></p>
      </form>
    </div>
  );
}
