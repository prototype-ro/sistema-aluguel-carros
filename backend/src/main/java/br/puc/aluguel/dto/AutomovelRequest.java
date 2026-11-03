package br.puc.aluguel.dto;

import br.puc.aluguel.model.TipoProprietario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AutomovelRequest(
        @NotBlank String matricula,
        @NotNull Integer ano,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotBlank String placa,
        @NotNull TipoProprietario tipoProprietario,
        @NotNull Long proprietarioId
) {
}
