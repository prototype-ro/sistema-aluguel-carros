package br.puc.aluguel.service;

import br.puc.aluguel.dto.AvaliarPedidoRequest;
import br.puc.aluguel.dto.PedidoRequest;
import br.puc.aluguel.model.*;
import br.puc.aluguel.repository.AutomovelRepository;
import br.puc.aluguel.repository.ContratoRepository;
import br.puc.aluguel.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AutomovelRepository automovelRepository;
    private final ContratoRepository contratoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            AutomovelRepository automovelRepository,
            ContratoRepository contratoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.automovelRepository = automovelRepository;
        this.contratoRepository = contratoRepository;
    }

    public Pedido criar(Cliente cliente, PedidoRequest request) {
        Automovel automovel = automovelRepository.findById(request.automovelId())
                .orElseThrow(() -> new IllegalArgumentException("Automóvel não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus(StatusPedido.INTRODUZIDO);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> listarPendentes() {
        return pedidoRepository.findByStatusIn(List.of(StatusPedido.INTRODUZIDO, StatusPedido.EM_ANALISE));
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    }

    public Pedido modificarAutomovel(Long id, PedidoRequest request) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getStatus() != StatusPedido.INTRODUZIDO && pedido.getStatus() != StatusPedido.EM_ANALISE) {
            throw new IllegalStateException("Pedido não pode mais ser modificado");
        }
        Automovel automovel = automovelRepository.findById(request.automovelId())
                .orElseThrow(() -> new IllegalArgumentException("Automóvel não encontrado"));
        pedido.setAutomovel(automovel);
        return pedidoRepository.save(pedido);
    }

    public Pedido cancelar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getStatus() != StatusPedido.INTRODUZIDO && pedido.getStatus() != StatusPedido.EM_ANALISE) {
            throw new IllegalStateException("Pedido não pode mais ser cancelado");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    public Pedido avaliar(Long id, AvaliarPedidoRequest request) {
        Pedido pedido = buscarPorId(id);
        if (Boolean.TRUE.equals(request.aprovado())) {
            pedido.setStatus(StatusPedido.APROVADO);
            pedidoRepository.save(pedido);

            Contrato contrato = new Contrato();
            contrato.setPedido(pedido);
            contrato.setDataInicio(LocalDate.now());
            contrato.setDataFim(LocalDate.now().plusMonths(12));
            contrato.setValor(BigDecimal.ZERO);
            contratoRepository.save(contrato);
        } else {
            pedido.setStatus(StatusPedido.REJEITADO);
            pedidoRepository.save(pedido);
        }
        return pedido;
    }
}
