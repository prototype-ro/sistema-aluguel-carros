package br.puc.aluguel.dto;

import br.puc.aluguel.model.StatusPedido;
import java.time.LocalDate;

public record PedidoResponse(
        Long id, Long clienteId, Long automovelId, LocalDate dataPedido, StatusPedido status
) {
}
