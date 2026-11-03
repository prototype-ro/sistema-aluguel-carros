package br.puc.aluguel.service;

import br.puc.aluguel.dto.*;
import br.puc.aluguel.model.*;
import br.puc.aluguel.repository.AgenteRepository;
import br.puc.aluguel.repository.ClienteRepository;
import br.puc.aluguel.repository.UsuarioRepository;
import br.puc.aluguel.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            AgenteRepository agenteRepository,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
        this.jwtService = jwtService;
    }

    public Cliente registrarCliente(RegistroClienteRequest request) {
        if (request.empregadores() != null && request.empregadores().size() > 3) {
            throw new IllegalArgumentException("Cliente pode ter no máximo 3 empregadores");
        }
        if (usuarioRepository.existsByLogin(request.login())) {
            throw new IllegalArgumentException("Login já cadastrado");
        }
        if (clienteRepository.existsByCpf(request.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(request.login());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setRole(Role.CLIENTE);
        usuario = usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setRg(request.rg());
        cliente.setCpf(request.cpf());
        cliente.setNome(request.nome());
        cliente.setEndereco(request.endereco());
        cliente.setProfissao(request.profissao());

        List<Empregador> empregadores = new ArrayList<>();
        if (request.empregadores() != null) {
            for (EmpregadorRequest er : request.empregadores()) {
                Empregador empregador = new Empregador();
                empregador.setCliente(cliente);
                empregador.setNome(er.nome());
                empregador.setRendimento(er.rendimento());
                empregadores.add(empregador);
            }
        }
        cliente.setEmpregadores(empregadores);

        return clienteRepository.save(cliente);
    }

    public Agente registrarAgente(RegistroAgenteRequest request) {
        if (usuarioRepository.existsByLogin(request.login())) {
            throw new IllegalArgumentException("Login já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(request.login());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setRole(Role.AGENTE);
        usuario = usuarioRepository.save(usuario);

        Agente agente = new Agente();
        agente.setUsuario(usuario);
        agente.setTipo(request.tipo());
        agente.setNome(request.nome());

        return agenteRepository.save(agente);
    }

    public TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByLogin(request.login())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.gerarToken(usuario.getLogin());
        return new TokenResponse(token, usuario.getRole().name());
    }
}
