package br.puc.aluguel.service;

import br.puc.aluguel.dto.EmpregadorRequest;
import br.puc.aluguel.dto.RegistroClienteRequest;
import br.puc.aluguel.model.Cliente;
import br.puc.aluguel.model.Usuario;
import br.puc.aluguel.repository.AgenteRepository;
import br.puc.aluguel.repository.ClienteRepository;
import br.puc.aluguel.repository.UsuarioRepository;
import br.puc.aluguel.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private AgenteRepository agenteRepository;
    @Mock private JwtService jwtService;
    @InjectMocks private AuthService authService;

    @Test
    void registraClienteComAteTresEmpregadores() {
        var request = new RegistroClienteRequest(
                "cliente1", "senha123", "MG1234567", "12345678901", "Fulano de Tal",
                "Rua A, 1", "Engenheiro",
                List.of(new EmpregadorRequest("Empresa X", new BigDecimal("5000.00")))
        );

        when(usuarioRepository.existsByLogin("cliente1")).thenReturn(false);
        when(clienteRepository.existsByCpf("12345678901")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente cliente = authService.registrarCliente(request);

        assertEquals("Fulano de Tal", cliente.getNome());
        assertEquals(1, cliente.getEmpregadores().size());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void rejeitaClienteComMaisDeTresEmpregadores() {
        var request = new RegistroClienteRequest(
                "cliente2", "senha123", "MG1234567", "98765432100", "Ciclano",
                "Rua B, 2", "Médico",
                List.of(
                        new EmpregadorRequest("A", new BigDecimal("1000")),
                        new EmpregadorRequest("B", new BigDecimal("1000")),
                        new EmpregadorRequest("C", new BigDecimal("1000")),
                        new EmpregadorRequest("D", new BigDecimal("1000"))
                )
        );

        assertThrows(IllegalArgumentException.class, () -> authService.registrarCliente(request));
    }

    @Test
    void loginComCredenciaisValidasRetornaToken() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("cliente1");
        usuario.setSenhaHash(new BCryptPasswordEncoder().encode("senha123"));
        usuario.setRole(br.puc.aluguel.model.Role.CLIENTE);

        when(usuarioRepository.findByLogin("cliente1")).thenReturn(Optional.of(usuario));
        when(jwtService.gerarToken("cliente1")).thenReturn("token-fake");

        var resposta = authService.login(new br.puc.aluguel.dto.LoginRequest("cliente1", "senha123"));

        assertEquals("token-fake", resposta.token());
        assertEquals("CLIENTE", resposta.role());
    }
}
