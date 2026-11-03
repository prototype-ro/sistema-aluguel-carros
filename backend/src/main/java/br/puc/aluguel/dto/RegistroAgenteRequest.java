package br.puc.aluguel.dto;

import br.puc.aluguel.model.TipoAgente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroAgenteRequest(
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull TipoAgente tipo,
        @NotBlank String nome
) {
}
