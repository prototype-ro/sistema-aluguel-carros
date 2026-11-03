package br.puc.aluguel.dto;

import jakarta.validation.constraints.NotNull;

public record AvaliarPedidoRequest(@NotNull Boolean aprovado) {
}
