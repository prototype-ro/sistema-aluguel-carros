package br.puc.aluguel.controller;

import br.puc.aluguel.dto.*;
import br.puc.aluguel.model.Pedido;
import br.puc.aluguel.security.UsuarioDetails;
import br.puc.aluguel.service.ClienteService;
import br.puc.aluguel.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(
            @AuthenticationPrincipal UsuarioDetails principal,
            @Valid @RequestBody PedidoRequest request) {
        var cliente = clienteService.buscarPorUsuarioId(principal.getUsuario().getId());
        return ResponseEntity.ok(toResponse(pedidoService.criar(cliente, request)));
    }

    @GetMapping("/meus")
    public ResponseEntity<List<PedidoResponse>> meusPedidos(@AuthenticationPrincipal UsuarioDetails principal) {
        var cliente = clienteService.buscarPorUsuarioId(principal.getUsuario().getId());
        return ResponseEntity.ok(pedidoService.listarPorCliente(cliente.getId()).stream().map(this::toResponse).toList());
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<PedidoResponse>> pendentes() {
        return ResponseEntity.ok(pedidoService.listarPendentes().stream().map(this::toResponse).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> modificar(
            @AuthenticationPrincipal UsuarioDetails principal,
            @PathVariable Long id, @Valid @RequestBody PedidoRequest request) {
        var cliente = clienteService.buscarPorUsuarioId(principal.getUsuario().getId());
        return ResponseEntity.ok(toResponse(pedidoService.modificarAutomovel(cliente.getId(), id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PedidoResponse> cancelar(
            @AuthenticationPrincipal UsuarioDetails principal,
            @PathVariable Long id) {
        var cliente = clienteService.buscarPorUsuarioId(principal.getUsuario().getId());
        return ResponseEntity.ok(toResponse(pedidoService.cancelar(cliente.getId(), id)));
    }

    @PutMapping("/avaliar/{id}")
    public ResponseEntity<PedidoResponse> avaliar(@PathVariable Long id, @Valid @RequestBody AvaliarPedidoRequest request) {
        return ResponseEntity.ok(toResponse(pedidoService.avaliar(id, request)));
    }

    private PedidoResponse toResponse(Pedido p) {
        return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getAutomovel().getId(), p.getDataPedido(), p.getStatus());
    }
}
