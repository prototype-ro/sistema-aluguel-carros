package br.puc.aluguel.service;

import br.puc.aluguel.dto.AvaliarPedidoRequest;
import br.puc.aluguel.dto.PedidoRequest;
import br.puc.aluguel.model.*;
import br.puc.aluguel.repository.AutomovelRepository;
import br.puc.aluguel.repository.ContratoRepository;
import br.puc.aluguel.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private AutomovelRepository automovelRepository;
    @Mock private ContratoRepository contratoRepository;
    @InjectMocks private PedidoService pedidoService;

    @Test
    void criaPedidoComStatusIntroduzido() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        Automovel automovel = new Automovel();
        automovel.setId(2L);

        when(automovelRepository.findById(2L)).thenReturn(Optional.of(automovel));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido pedido = pedidoService.criar(cliente, new PedidoRequest(2L));

        assertEquals(StatusPedido.INTRODUZIDO, pedido.getStatus());
        assertEquals(automovel, pedido.getAutomovel());
    }

    @Test
    void aprovarPedidoCriaContratoEAtualizaStatus() {
        Pedido pedido = new Pedido();
        pedido.setId(10L);
        pedido.setStatus(StatusPedido.EM_ANALISE);

        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(contratoRepository.save(any(Contrato.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido avaliado = pedidoService.avaliar(10L, new AvaliarPedidoRequest(true));

        assertEquals(StatusPedido.APROVADO, avaliado.getStatus());
        verify(contratoRepository).save(any(Contrato.class));
    }

    @Test
    void rejeitarPedidoNaoCriaContrato() {
        Pedido pedido = new Pedido();
        pedido.setId(11L);
        pedido.setStatus(StatusPedido.EM_ANALISE);

        when(pedidoRepository.findById(11L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido avaliado = pedidoService.avaliar(11L, new AvaliarPedidoRequest(false));

        assertEquals(StatusPedido.REJEITADO, avaliado.getStatus());
        verify(contratoRepository, never()).save(any(Contrato.class));
    }

    @Test
    void cancelarPedidoJaAprovadoLancaExcecao() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        Pedido pedido = new Pedido();
        pedido.setId(12L);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.APROVADO);

        when(pedidoRepository.findById(12L)).thenReturn(Optional.of(pedido));

        assertThrows(IllegalStateException.class, () -> pedidoService.cancelar(1L, 12L));
    }

    @Test
    void cancelarPedidoDeOutroClienteLancaAcessoNegado() {
        Cliente dono = new Cliente();
        dono.setId(1L);
        Pedido pedido = new Pedido();
        pedido.setId(13L);
        pedido.setCliente(dono);
        pedido.setStatus(StatusPedido.INTRODUZIDO);

        when(pedidoRepository.findById(13L)).thenReturn(Optional.of(pedido));

        assertThrows(br.puc.aluguel.exception.AcessoNegadoException.class,
                () -> pedidoService.cancelar(2L, 13L));
    }
}
