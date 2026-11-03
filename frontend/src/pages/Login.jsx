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
    <form onSubmit={handleSubmit}>
      <h1>Login</h1>
      {erro && <p role="alert">{erro}</p>}
      <input placeholder="Login" value={login} onChange={(e) => setLogin(e.target.value)} />
      <input placeholder="Senha" type="password" value={senha} onChange={(e) => setSenha(e.target.value)} />
      <button type="submit">Entrar</button>
      <p><Link to="/registro">Criar conta de cliente</Link></p>
    </form>
  );
}
