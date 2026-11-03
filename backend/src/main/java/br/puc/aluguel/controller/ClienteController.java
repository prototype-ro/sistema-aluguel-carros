package br.puc.aluguel.controller;

import br.puc.aluguel.dto.AtualizarClienteRequest;
import br.puc.aluguel.dto.ClienteResponse;
import br.puc.aluguel.dto.EmpregadorRequest;
import br.puc.aluguel.model.Cliente;
import br.puc.aluguel.security.UsuarioDetails;
import br.puc.aluguel.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/me")
    public ResponseEntity<ClienteResponse> meuPerfil(@AuthenticationPrincipal UsuarioDetails principal) {
        Cliente cliente = clienteService.buscarPorUsuarioId(principal.getUsuario().getId());
        return ResponseEntity.ok(toResponse(cliente));
    }

    @PutMapping("/me")
    public ResponseEntity<ClienteResponse> atualizarMeuPerfil(
            @AuthenticationPrincipal UsuarioDetails principal,
            @Valid @RequestBody AtualizarClienteRequest request) {
        Cliente cliente = clienteService.buscarPorUsuarioId(principal.getUsuario().getId());
        Cliente atualizado = clienteService.atualizar(cliente.getId(), request);
        return ResponseEntity.ok(toResponse(atualizado));
    }

    private ClienteResponse toResponse(Cliente cliente) {
        List<EmpregadorRequest> empregadores = cliente.getEmpregadores().stream()
                .map(e -> new EmpregadorRequest(e.getNome(), e.getRendimento()))
                .toList();
        return new ClienteResponse(
                cliente.getId(), cliente.getRg(), cliente.getCpf(), cliente.getNome(),
                cliente.getEndereco(), cliente.getProfissao(), empregadores
        );
    }
}
