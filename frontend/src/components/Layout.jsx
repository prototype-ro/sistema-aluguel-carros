import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Layout({ children }) {
  const { role, logout } = useAuth();

  return (
    <div>
      <header className="topbar">
        <Link to="/" className="topbar-brand">Aluguel de Carros</Link>
        <nav className="topbar-nav">
          {role === "CLIENTE" && <Link to="/perfil" className="btn btn-ghost btn-sm">Meu perfil</Link>}
          {role === "CLIENTE" && <Link to="/pedidos/novo" className="btn btn-primary btn-sm">Novo pedido</Link>}
          {role === "AGENTE" && <Link to="/automoveis" className="btn btn-ghost btn-sm">Automóveis</Link>}
          <button className="btn btn-ghost btn-sm" onClick={logout}>Sair</button>
        </nav>
      </header>
      <main className="page">{children}</main>
    </div>
  );
}
